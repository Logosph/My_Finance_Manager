package ru.logosph.myfinancemanager.ui.DiagramView;

import java.util.List;
import java.util.AbstractMap.SimpleEntry;

public interface AnalyticalPieChartInterface {

    /**
     * Method for adding a list of data to display on the chart.
     * @param list - the list of data, the type of which you can change
     * to your defined model.
     */
    void setDataChart(List<SimpleEntry<Integer, String>> list);

    /**
     * Method to activate drawing animation.
     */
    void startAnimation();

    void setAnimationEnabled(boolean enabled);
}
