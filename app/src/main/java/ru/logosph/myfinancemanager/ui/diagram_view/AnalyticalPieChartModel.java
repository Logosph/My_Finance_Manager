package ru.logosph.myfinancemanager.ui.diagram_view;

import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;

public class AnalyticalPieChartModel {
    private float percentOfCircle;
    private float percentToStartAt;
    private int colorOfLine;
    private float stroke;
    private Paint paint;
    private boolean paintRound;

    public AnalyticalPieChartModel() {
        this.percentOfCircle = 0F;
        this.percentToStartAt = 0F;
        this.colorOfLine = 0;
        this.stroke = 0F;
        this.paint = new Paint();
        this.paintRound = true;

    }

    public AnalyticalPieChartModel(float percentOfCircle, float percentToStartAt, int colorOfLine, float stroke, boolean paintRound) {
        this.percentOfCircle = percentOfCircle;
        this.percentToStartAt = percentToStartAt;
        this.colorOfLine = colorOfLine;
        this.stroke = stroke;
        this.paintRound = paintRound;

        // Check for correct percentage.
        if (this.percentOfCircle < 0 || this.percentOfCircle > 100) {
            this.percentOfCircle = 100F;
        }

        // Calculate the passed value on the pie chart.
        this.percentOfCircle = 360 * this.percentOfCircle / 100;

        // Check for correct percentage.
        if (this.percentToStartAt < 0 || this.percentToStartAt > 100) {
            this.percentToStartAt = 0F;
        }

        // Calculate the passed value on the pie chart.
        this.percentToStartAt = 360 * this.percentToStartAt / 100;

        // Set your own color in case of skipping [colorOfLine]
        if (this.colorOfLine == 0) {
            this.colorOfLine = Color.parseColor("#000000");
        }

        // Initialize the brush for drawing
        this.paint = new Paint();
        this.paint.setColor(colorOfLine);
        this.paint.setAntiAlias(true);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(stroke);
        this.paint.setDither(true);

        // Check the need to round the ends of the line object.
        if (this.paintRound){
            this.paint.setStrokeJoin(Paint.Join.ROUND);
            this.paint.setStrokeCap(Paint.Cap.ROUND);
            this.paint.setPathEffect(new CornerPathEffect(8F));
        }
    }

    // Region Getters and Setters
    public float getPercentOfCircle() {
        return percentOfCircle;
    }

    public void setPercentOfCircle(float percentOfCircle) {
        this.percentOfCircle = percentOfCircle;
    }

    public float getPercentToStartAt() {
        return percentToStartAt;
    }

    public void setPercentToStartAt(float percentToStartAt) {
        this.percentToStartAt = percentToStartAt;
    }

    public int getColorOfLine() {
        return colorOfLine;
    }

    public void setColorOfLine(int colorOfLine) {
        this.colorOfLine = colorOfLine;
    }

    public float getStroke() {
        return stroke;
    }

    public void setStroke(float stroke) {
        this.stroke = stroke;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public boolean isPaintRound() {
        return paintRound;
    }

    public void setPaintRound(boolean paintRound) {
        this.paintRound = paintRound;
    }
    // End Region Getters and Setters
}
