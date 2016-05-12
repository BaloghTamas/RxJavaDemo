package hu.bme.aut.amorg.education.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {


    private TextView textView;
    private Button button;
    private Subscription subscription;
    private ObservableStore observableStore;
    private SeekBar seekBar;
    private Observable<Integer> observableSeekbar;
    private Observable<String> stringObservable;
    private Subscription subscription2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.demoTextView);
        button = (Button) findViewById(R.id.startButton);
        seekBar = (SeekBar) findViewById(R.id.seekBar);

        stringObservable = ObservableStore.getBetterStringObservable();
        observableSeekbar = ObservableStore.createObservableFromSeekBar(seekBar);

        button.setOnClickListener(v -> {
            startStringObservable();
            startTimeObservable();
        });
    }

    private void startStringObservable() {
        subscription = Observable.combineLatest(stringObservable, observableSeekbar, (s, integer) -> new TextWithColor(s, integer))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::onStringWithColorReceived, this::onError, this::onFinished);
    }


    private void startTimeObservable() {
        subscription2 = ObservableStore.getTimeFromServerObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::onTimeReceived, this::onError);
    }

    private void onTimeReceived(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onDestroy() {
        if (subscription != null) {
            subscription.unsubscribe();
        }

        if (subscription2 != null) {
            subscription2.unsubscribe();
        }
        super.onDestroy();
    }

    private void onFinished() {

    }

    private void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    private void onStringWithColorReceived(TextWithColor textWithColor) {
        textView.setTextColor(textWithColor.getColor());
        textView.setText(textWithColor.getText());

    }
}
