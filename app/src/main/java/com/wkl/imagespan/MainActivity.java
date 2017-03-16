package com.wkl.imagespan;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt = (TextView) findViewById(R.id.txt);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img(AlignImageSpan.ALIGN_TOP);
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img(AlignImageSpan.ALIGN_CENTER);
            }
        });
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img(AlignImageSpan.ALIGN_BASELINE);
            }
        });
        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img(AlignImageSpan.ALIGN_BOTTOM);
            }
        });
    }

    private void img(int align) {
        SpannableString ss = new SpannableString("文字holder文字");
        Drawable d = getDrawable(R.drawable.my_pic);
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        ImageSpan span = new AlignImageSpan(d, align);
        ss.setSpan(span, 2, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txt.setText("上一行文字\n");
        txt.append(ss);
        txt.append("\n下一行文字");
    }
}
