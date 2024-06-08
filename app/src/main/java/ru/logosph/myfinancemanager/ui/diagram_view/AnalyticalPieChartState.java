package ru.logosph.myfinancemanager.ui.diagram_view;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View.BaseSavedState;

import java.util.List;
import java.util.ArrayList;
import java.util.AbstractMap.SimpleEntry;

public class AnalyticalPieChartState extends BaseSavedState {
    private List<SimpleEntry<Integer, String>> dataList;

    public AnalyticalPieChartState(Parcelable superSavedState, List<SimpleEntry<Integer, String>> dataList) {
        super(superSavedState);
        this.dataList = dataList;
    }

    private AnalyticalPieChartState(Parcel in) {
        super(in);
        this.dataList = new ArrayList<>();
        in.readList(this.dataList, SimpleEntry.class.getClassLoader());
    }

    public List<SimpleEntry<Integer, String>> getDataList() {
        return dataList;
    }

    public void setDataList(List<SimpleEntry<Integer, String>> dataList) {
        this.dataList = dataList;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeList(this.dataList);
    }

    public static final Parcelable.Creator<AnalyticalPieChartState> CREATOR = new Parcelable.Creator<AnalyticalPieChartState>() {
        public AnalyticalPieChartState createFromParcel(Parcel in) {
            return new AnalyticalPieChartState(in);
        }

        public AnalyticalPieChartState[] newArray(int size) {
            return new AnalyticalPieChartState[size];
        }
    };
}