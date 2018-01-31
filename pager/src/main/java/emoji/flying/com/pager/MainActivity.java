package emoji.flying.com.pager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    TextView mAnimationView;
    TextView mCancelTv;
    TextView mStartTv;
    TranslateAnimation translateAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        bindListener();
        startAnimation();
    }

    private void initViews() {
        mAnimationView = findViewById(R.id.animation_view);
        mCancelTv = findViewById(R.id.cancel);
        mStartTv = findViewById(R.id.start);
    }

    private void startAnimation() {
        translateAnimation = new TranslateAnimation(0,0,1000,0);
        translateAnimation.setDuration(5000);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.e(TAG,"onAnimationStart");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.e(TAG,"onAnimationEnd: "+Log.getStackTraceString(new Throwable()));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.e(TAG,"onAnimationRepeat");
            }
        });
        mAnimationView.setAnimation(translateAnimation);
        translateAnimation.start();
    }

    private void bindListener() {
        mCancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translateAnimation.cancel();
            }
        });

        mStartTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAnimation();
            }
        });
    }
}
