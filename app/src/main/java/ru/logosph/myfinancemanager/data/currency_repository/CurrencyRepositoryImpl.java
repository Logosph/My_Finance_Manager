package ru.logosph.myfinancemanager.data.currency_repository;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.logosph.myfinancemanager.domain.repository_interfaces.CurrencyRepository;

public class CurrencyRepositoryImpl implements CurrencyRepository {
    @Override
    public String loadCurrencies() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://www.cbr.ru/scripts/XML_daily.asp?date_req=06/06/2024")
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


}
