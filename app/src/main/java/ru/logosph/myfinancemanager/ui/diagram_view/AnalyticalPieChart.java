package ru.logosph.myfinancemanager.ui.diagram_view;

import static ru.logosph.myfinancemanager.Methods.dpToPx;
import static ru.logosph.myfinancemanager.Methods.spToPx;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.logosph.myfinancemanager.Methods;
import ru.logosph.myfinancemanager.R;

public class AnalyticalPieChart extends View implements AnalyticalPieChartInterface {

    private boolean isAnimationEnabled = true;

    public interface TextFormatter {
        String format(int value);
    }

    @Override
    public void setAnimationEnabled(boolean enabled) {
        this.isAnimationEnabled = enabled;
    }

    /**
     * Базовые значения для полей и самой [AnalyticalPieChart]
     */
    private static final int DEFAULT_MARGIN_TEXT_1 = 2;
    private static final int DEFAULT_MARGIN_TEXT_2 = 10;
    private static final int DEFAULT_MARGIN_TEXT_3 = 2;
    private static final int DEFAULT_MARGIN_SMALL_CIRCLE = 12;
    private static final String ANALYTICAL_PIE_CHART_KEY = "AnalyticalPieChartArrayData";

    /* Процент ширины для отображения текста от общей ширины View */
    private static final double TEXT_WIDTH_PERCENT = 0.40;

    /* Процент ширины для отображения круговой диаграммы от общей ширины View */
    private static final double CIRCLE_WIDTH_PERCENT = 0.50;

    /* Базовые значения ширины и высоты View */
    private static final int DEFAULT_VIEW_SIZE_HEIGHT = 150;
    private static final int DEFAULT_VIEW_SIZE_WIDTH = 250;

    private float marginTextFirst = dpToPx(DEFAULT_MARGIN_TEXT_1, getContext());
    private float marginTextSecond = dpToPx(DEFAULT_MARGIN_TEXT_2, getContext());
    private float marginTextThird = dpToPx(DEFAULT_MARGIN_TEXT_3, getContext());
    private float marginSmallCircle = dpToPx(DEFAULT_MARGIN_SMALL_CIRCLE, getContext());
    private float marginText = marginTextFirst + marginTextSecond;
    private RectF circleRect = new RectF();
    private float circleStrokeWidth = dpToPx(6, getContext());
    private float circleRadius = 0F;
    private float circlePadding = dpToPx(8, getContext());
    private boolean circlePaintRoundSize = true;
    private float circleSectionSpace = 3F;
    private float circleCenterX = 0F;
    private float circleCenterY = 0F;
    private TextPaint numberTextPaint = new TextPaint();
    private TextPaint descriptionTextPain = new TextPaint();
    private TextPaint amountTextPaint = new TextPaint();
    private float textStartX = 0F;
    private float textStartY = 0F;
    private int textHeight = 0;
    private float textCircleRadius = dpToPx(4, getContext());
    private String textAmountStr = "";
    private float textAmountY = 0F;
    private float textAmountXNumber = 0F;
    private float textAmountXDescription = 0F;
    private float textAmountYDescription = 0F;
    private int totalAmount = 0;
    private int totalAmountToDisplay = 0;
    private List<String> pieChartColors = new ArrayList<>();
    private List<AnalyticalPieChartModel> percentageCircleList = new ArrayList<>();
    private List<StaticLayout> textRowList = new ArrayList<>();
    private List<SimpleEntry<Integer, String>> dataList = new ArrayList<>();
    private int animationSweepAngle = 0;
    private TextFormatter textFormatter = value -> String.valueOf(value);

    public void setTextFormatter(TextFormatter textFormatter) {
        this.textFormatter = textFormatter;
    }

    public AnalyticalPieChart(Context context) {
        super(context);
        init(null, 0);
    }

