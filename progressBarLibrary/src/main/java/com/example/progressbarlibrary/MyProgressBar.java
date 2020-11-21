package com.example.progressbarlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import java.text.DecimalFormat;

public class MyProgressBar extends View {
    private Paint paint;
    protected Paint textPaint;
    private RectF rectF = new RectF();

    private float strokeWidth, bottomTextSize, textSize, arcAngle, arcBottomHeight,  progress = 0;
    private String bottomText, text, firstNumber, secondNumber;
    private int textColor, max, finishedStrokeColor, unfinishedStrokeColor;

    private final int default_finished_color = Color.WHITE;
    private final int default_unfinished_color = Color.rgb(72, 106, 176);
    private final int default_text_color = Color.rgb(66, 145, 241);
    private final float default_bottom_text_size, default_stroke_width;
    private final int default_max = 100;
    private final float default_arc_angle = 360 * 0.8f;
    private float default_text_size;
    private final int min_size;

    // Restore and save data of the progressbar.
    private static final String INSTANCE_STATE = "saved_instance";
    private static final String INSTANCE_STROKE_WIDTH = "stroke_width";
    private static final String INSTANCE_BOTTOM_TEXT_SIZE = "bottom_text_size";
    private static final String INSTANCE_BOTTOM_TEXT = "bottom_text";
    private static final String INSTANCE_TEXT_SIZE = "text_size";
    private static final String INSTANCE_TEXT_COLOR = "text_color";
    private static final String INSTANCE_PROGRESS = "progress";
    private static final String INSTANCE_MAX = "max";
    private static final String INSTANCE_FINISHED_STROKE_COLOR = "finished_stroke_color";
    private static final String INSTANCE_UNFINISHED_STROKE_COLOR = "unfinished_stroke_color";
    private static final String INSTANCE_ARC_ANGLE = "arc_angle";

    /**
     *  Constructors
     * @param context
     */
    public MyProgressBar(Context context) {
        this(context, null);
    }

