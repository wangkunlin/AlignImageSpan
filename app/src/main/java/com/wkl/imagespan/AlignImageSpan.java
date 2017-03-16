package com.wkl.imagespan;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.text.style.ImageSpan;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

/**
 * Created by wangkunlin
 * On 2017-03-15
 */

public class AlignImageSpan extends ImageSpan {

    /**
     * 顶部对齐
     */
    public static final int ALIGN_TOP = 3;
    /**
     * 垂直居中
     */
    public static final int ALIGN_CENTER = 4;

    @IntDef({ALIGN_BOTTOM, ALIGN_BASELINE, ALIGN_TOP, ALIGN_CENTER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Aligment {
    }

    public AlignImageSpan(Drawable d) {
        this(d, ALIGN_CENTER);
    }

    public AlignImageSpan(Drawable d, @Aligment int verticalAlignment) {
        super(d, verticalAlignment);
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        Drawable d = getCachedDrawable();
        Rect rect = d.getBounds();
        if (fm != null) {
            Paint.FontMetrics fmPaint = paint.getFontMetrics();
            // 顶部 leading
            float topLeading = fmPaint.top - fmPaint.ascent;
            // 底部 leading
            float bottomLeading = fmPaint.bottom - fmPaint.descent;
            // drawable 的高度
            int drHeight = rect.height();

            switch (mVerticalAlignment) {
                case ALIGN_CENTER: { // drawable 的中间与 行中间对齐
                    // 当前行 的高度
                    float fontHeight = fmPaint.descent - fmPaint.ascent;
                    // 整行的 y方向上的中间 y 坐标
                    float center = fmPaint.descent - fontHeight / 2;

                    // 算出 ascent 和 descent
                    float ascent = center - drHeight / 2;
                    float descent = center + drHeight / 2;

                    fm.ascent = (int) ascent;
                    fm.top = (int) (ascent + topLeading);
                    fm.descent = (int) descent;
                    fm.bottom = (int) (descent + bottomLeading);
                    break;
                }
                case ALIGN_BASELINE: { // drawable 的底部与 baseline 对齐
                    // 所以 ascent 的值就是 负的 drawable 的高度
                    float ascent = -drHeight;
                    fm.ascent = -drHeight;
                    fm.top = (int) (ascent + topLeading);
                    break;
                }
                case ALIGN_TOP: { // drawable 的顶部与 行的顶部 对齐
                    // 算出 descent
                    float descent = drHeight + fmPaint.ascent;
                    fm.descent = (int) descent;
                    fm.bottom = (int) (descent + bottomLeading);
                    break;
                }
                case ALIGN_BOTTOM: // drawable 的底部与 行的底部 对齐
                default: {
                    // 算出 ascent
                    float ascent = fmPaint.descent - drHeight;
                    fm.ascent = (int) ascent;
                    fm.top = (int) (ascent + topLeading);
                }
            }
        }
        return rect.right;
    }

    /**
     * 这里的 x, y, top 以及 bottom 都是基于整个 TextView 的坐标系的坐标
     *
     * @param x      drawable 绘制的起始 x 坐标
     * @param top    当前行最高处，在 TextView 中的 y 坐标
     * @param y      当前行的 BaseLine 在 TextView 中的 y 坐标
     * @param bottom 当前行最低处，在 TextView 中的 y 坐标
     */
    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        Drawable drawable = getDrawable();
        Rect rect = drawable.getBounds();
        float transY;
        switch (mVerticalAlignment) {
            case ALIGN_BASELINE:
                transY = y - rect.height();
                break;
            case ALIGN_CENTER:
                transY = ((bottom - top) - rect.height()) / 2 + top;
                break;
            case ALIGN_TOP:
                transY = top;
                break;
            case ALIGN_BOTTOM:
            default:
                transY = bottom - rect.height();
        }
        canvas.save();
        // 这里如果不移动画布，drawable 就会在 Textview 的左上角出现
        canvas.translate(x, transY);
        drawable.draw(canvas);
        canvas.restore();
    }

    private Drawable getCachedDrawable() {
        WeakReference<Drawable> wr = mDrawableRef;
        Drawable d = null;

        if (wr != null)
            d = wr.get();

        if (d == null) {
            d = getDrawable();
            mDrawableRef = new WeakReference<>(d);
        }

        return d;
    }

    private WeakReference<Drawable> mDrawableRef;
}
