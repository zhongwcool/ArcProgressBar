package com.woodnaonly.arcprogress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.arcprogress.R;

import java.text.DecimalFormat;

/**
 * 作者：woodnaonly on 2016/7/4 0031 10:18
 * 邮箱：497917264@qq.com
 * 内容：
 * 备注：
 */
public class ArcProgress extends View
{
    private Context mContext;
    private Paint paint;
    protected Paint textPaint;

    private RectF rectF = new RectF();

    private int decimal_digits;

    private float strokeWidth;
    private float suffixTextSize;
    private float bottomTextSize;
    private String bottomText;

    private String progressTextTop;
    private float progressTextTopSize;
    private int progressTextTopColor;

    private float progressTextBottomSize;
    private int progressTextBottomColor;
    private String progressTextBottom;

    private float progressTextSize;
    private int progressTextColor;


    private String progressSuffixText = "%";
    private float progressSuffixTextSize;
    private int progressSuffixTextColor;


    private double progress = 0;
    private double max;
    private int finishedStrokeColor;
    private int unfinishedStrokeColor;
    private float arcAngle;
    private String suffixText = "%";
    private float suffixTextPadding;
    private float arcBottomHeight;

    private final int default_finished_color = Color.WHITE;
    private final int default_unfinished_color = Color.rgb(72, 106, 176);
    private final int default_text_color = Color.rgb(66, 145, 241);
    private final float default_suffix_text_size;
    private final float default_suffix_padding;
    private final float default_bottom_text_size;
    private final float default_stroke_width;
    private final String default_suffix_text;
    private final int default_max = 100;
    private final float default_arc_angle = 360 * 0.8f;
    private float default_text_size;
    private final int min_size;

    private static final String INSTANCE_STATE = "saved_instance";
    private static final String INSTANCE_STROKE_WIDTH = "stroke_width";
    private static final String INSTANCE_SUFFIX_TEXT_SIZE = "suffix_text_size";
    private static final String INSTANCE_SUFFIX_TEXT_PADDING = "suffix_text_padding";
    private static final String INSTANCE_BOTTOM_TEXT_SIZE = "bottom_text_size";
    private static final String INSTANCE_BOTTOM_TEXT = "bottom_text";

    private static final String INSTANCE_PROGRESS_TEXT_TOP = "progress_text_top";
    private static final String INSTANCE_PROGRESS_TEXT_TOP_SIZE = "progress_text_top_size";
    private static final String INSTANCE_PROGRESS_TEXT_TOP_COLOR = "progress_text_top_color";

    private static final String INSTANCE_PROGRESS_SUFFIX_TEXT = "progress_suffix_text";
    private static final String INSTANCE_PROGRESS_SUFFIX_TEXT_SIZE = "progress_suffix_text_size";
    private static final String INSTANCE_PROGRESS_SUFFIX_TEXT_COLOR = "progress_suffix_text_color";

    private static final String INSTANCE_PROGRESS_TEXT_BOTTOM = "progress_text_bottom";
    private static final String INSTANCE_PROGRESS_TEXT_BOTTOM_SIZE = "progress_text_bottom_size";
    private static final String INSTANCE_PROGRESS_TEXT_BOTTOM_COLOR = "progress_text_bottom_color";


    private static final String INSTANCE_TEXT_SIZE = "text_size";
    private static final String INSTANCE_TEXT_COLOR = "text_color";
    private static final String INSTANCE_PROGRESS = "progress";
    private static final String INSTANCE_MAX = "max";
    private static final String INSTANCE_FINISHED_STROKE_COLOR = "finished_stroke_color";
    private static final String INSTANCE_UNFINISHED_STROKE_COLOR = "unfinished_stroke_color";
    private static final String INSTANCE_ARC_ANGLE = "arc_angle";
    private static final String INSTANCE_SUFFIX = "suffix";

    public ArcProgress(Context context)
    {
        this(context, null);
    }

    public ArcProgress(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public ArcProgress(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        mContext = context;
        default_text_size = Utils.sp2px(getResources(), 18);
        min_size = (int) Utils.dp2px(getResources(), 100);
        default_text_size = Utils.sp2px(getResources(), 40);
        default_suffix_text_size = Utils.sp2px(getResources(), 15);
        default_suffix_padding = Utils.dp2px(getResources(), 4);
        default_suffix_text = "%";
        default_bottom_text_size = Utils.sp2px(getResources(), 10);
        default_stroke_width = Utils.dp2px(getResources(), 4);
        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArcProgress, defStyleAttr, 0);
        initByAttributes(attributes);
        attributes.recycle();
        initPainters();
    }