    public AnalyticalPieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public AnalyticalPieChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    public void init(AttributeSet attrs, int defStyleAttr) {
        // Set base values and convert to px
        float textAmountSize = spToPx(22, getContext());
        float textNumberSize = spToPx(20, getContext());
        float textDescriptionSize = spToPx(14, getContext());
        int textAmountColor = Color.WHITE;
        int textNumberColor = Color.WHITE;
        int textDescriptionColor = Color.GRAY;

        // Initialize View fields if Attrs are present
        if (attrs != null) {
            TypedArray typeArray = getContext().obtainStyledAttributes(attrs, R.styleable.AnalyticalPieChart);

            // Color list section
            int colorResId = typeArray.getResourceId(R.styleable.AnalyticalPieChart_pieChartColors, 0);
            pieChartColors = Arrays.asList(getContext().getResources().getStringArray(colorResId));

            // Margin section
            marginTextFirst = typeArray.getDimension(R.styleable.AnalyticalPieChart_pieChartMarginTextFirst, marginTextFirst);
            marginTextSecond = typeArray.getDimension(R.styleable.AnalyticalPieChart_pieChartMarginTextSecond, marginTextSecond);
            marginTextThird = typeArray.getDimension(R.styleable.AnalyticalPieChart_pieChartMarginTextThird, marginTextThird);
            marginSmallCircle = typeArray.getDimension(R.styleable.AnalyticalPieChart_pieChartMarginSmallCircle, marginSmallCircle);

            // Circle diagram section
            circleStrokeWidth = typeArray.getDimension(R.styleable.AnalyticalPieChart_pieChartCircleStrokeWidth, circleStrokeWidth);
            circlePadding = typeArray.getDimension(R.styleable.AnalyticalPieChart_pieChartCirclePadding, circlePadding);
            circlePaintRoundSize = typeArray.getBoolean(R.styleable.AnalyticalPieChart_pieChartCirclePaintRoundSize, circlePaintRoundSize);
            circleSectionSpace = typeArray.getFloat(R.styleable.AnalyticalPieChart_pieChartCircleSectionSpace, circleSectionSpace);

            // Text section
            textCircleRadius = typeArray.getDimension(R.styleable.AnalyticalPieChart_pieChartTextCircleRadius, textCircleRadius);
            textAmountSize = typeArray.getDimension(R.styleable.AnalyticalPieChart_pieChartTextAmountSize, textAmountSize);
            textNumberSize = typeArray.getDimension(R.styleable.AnalyticalPieChart_pieChartTextNumberSize, textNumberSize);
            textDescriptionSize = typeArray.getDimension(R.styleable.AnalyticalPieChart_pieChartTextDescriptionSize, textDescriptionSize);
            textAmountColor = typeArray.getColor(R.styleable.AnalyticalPieChart_pieChartTextAmountColor, textAmountColor);
            textNumberColor = typeArray.getColor(R.styleable.AnalyticalPieChart_pieChartTextNumberColor, textNumberColor);
            textDescriptionColor = typeArray.getColor(R.styleable.AnalyticalPieChart_pieChartTextDescriptionColor, textDescriptionColor);
            textAmountStr = typeArray.getString(R.styleable.AnalyticalPieChart_pieChartTextAmount);
            if (textAmountStr == null) {
                textAmountStr = "";
            }

            typeArray.recycle();
        }

        circlePadding += circleStrokeWidth;

        // Initialize View brushes
        initPains(amountTextPaint, textAmountSize, textAmountColor, false);
        initPains(numberTextPaint, textNumberSize, textNumberColor, false);
        initPains(descriptionTextPain, textDescriptionSize, textDescriptionColor, true);
    }

    /**
     * Implemented method of the interaction interface [AnalyticalPieChartInterface].
     * Adding data to View.
     */
    @Override
    public void setDataChart(List<SimpleEntry<Integer, String>> list) {
        dataList = list;
        calculatePercentageOfData();
        startAnimation();
        requestLayout();
    }

    private void calculatePercentageOfData() {
        totalAmount = 0;
        totalAmountToDisplay = 0;
        for (SimpleEntry<Integer, String> entry : dataList) {
            totalAmount += Math.abs(entry.getKey());
            totalAmountToDisplay += entry.getKey();
        }

        float startAt = circleSectionSpace;
        percentageCircleList = new ArrayList<>();
        for (int index = 0; index < dataList.size(); index++) {
            SimpleEntry<Integer, String> pair = dataList.get(index);
            float percent = Math.abs(pair.getKey()) * 100 / (float) totalAmount - circleSectionSpace;
            percent = Math.max(0, percent);

            AnalyticalPieChartModel resultModel = new AnalyticalPieChartModel(
                    percent,
                    startAt,
                    Color.parseColor(pieChartColors.get(index % pieChartColors.size())),
                    circleStrokeWidth,
                    circlePaintRoundSize
            );
            if (percent != 0) {
                startAt += percent + circleSectionSpace;
            }
            percentageCircleList.add(resultModel);
        }
    }

