package ru.logosph.myfinancemanager.domain.usecases;

import static kotlin.io.TextStreamsKt.readText;
import static kotlinx.coroutines.flow.FlowKt.skip;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;

import ru.logosph.myfinancemanager.domain.repository_interfaces.CurrencyRepository;

public class CalculateDollarsUseCase {
    public static double execute(CurrencyRepository repo, int total) {
//        Log.d("CalculateDollarsUseCase", "execute: " + parse(repo.loadCurrencies()));
        return parse(repo.loadCurrencies()) * total;
    }

    private static double parse(String response) {
        XmlPullParser parser = Xml.newPullParser();

        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(new StringReader(response));
            parser.nextTag();
            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                if (name.equals("Valute")) {
                    String charCode = "";
                    String value = "";
                    while (true) {
                        parser.next();
                        if (parser.getName() != null && parser.getName().equals("Valute")) {
                            break;
                        }
                        if (parser.getEventType() != XmlPullParser.START_TAG) {
                            continue;
                        }
                        String tagName = parser.getName();
                        if (tagName.equals("CharCode")) {
                            charCode = readText(parser);
                        } else if (tagName.equals("VunitRate")) {
                            value = readText(parser);
                        }
                    }
                    if (charCode.equals("USD")) {
                        return 1 / Double.parseDouble(value.replace(",", "."));
                    }
                }
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
}