    protected void initByAttributes(TypedArray attributes)
    {
        //画弧度的颜色
        finishedStrokeColor = attributes.getColor(R.styleable.ArcProgress_arc_finished_color, default_finished_color);
        //底色
        unfinishedStrokeColor = attributes.getColor(R.styleable.ArcProgress_arc_unfinished_color, default_unfinished_color);

        //进度条字体颜色
        progressTextColor = attributes.getColor(R.styleable.ArcProgress_arc_progress_text_color, default_text_color);
        //进度条字体大小
        progressTextSize = attributes.getDimension(R.styleable.ArcProgress_arc_progress_text_size, default_text_size);

        //进度条上面字体颜色
        progressTextTopColor = attributes.getColor(R.styleable.ArcProgress_arc_progress_text_top_color, default_text_color);
        //进度条上面字体大小
        progressTextTopSize = attributes.getDimension(R.styleable.ArcProgress_arc_progress_text_top_size, default_text_size);
        //进度条上面
        progressTextTop = attributes.getString(R.styleable.ArcProgress_arc_progress_text_top_text);


        //进度条下面
        progressTextBottom = attributes.getString(R.styleable.ArcProgress_arc_progress_text_bottom_text);
        //进度条下面字体颜色
        progressTextBottomColor = attributes.getColor(R.styleable.ArcProgress_arc_progress_text_bottom_color, default_text_color);
        //进度条下面字体大小
        progressTextBottomSize = attributes.getDimension(R.styleable.ArcProgress_arc_progress_text_bottom_size, default_text_size);


        //字体颜色
        progressTextColor = attributes.getColor(R.styleable.ArcProgress_arc_progress_text_color, default_text_color);
        //字体大小
        progressTextSize = attributes.getDimension(R.styleable.ArcProgress_arc_progress_text_size, default_text_size);

        progressSuffixText = attributes.getString(R.styleable.ArcProgress_arc_progress_suffix_text);
        if (progressSuffixText == null)
            progressSuffixText = "";
        progressSuffixTextColor = attributes.getColor(R.styleable.ArcProgress_arc_progress_suffix_text_color, default_text_color);
        progressSuffixTextSize = attributes.getDimension(R.styleable.ArcProgress_arc_progress_suffix_text_size, default_text_size);


        decimal_digits = attributes.getInt(R.styleable.ArcProgress_decimal_digits, 0);
        //弧度？
        arcAngle = attributes.getFloat(R.styleable.ArcProgress_arc_angle, default_arc_angle);
        //最大值
        setMax(attributes.getInt(R.styleable.ArcProgress_arc_max, default_max));
        //默认值
        setProgress(attributes.getInt(R.styleable.ArcProgress_arc_progress, 0));
        //宽带
        strokeWidth = attributes.getDimension(R.styleable.ArcProgress_arc_stroke_width, default_stroke_width);
        //旁边字体的宽度
        suffixTextSize = attributes.getDimension(R.styleable.ArcProgress_arc_suffix_text_size, default_suffix_text_size);
        //旁边字体的字体
        suffixText = TextUtils.isEmpty(attributes.getString(R.styleable.ArcProgress_arc_suffix_text)) ? default_suffix_text : attributes.getString(R.styleable.ArcProgress_arc_suffix_text);

        //旁边字体
        suffixTextPadding = attributes.getDimension(R.styleable.ArcProgress_arc_suffix_text_padding, default_suffix_padding);
        //底部文字大小
        bottomTextSize = attributes.getDimension(R.styleable.ArcProgress_arc_bottom_text_size, default_bottom_text_size);
        //底部文字
        bottomText = attributes.getString(R.styleable.ArcProgress_arc_bottom_text);
    }