    /**
     * Implemented method of the interaction interface [AnalyticalPieChartInterface].
     * Start animation of drawing View.
     */
    @Override
    public void startAnimation() {
        if (!isAnimationEnabled) {
            return;
        }
        // Pass values from 0 to 360 (full circle), with a duration of 1.5 seconds
        ValueAnimator animator = ValueAnimator.ofInt(0, 360);
        animator.setDuration(1500); // animation duration in milliseconds
        animator.setInterpolator(new FastOutSlowInInterpolator()); // animation interpolator
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                // Update the value for drawing the chart
                animationSweepAngle = (int) valueAnimator.getAnimatedValue();
                // Force redraw
                invalidate();
            }
        });
        animator.start();
    }

    /**
     * Method to initialize the given TextPaint
     */
    private void initPains(TextPaint textPaint, float textSize, int textColor, boolean isDescription) {
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);

        if (!isDescription) textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
    }

    /**
     * Method to get the size of View by the passed Mode.
     */
    private int resolveDefaultSize(int spec, int defValue) {
        if (MeasureSpec.getMode(spec) == MeasureSpec.UNSPECIFIED) {
            return (int) dpToPx(defValue, getContext()); // Size is not defined by parent layout
        }
        return MeasureSpec.getSize(spec); // Listen to parent layout
    }

    /**
     * Method to create [StaticLayout] with passed values
     */
    private StaticLayout getMultilineText(
            CharSequence text,
            TextPaint textPaint,
            int width,
            int start,
            int end,
            Layout.Alignment alignment,
            TextDirectionHeuristic textDir,
            float spacingMult,
            float spacingAdd) {

        return StaticLayout.Builder
                .obtain(text, start, end, textPaint, width)
                .setAlignment(alignment)
                .setTextDirection(textDir)
                .setLineSpacing(spacingAdd, spacingMult)
                .build();
    }

    /**
     * Method to calculate the height of objects, where an object is a number and its description.
     * Adding an object to the list of lines for drawing [textRowList].
     */
    private int getTextViewHeight(int maxWidth) {
        int textHeight = 0;
        // Go through all the data that was passed to the View
        for (SimpleEntry<Integer, String> entry : dataList) {
            // Create a StaticLayout object for the data value
            StaticLayout textLayoutNumber = getMultilineText(
                    entry.getKey().toString(),
                    numberTextPaint,
                    maxWidth,
                    0,
                    entry.getKey().toString().length(),
                    Layout.Alignment.ALIGN_NORMAL,
                    TextDirectionHeuristics.LTR,
                    1f,
                    0f
            );
            // Create a StaticLayout object for the description of the data value
            StaticLayout textLayoutDescription = getMultilineText(
                    entry.getValue(),
                    descriptionTextPain,
                    maxWidth,
                    0,
                    entry.getValue().length(),
                    Layout.Alignment.ALIGN_NORMAL,
                    TextDirectionHeuristics.LTR,
                    1f,
                    0f
            );
            // Save objects to the list for drawing
            textRowList.add(textLayoutNumber);
            textRowList.add(textLayoutDescription);
            // Add up the heights of the texts
            textHeight += textLayoutNumber.getHeight() + textLayoutDescription.getHeight();
        }

        return textHeight;
    }

    /**
     * Method to calculate the height of all text, including margins.
     */
    private int calculateViewHeight(int heightMeasureSpec, int textWidth) {
        // Get the height that the parent layout suggests
        int initSizeHeight = resolveDefaultSize(heightMeasureSpec, DEFAULT_VIEW_SIZE_HEIGHT);
        // Calculate the height of the text taking into account the margins
        textHeight = (int) (dataList.size() * marginText + getTextViewHeight(textWidth));

        // Add the vertical padding of the View to the height value
        int textHeightWithPadding = textHeight + getPaddingTop() + getPaddingBottom();
        return Math.max(textHeightWithPadding, initSizeHeight);
    }

    private int resolveSize(int spec, int contentSize, int defValue) {
        int mode = MeasureSpec.getMode(spec);
        if (mode == MeasureSpec.EXACTLY) {
            return MeasureSpec.getSize(spec);
        } else if (mode == MeasureSpec.AT_MOST) {
            return Math.min(contentSize, MeasureSpec.getSize(spec));
        } else {
            return contentSize;
        }
    }

    /**
     * View lifecycle method.
     * Calculation of the necessary width and height of the View.
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Clear the list of lines for text
        textRowList.clear();

        // Get the width of the View
        int initSizeWidth = resolveSize(widthMeasureSpec, DEFAULT_VIEW_SIZE_WIDTH, DEFAULT_VIEW_SIZE_WIDTH);

        // Calculate the width that the text will occupy
        double textTextWidth = (initSizeWidth * TEXT_WIDTH_PERCENT);

        // Calculate the necessary height for our View
        int contentHeight = calculateViewHeight(heightMeasureSpec, (int) textTextWidth);
        int initSizeHeight = resolveSize(heightMeasureSpec, contentHeight, DEFAULT_VIEW_SIZE_HEIGHT);

        // X and Y coordinates from where the text will be drawn
        textStartX = initSizeWidth - (float) textTextWidth;
        textStartY = (float) initSizeHeight / 2 - textHeight / 2;

        calculateCircleRadius(initSizeWidth, initSizeHeight);

        setMeasuredDimension(initSizeWidth, initSizeHeight);
    }

    /**
     * Method to calculate the radius of the pie chart, set coordinates for drawing.
     */
    private void calculateCircleRadius(int width, int height) {

        // Calculate the width that the chart will occupy
        double circleViewWidth = (width * CIRCLE_WIDTH_PERCENT);
        // Calculate the radius of the pie chart
        circleRadius = (circleViewWidth > height) ?
                (height - circlePadding) / 2 :
                (float) circleViewWidth / 2;

        // Set the location of the pie chart on the View
        circleRect.set(circlePadding, height / 2 - circleRadius, circleRadius * 2 + circlePadding, height / 2 + circleRadius);

        // Coordinates of the center of the pie chart
        circleCenterX = (circleRadius * 2 + circlePadding + circlePadding) / 2;
        circleCenterY = (height / 2 + circleRadius + (height / 2 - circleRadius)) / 2;

        textAmountY = circleCenterY;

        // Create a container to display text in the center of the pie chart
        Rect sizeTextAmountNumber = getWidthOfAmountText(textFormatter.format(totalAmountToDisplay), amountTextPaint);

        // Calculate coordinates to display text in the center of the pie chart
        textAmountXNumber = circleCenterX - sizeTextAmountNumber.width() / 2;
        textAmountXDescription = circleCenterX - getWidthOfAmountText(textAmountStr, descriptionTextPain).width() / 2;
        textAmountYDescription = circleCenterY + sizeTextAmountNumber.height() + marginTextThird;
    }

    /**
     * Method to wrap text in the [Rect] class
     */
    private Rect getWidthOfAmountText(String text, TextPaint textPaint) {
        Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        return bounds;
    }

    /**
     * Method to draw a pie chart on Canvas.
     */
    private void drawCircle(Canvas canvas) {
        // Go through the arcs of the circle
        for (AnalyticalPieChartModel percent : percentageCircleList) {
            // If the percentage of the arc falls under the drawing angle (animationSweepAngle)
            // Display this arc on Canvas
            if (animationSweepAngle > percent.getPercentToStartAt() + percent.getPercentOfCircle()) {
                canvas.drawArc(circleRect, percent.getPercentToStartAt(), percent.getPercentOfCircle(), false, percent.getPaint());
            } else if (animationSweepAngle > percent.getPercentToStartAt()) {
                canvas.drawArc(circleRect, percent.getPercentToStartAt(), animationSweepAngle - percent.getPercentToStartAt(), false, percent.getPaint());
            }
        }
    }

    /**
     * Method to draw all the text of the chart on Canvas.
     */
    private void drawText(Canvas canvas) {
        // Track the Y coordinate when displaying text
        float textBuffY = textStartY;
        // Go through each line
        for (int index = 0; index < textRowList.size(); index++) {
            StaticLayout staticLayout = textRowList.get(index);
            // If this is our data value, then we display a filled circle and text value
            if (index % 2 == 0) {
                Methods.draw(staticLayout, canvas, textStartX + marginSmallCircle + textCircleRadius, textBuffY);
                Paint paint = new Paint();
                paint.setColor(Color.parseColor(pieChartColors.get((index / 2) % pieChartColors.size())));
                canvas.drawCircle(
                        textStartX + marginSmallCircle / 2,
                        textBuffY + staticLayout.getHeight() / 2 + textCircleRadius / 2,
                        textCircleRadius,
                        paint
                );
                // Add height and margin to the Y coordinate
                textBuffY += staticLayout.getHeight() + marginTextFirst;
            } else {
                // Display the description of the value
                Methods.draw(staticLayout, canvas, textStartX, textBuffY);
                textBuffY += staticLayout.getHeight() + marginTextSecond;
            }
        }

        // Display the text result in the center of the pie chart
        canvas.drawText(textFormatter.format(totalAmountToDisplay), textAmountXNumber, textAmountY, amountTextPaint);
        canvas.drawText(textAmountStr, textAmountXDescription, textAmountYDescription, descriptionTextPain);
    }

    /**
     * View lifecycle method.
     * Drawing all necessary components on Canvas.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawCircle(canvas);
        drawText(canvas);
    }



    /**
     * Restoring data from [AnalyticalPieChartState]
     */
    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof AnalyticalPieChartState) {
            AnalyticalPieChartState analyticalPieChartState = (AnalyticalPieChartState) state;
            super.onRestoreInstanceState(analyticalPieChartState.getSuperState());
            dataList = analyticalPieChartState.getDataList() != null ? analyticalPieChartState.getDataList() : new ArrayList<>();
        } else {
            super.onRestoreInstanceState(state);
        }
    }

    /**
     * Saving [dataList] in own [AnalyticalPieChartState]
     */
    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        return new AnalyticalPieChartState(superState, dataList);
    }

}