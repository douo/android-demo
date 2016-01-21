package info.dourok.android.demo.bug;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;

import java.lang.reflect.Field;

import info.dourok.android.demo.R;

/**
 * Created by John on 2016/1/20.
 */
public class CustomButton extends Button {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) {
            try {
                Class clazz = Class.forName("com.android.internal.R$styleable");
                Field field = clazz.getDeclaredField("View");

                int[] View = (int[]) field.get(clazz);
                int View_background = clazz.getDeclaredField("View_background").getInt(clazz);
                TypedArray a = context.obtainStyledAttributes(
                        attrs, View, 0, 0);
                if (a.peekValue(View_background) != null)
                    System.out.println(a.peekValue(View_background).coerceToString());
                a.recycle();

                int[] cbnAttrs = new int[]{R.attr.colorButtonNormal, R.attr.colorPrimary, R.attr.colorPrimaryDark};

                a = context.obtainStyledAttributes(cbnAttrs);
                for (int i = 0; i < cbnAttrs.length; i++) {
                    int color = a.getColor(i, 0x0);
                    System.out.println(String.format("0x%x", color));
                }
                a.recycle();

//            for (Field f : clazz.getDeclaredFields()) {
//                System.out.println(f.getName());
//            }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public CustomButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    @Override
    public void setBackground(Drawable background) {
        super.setBackground(background);
    }
}
