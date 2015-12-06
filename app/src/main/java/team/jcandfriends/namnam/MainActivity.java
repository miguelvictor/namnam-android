package team.jcandfriends.namnam;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeLoginText((TextView) findViewById(R.id.tv_login_text));
    }

    private void initializeLoginText(TextView textView) {
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(0xFF6151F9);

        ClickableSpan span = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };

        SpannableString ss = new SpannableString("Do you have account int Namnam? Login");
        ss.setSpan(span, 32, 37, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(colorSpan, 32, 37, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(Color.TRANSPARENT);
    }

    public void onRegister (View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

}
