package com.example.shraddhazingade.firstaiapiapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import ai.api.AIConfiguration;
import ai.api.AIListener;
import ai.api.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.JsonElement;
import java.util.Map;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements AIListener {

    private Button listenButton;
    private TextView resultTextView;
    private AIService aiService;
    private AIListener listener;
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listenButton = (Button) findViewById(R.id.listenButton);
        resultTextView = (TextView) findViewById(R.id.resultTextView);

        final AIConfiguration config = new AIConfiguration("6863b1e035514720ba21f579eaef5be4",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        aiService = AIService.getService(this, config);
        aiService.setListener(this);

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }



    @Override
    public void onStart() {
        super.onStart();
        client.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        client.disconnect();
    }

    public void listenButtonOnClick ( final View view){
        Log.d("","Entered listenButtonOnClick");
        aiService.startListening();
    }

    public void onResult(final AIResponse response) {
        Log.d("","Entered onResult");
        Result result = response.getResult();

        String parameterString="";
        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
            for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                parameterString += entry.getValue();
            }
        }

        resultTextView.setText(parameterString);
    }

    @Override
    public void onError(final AIError error) {
        resultTextView.setText(error.toString());
    }


    @Override
    public void onListeningStarted() {
        if (listener != null) {
            listener.onListeningStarted();
        }
    }

    @Override
    public void onListeningCanceled() {}

    @Override
    public void onListeningFinished() {
//         if (listener != null) {
//        listener.onListeningFinished();
//        }
    }

    @Override
    public void onAudioLevel(final float level) {
        if (listener != null) {
            listener.onAudioLevel(level);
        }
    }
}