    public MyProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        default_text_size = Utils.sp2px(getResources(), 30);
        min_size = (int) Utils.dp2px(getResources(), 100);
        default_bottom_text_size = Utils.sp2px(getResources(), 10);
        default_stroke_width = Utils.dp2px(getResources(), 4);

        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyProgressBar, defStyleAttr, 0);
        initByAttributes(attributes);
        attributes.recycle();

        initPainters();
    }

    /**
     * Init styled attributes
     * @param attributes array.
     */
    private void initByAttributes(TypedArray attributes) {
        finishedStrokeColor = attributes.getColor(R.styleable.MyProgressBar_finished_color, default_finished_color);
        unfinishedStrokeColor = attributes.getColor(R.styleable.MyProgressBar_unfinished_color, default_unfinished_color);
        textColor = attributes.getColor(R.styleable.MyProgressBar_text_color, default_text_color);
        textSize = attributes.getDimension(R.styleable.MyProgressBar_text_size, default_text_size);
        arcAngle = attributes.getFloat(R.styleable.MyProgressBar_angle, default_arc_angle);
        progress = attributes.getFloat(R.styleable.MyProgressBar_progress, 0);
        setMax(attributes.getInt(R.styleable.MyProgressBar_max, default_max));
        strokeWidth = attributes.getDimension(R.styleable.MyProgressBar_stroke_width, default_stroke_width);
        bottomTextSize = attributes.getDimension(R.styleable.MyProgressBar_bottom_text_size, default_bottom_text_size);
        bottomText = attributes.getString(R.styleable.MyProgressBar_bottom_text);
    }

    /**
     * Init the TextPaint and the Paint.
     */
    private void initPainters() {
        textPaint = new TextPaint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);

        paint = new Paint();
        paint.setColor(default_unfinished_color);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    public void invalidate() {
        initPainters();
        super.invalidate();
    }

    /**
     * Get the width of the arc that to be drawn.
     * @return the arc width.
     */
    public float getStrokeWidth() {
        return strokeWidth;
    }

    /**
     * Set the width of the arc that to be drawn.
     * @param strokeWidth size.
     */
    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        this.invalidate();
    }

    /**
     * Get the text that has been written at the bottom.
     * @return the bottom text.
     */
    public String getBottomText() {
        return bottomText;
    }

    /**
     * Set the text that will be written at the bottom.
     * @param bottomText to set.
     */
    public void setBottomText(String bottomText) {
        this.bottomText = bottomText;
        this.invalidate();
    }

    /**
     * Get the percent of the progress bar.
     * @return the progress number.
     */
    private float getProgress() {
        return progress;
    }

    /**
     * set the percent of the progress bar.
     * @param progress size to set.
     */
    private void setProgress(float progress) {
        this.progress = Float.valueOf(new DecimalFormat("#.##").format(progress));

        if (this.progress > getMax()) {
            this.progress %= getMax();
        }
        invalidate();
    }

    /**
     * Get progress maximum.
     * @return progress maximum number.
     */
    public int getMax() {
        return max;
    }

    /**
     * Set progress maximum.
     * @param max size.
     */
    public void setMax(int max) {
        if (max > 0) {
            this.max = max;
            invalidate();
        }
    }

    /**
     * Get progress bottom text size.
     * @return progress bottom text size.
     */
    public float getBottomTextSize() {
        return bottomTextSize;
    }

    /**
     * Set progress bottom text size.
     * @param bottomTextSize size to set.
     */
    public void setBottomTextSize(float bottomTextSize) {
        this.bottomTextSize = bottomTextSize;
        this.invalidate();
    }

    /**
     * Set the text in the progress bar and calculate the progress bar that already complete.
     * @param first number done
     * @param second number from.
     */
    public void setText(int first, int second){
        this.firstNumber = first + "";
        this.secondNumber = second + "";
        float firstNumInt = Float.parseFloat(firstNumber);
        float secondNumInt = Float.parseFloat(secondNumber);
        if(firstNumInt > secondNumInt) {
            first = second;
            firstNumInt = secondNumInt;
        }
        this.text = first +  "/" + second;
        float percentProgress = (firstNumInt / secondNumInt)*getMax();
        setProgress(percentProgress);
        this.invalidate();
    }

    /**
     * Get the text of progress bar.
     * @return the progress bar text.
     */
    public int[] getText(){
        int[] res = new int[2];
        String[] result =  this.text.split("/");
        for(int i=0; i<result.length; i++){
            res[i] = Integer.parseInt(result[i]);
        }
        return res;
    }

    /**
     * Set default text.
     */
    public void setDefaultText(){
        text = String.valueOf(getProgress());
        invalidate();
    }

    /**
     * Get progress text size.
     * @return progress text size.
     */
    public float getTextSize() {
        return textSize;
    }

    /**
     * Set progress text size.
     * @param textSize to set in the progress bar.
     */
    public void setTextSize(float textSize) {
        this.textSize = textSize;
        this.invalidate();
    }

    /**
     * Get progress text color.
     * @return progress text color.
     */
    public int getTextColor() {
        return textColor;
    }

    /**
     * Set progress text color.
     * @param textColor
     */
    public void setTextColor(int textColor) {
        this.textColor = textColor;
        this.invalidate();
    }

    /**
     * Get the color of the finish part in the arc.
     * @return the finish color.
     */
    public int getFinishedStrokeColor() {
        return finishedStrokeColor;
    }

    /**
     * Set the color of the finish part in the arc.
     * @param finishedStrokeColor color for the finish part in the arc.
     */
    public void setFinishedStrokeColor(int finishedStrokeColor) {
        this.finishedStrokeColor = finishedStrokeColor;
        this.invalidate();
    }

    /**
     * Get the color of the unfinished part in the arc.
     * @return the unfinished color.
     */
    public int getUnfinishedStrokeColor() {
        return unfinishedStrokeColor;
    }

    /**
     * Set the color of the unfinished part in the arc.
     * @param unfinishedStrokeColor unfinished part in the arc.
     */
    public void setUnfinishedStrokeColor(int unfinishedStrokeColor) {
        this.unfinishedStrokeColor = unfinishedStrokeColor;
        this.invalidate();
    }

    /**
     * Get the Angle of the arc progress bar.
     * @return the angle.
     */
    public float getArcAngle() {
        return arcAngle;
    }

    /**
     * Set the Angle of the arc progress bar.
     * @param arcAngle
     */
    public void setArcAngle(float arcAngle) {
        this.arcAngle = arcAngle;
        this.invalidate();
    }

    /**
     * Determine the size requirements for this view and all of its children.
     * @param widthMeasureSpec horizontal space requirements as imposed by the parent.
     * @param heightMeasureSpec vertical space requirements as imposed by the parent.
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        rectF.set(strokeWidth / 2f, strokeWidth / 2f, width - strokeWidth / 2f, MeasureSpec.getSize(heightMeasureSpec) - strokeWidth / 2f);
        float radius = width / 2f;
        float angle = (360 - arcAngle) / 2f;
        arcBottomHeight = radius * (float) (1 - Math.cos(angle / 180 * Math.PI));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float startAngle = 270 - arcAngle / 2f;
        float finishedSweepAngle = progress / (float) getMax() * arcAngle;
        float finishedStartAngle = startAngle;
        if(progress == 0) finishedStartAngle = 0.01f;
        paint.setColor(unfinishedStrokeColor);
        canvas.drawArc(rectF, startAngle, arcAngle, false, paint);
        paint.setColor(finishedStrokeColor);
        canvas.drawArc(rectF, finishedStartAngle, finishedSweepAngle, false, paint);

        if(text == null){
            text = String.valueOf(getProgress());
        }

        if(firstNumber == null || secondNumber == null){
            text = String.valueOf(getProgress());
        }

        if (!TextUtils.isEmpty(text)) {
            textPaint.setColor(textColor);
            textPaint.setTextSize(textSize);
            float textHeight = textPaint.descent() + textPaint.ascent();
            float textBaseline = (getHeight() - textHeight) / 2.0f;
            canvas.drawText(text, (getWidth() - textPaint.measureText(text)) / 2.0f, textBaseline, textPaint);
        }

        if(arcBottomHeight == 0) {
            float radius = getWidth() / 2f;
            float angle = (360 - arcAngle) / 2f;
            arcBottomHeight = radius * (float) (1 - Math.cos(angle / 180 * Math.PI));
        }

        if (!TextUtils.isEmpty(getBottomText())) {
            textPaint.setTextSize(bottomTextSize);
            float bottomTextBaseline = getHeight() - arcBottomHeight - (textPaint.descent() + textPaint.ascent()) / 2;
            canvas.drawText(getBottomText(), (getWidth() - textPaint.measureText(getBottomText())) / 2.0f, bottomTextBaseline, textPaint);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putFloat(INSTANCE_STROKE_WIDTH, getStrokeWidth());
        bundle.putFloat(INSTANCE_BOTTOM_TEXT_SIZE, getBottomTextSize());
        bundle.putString(INSTANCE_BOTTOM_TEXT, getBottomText());
        bundle.putFloat(INSTANCE_TEXT_SIZE, getTextSize());
        bundle.putInt(INSTANCE_TEXT_COLOR, getTextColor());
        bundle.putFloat(INSTANCE_PROGRESS, getProgress());
        bundle.putInt(INSTANCE_MAX, getMax());
        bundle.putInt(INSTANCE_FINISHED_STROKE_COLOR, getFinishedStrokeColor());
        bundle.putInt(INSTANCE_UNFINISHED_STROKE_COLOR, getUnfinishedStrokeColor());
        bundle.putFloat(INSTANCE_ARC_ANGLE, getArcAngle());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle) {
            final Bundle bundle = (Bundle) state;
            strokeWidth = bundle.getFloat(INSTANCE_STROKE_WIDTH);
            bottomTextSize = bundle.getFloat(INSTANCE_BOTTOM_TEXT_SIZE);
            bottomText = bundle.getString(INSTANCE_BOTTOM_TEXT);
            textSize = bundle.getFloat(INSTANCE_TEXT_SIZE);
            textColor = bundle.getInt(INSTANCE_TEXT_COLOR);
            setMax(bundle.getInt(INSTANCE_MAX));
            //setProgress(bundle.getFloat(INSTANCE_PROGRESS));
            finishedStrokeColor = bundle.getInt(INSTANCE_FINISHED_STROKE_COLOR);
            unfinishedStrokeColor = bundle.getInt(INSTANCE_UNFINISHED_STROKE_COLOR);
            initPainters();
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }
}
