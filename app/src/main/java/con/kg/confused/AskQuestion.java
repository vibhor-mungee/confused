package con.kg.confused;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;


public class AskQuestion extends Activity implements View.OnClickListener {

    private final int REQ_CODE_SPEECH_INPUT = 100;
    private ImageView mic_iv;
    private EditText input_et;
    private TextView output_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);
        mic_iv = (ImageView) findViewById(R.id.mic_iv);
        input_et = (EditText) findViewById(R.id.input_et);
        output_et = (TextView) findViewById(R.id.output_et);
        input_et.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (input_et.getRight() - input_et.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        input_et.setVisibility(View.GONE);
                        mic_iv.setVisibility(View.GONE);
                        InputMethodManager imm = (InputMethodManager) getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        String s = input_et.getText().toString();
                        output_et.setText(s);
                        output_et.setVisibility(View.VISIBLE);
                        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.movetotop);
                        anim.setStartTime(2000);
                        output_et.startAnimation(anim);
                        return true;
                    }
                }
                return false;
            }
        });

        mic_iv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        promptSpeechInput();
    }

    private void promptSpeechInput() {
        Intent in = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        in.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        in.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        in.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Say Something!");
        try {
            startActivityForResult(in, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException e) {
            Log.e(getString(R.string.app_name), "Speech not supported");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    input_et.setText(result.get(0));

                }
                break;
            }
        }

    }
}