    protected void initPainters()
    {
        textPaint = new TextPaint();
        textPaint.setColor(progressTextColor);
        textPaint.setTextSize(progressTextSize);
        textPaint.setAntiAlias(true);

        paint = new Paint();
//        paint.setColor(default_unfinished_color);
//        paint.setColor(Color.rgb(200, 200, 200));
        paint.setColor(Color.rgb(10, 20, 220));
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    public void invalidate()
    {
        initPainters();
        super.invalidate();
    }

    public float getStrokeWidth()
    {
        return strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth)
    {
        this.strokeWidth = strokeWidth;
        this.invalidate();
    }

    public float getSuffixTextSize()
    {
        return suffixTextSize;
    }

    public void setSuffixTextSize(float suffixTextSize)
    {
        this.suffixTextSize = suffixTextSize;
        this.invalidate();
    }

    public String getBottomText()
    {
        return bottomText;
    }

    public void setBottomText(String bottomText)
    {
        this.bottomText = bottomText;
        this.invalidate();
    }

    public double getProgress()
    {
        return progress;
    }

    public void setProgress(double progress)
    {
        this.progress = progress;
//        if (progress == 0)
//            this.progress = 2;
//        Log.d("=====1", getMax() + "");
        if (this.progress > getMax())
        {
            this.progress %= getMax();
        }
        invalidate();
    }

    public double getMax()
    {
        return max;
    }

    public void setMax(double max)
    {
        if (max > 0)
        {
            this.max = max;
            invalidate();
        }
    }

    public String getProgressTextTop()
    {
        return progressTextTop;
    }

    public void setProgressTextTop(String progressTextTop)
    {
        this.progressTextTop = progressTextTop;
        this.invalidate();
    }

    public String getProgressTextBottom()
    {
        return progressTextBottom;
    }

    public void setProgressTextBottom(String progressTextBottom)
    {
        this.progressTextBottom = progressTextBottom;
        this.invalidate();
    }

    public float getBottomTextSize()
    {
        return bottomTextSize;
    }

    public void setBottomTextSize(float bottomTextSize)
    {
        this.bottomTextSize = bottomTextSize;
        this.invalidate();
    }

    public float getProgressTextSize()
    {
        return progressTextSize;
    }

    public void setProgressTextSize(float progressTextSize)
    {
        this.progressTextSize = progressTextSize;
        this.invalidate();
    }

    public int getProgressTextColor()
    {
        return progressTextColor;
    }

    public void setProgressTextColor(int progressTextColor)
    {
        this.progressTextColor = progressTextColor;
        this.invalidate();
    }

    public int getFinishedStrokeColor()
    {
        return finishedStrokeColor;
    }

    public void setFinishedStrokeColor(int finishedStrokeColor)
    {
        this.finishedStrokeColor = finishedStrokeColor;
        this.invalidate();
    }

    public int getUnfinishedStrokeColor()
    {
        return unfinishedStrokeColor;
    }

    public void setUnfinishedStrokeColor(int unfinishedStrokeColor)
    {
        this.unfinishedStrokeColor = unfinishedStrokeColor;
        this.invalidate();
    }

    public float getArcAngle()
    {
        return arcAngle;
    }

    public void setArcAngle(float arcAngle)
    {
        this.arcAngle = arcAngle;
        this.invalidate();
    }

    public String getSuffixText()
    {
        return suffixText;
    }

    public void setSuffixText(String suffixText)
    {
        this.suffixText = suffixText;
        this.invalidate();
    }

    public float getSuffixTextPadding()
    {
        return suffixTextPadding;
    }

    public void setSuffixTextPadding(float suffixTextPadding)
    {
        this.suffixTextPadding = suffixTextPadding;
        this.invalidate();
    }

    @Override
    protected int getSuggestedMinimumHeight()
    {
        return min_size;
    }

    @Override
    protected int getSuggestedMinimumWidth()
    {
        return min_size;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
//        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        rectF.set(strokeWidth / 2f, strokeWidth / 2f, width - strokeWidth / 2f, MeasureSpec.getSize(heightMeasureSpec) - strokeWidth / 2f);
        float radius = width / 2f;
        float angle = (360 - arcAngle) / 2f;
        arcBottomHeight = radius * (float) (1 - Math.cos(angle / 180 * Math.PI));
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        float startAngle = 270 - arcAngle / 2f;
        double finishedSweepAngle = progress / (float) getMax() * arcAngle;
        float finishedStartAngle = startAngle;
        paint.setColor(unfinishedStrokeColor);
//        paint.setColor(Color.parseColor("#AAAAAAAA"));

        canvas.drawArc(rectF, startAngle, arcAngle, false, paint);
        paint.setColor(finishedStrokeColor);

//        paint.setColor(finishedStrokeColor);

        Log.d("====3", getMax() + "," + startAngle);
        if (getMax() == 0)
        {

        }

        if (getMax() != 0)
        {
            Log.d("====4", getMax() + "," + finishedStartAngle + "," + finishedSweepAngle);
            if (finishedSweepAngle != 0)
                canvas.drawArc(rectF, finishedStartAngle, (float) finishedSweepAngle, false, paint);
        }
        Log.d("====4", getProgress() + "");
//        if (decimal_digits == 0)
//            String text = Math.round(getProgress()) + progressSuffixText;
//        else
        String text = null;
        if ((decimal_digits) == 0)
            text = Math.round(getProgress()) + progressSuffixText;
        else
        {
            DecimalFormat decimalFormat = new DecimalFormat("0.00");//格式化设置
            text = decimalFormat.format(getProgress()) + progressSuffixText;
        }
        if (!TextUtils.isEmpty(text))
        {
            textPaint.setColor(progressTextColor);
            textPaint.setTextSize(progressTextSize);
            float textHeight = textPaint.descent() + textPaint.ascent();
            float textBaseline = (getHeight() - textHeight) / 2.0f;
            Typeface fontFace = Typeface.createFromAsset(mContext.getAssets(),
                    "fonts/t1.otf");
            textPaint.setTypeface(fontFace);
            if (isText)
            {
                canvas.drawText(mText, (getWidth() - textPaint.measureText(text)) / 2.0f, textBaseline, textPaint);
            } else
            {
                canvas.drawText(text, (getWidth() - textPaint.measureText(text)) / 2.0f, textBaseline, textPaint);
            }


            if (!TextUtils.isEmpty(getProgressTextTop()))
            {
                textPaint.setTextSize(progressTextTopSize);
                textPaint.setColor(progressTextTopColor);
                canvas.drawText(getProgressTextTop(), (getWidth() - textPaint.measureText(getProgressTextTop())) / 2.0f, textBaseline + textHeight - Utils.dp2px(getResources(), 20), textPaint);
            }
            if (!TextUtils.isEmpty(getProgressTextBottom()))
            {
                textPaint.setTextSize(progressTextBottomSize);
                textPaint.setColor(progressTextBottomColor);
                canvas.drawText(getProgressTextBottom(), (getWidth() - textPaint.measureText(getProgressTextBottom())) / 2.0f, textBaseline - textHeight / 2 + Utils.dp2px(getResources(), 5), textPaint);

            }

//            textPaint.setTextSize(suffixTextSize);
//            float suffixHeight = textPaint.descent() + textPaint.ascent();
//            canvas.drawText(suffixText, getWidth() / 2.0f + textPaint.measureText(text) + suffixTextPadding, textBaseline + textHeight - suffixHeight, textPaint);
        }

        if (!TextUtils.isEmpty(getBottomText()))
        {
            textPaint.setTextSize(bottomTextSize);

            float bottomTextBaseline = getHeight() - arcBottomHeight - (textPaint.descent() + textPaint.ascent()) / 2;
            float textHeight = textPaint.descent() + textPaint.ascent();
            float textBaseline = (getHeight() - textHeight) / 2.0f;
            canvas.drawText(getBottomText(), (getWidth() - textPaint.measureText(getBottomText())) / 2.0f, bottomTextBaseline, textPaint);
        }
    }

    public void setText(String s)
    {
        mText = s;
        invalidate();
    }

    public String mText = "0.00";
    public boolean isText = false;
//    private String progressSuffixText = "%";
//    private float progressSuffixTextSize;
//    private int progressSuffixTextColor;

    //    private static final String INSTANCE_PROGRESS_SUFFIX_TEXT = "progress__suffix_text";
//    private static final String INSTANCE_PROGRESS_SUFFIX_TEXT_SIZE = "progress_suffix_text_size";
//    private static final String INSTANCE_PROGRESS_SUFFIX_TEXT_COLOR = "progress_suffix_text_color";
    @Override
    protected Parcelable onSaveInstanceState()
    {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putFloat(INSTANCE_STROKE_WIDTH, getStrokeWidth());
        bundle.putFloat(INSTANCE_SUFFIX_TEXT_SIZE, getSuffixTextSize());
        bundle.putFloat(INSTANCE_SUFFIX_TEXT_PADDING, getSuffixTextPadding());
        bundle.putFloat(INSTANCE_BOTTOM_TEXT_SIZE, getBottomTextSize());
        bundle.putString(INSTANCE_BOTTOM_TEXT, getBottomText());

        bundle.putString(INSTANCE_PROGRESS_TEXT_TOP, getProgressTextTop());
        bundle.putInt(INSTANCE_PROGRESS_TEXT_TOP_COLOR, progressTextTopColor);
        bundle.putFloat(INSTANCE_PROGRESS_TEXT_TOP_SIZE, progressTextTopSize);


        bundle.putString(INSTANCE_PROGRESS_SUFFIX_TEXT, progressSuffixText);
        bundle.putInt(INSTANCE_PROGRESS_SUFFIX_TEXT_COLOR, progressSuffixTextColor);
        bundle.putFloat(INSTANCE_PROGRESS_SUFFIX_TEXT_SIZE, progressSuffixTextSize);

        bundle.putString(INSTANCE_PROGRESS_TEXT_BOTTOM, getProgressTextBottom());
        bundle.putInt(INSTANCE_PROGRESS_TEXT_BOTTOM_COLOR, progressTextBottomColor);
        bundle.putFloat(INSTANCE_PROGRESS_TEXT_BOTTOM_SIZE, progressTextBottomSize);


        bundle.putFloat(INSTANCE_TEXT_SIZE, getProgressTextSize());
        bundle.putInt(INSTANCE_TEXT_COLOR, getProgressTextColor());
        bundle.putDouble(INSTANCE_PROGRESS, getProgress());
        bundle.putDouble(INSTANCE_MAX, getMax());
        bundle.putInt(INSTANCE_FINISHED_STROKE_COLOR, getFinishedStrokeColor());
        bundle.putInt(INSTANCE_UNFINISHED_STROKE_COLOR, getUnfinishedStrokeColor());
        bundle.putFloat(INSTANCE_ARC_ANGLE, getArcAngle());
        bundle.putString(INSTANCE_SUFFIX, getSuffixText());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state)
    {
        if (state instanceof Bundle)
        {
            final Bundle bundle = (Bundle) state;
            strokeWidth = bundle.getFloat(INSTANCE_STROKE_WIDTH);
            suffixTextSize = bundle.getFloat(INSTANCE_SUFFIX_TEXT_SIZE);
            suffixTextPadding = bundle.getFloat(INSTANCE_SUFFIX_TEXT_PADDING);
            bottomTextSize = bundle.getFloat(INSTANCE_BOTTOM_TEXT_SIZE);
            bottomText = bundle.getString(INSTANCE_BOTTOM_TEXT);


            progressTextSize = bundle.getFloat(INSTANCE_TEXT_SIZE);
            progressTextColor = bundle.getInt(INSTANCE_TEXT_COLOR);

            progressTextTop = bundle.getString(INSTANCE_PROGRESS_TEXT_TOP);
            progressTextTopColor = bundle.getInt(INSTANCE_PROGRESS_TEXT_TOP_COLOR);
            progressTextTopSize = bundle.getFloat(INSTANCE_PROGRESS_TEXT_TOP_SIZE);

            //
            progressSuffixText = bundle.getString(INSTANCE_PROGRESS_SUFFIX_TEXT);
            progressSuffixTextColor = bundle.getInt(INSTANCE_PROGRESS_SUFFIX_TEXT_COLOR);
            progressSuffixTextSize = bundle.getFloat(INSTANCE_PROGRESS_SUFFIX_TEXT_SIZE);


            progressTextBottom = bundle.getString(INSTANCE_PROGRESS_TEXT_BOTTOM);
            progressTextBottomColor = bundle.getInt(INSTANCE_PROGRESS_TEXT_BOTTOM_COLOR);
            progressTextBottomSize = bundle.getFloat(INSTANCE_PROGRESS_TEXT_BOTTOM_SIZE);


            setMax(bundle.getInt(INSTANCE_MAX));
            setProgress(bundle.getInt(INSTANCE_PROGRESS));
            finishedStrokeColor = bundle.getInt(INSTANCE_FINISHED_STROKE_COLOR);
            unfinishedStrokeColor = bundle.getInt(INSTANCE_UNFINISHED_STROKE_COLOR);
            suffixText = bundle.getString(INSTANCE_SUFFIX);
            initPainters();
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }
}