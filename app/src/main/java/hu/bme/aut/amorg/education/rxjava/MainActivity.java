package hu.bme.aut.amorg.education.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Button button;
    private Disposable disposable;
    private SeekBar seekBar;
    private Observable<Integer> observableSeekBar;
    private Observable<String> stringObservable;
    private Disposable disposable2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.demoTextView);
        button = (Button) findViewById(R.id.startButton);
        seekBar = (SeekBar) findViewById(R.id.seekBar);

        stringObservable = ObservableStore.getBetterStringObservable();
        observableSeekBar = ObservableStore.createObservableFromSeekBar(seekBar);

        button.setOnClickListener(v -> {
            startStringObservable();
            startTimeObservable();
        });
    }

    private void startStringObservable() {
        disposable = Observable.combineLatest(stringObservable, observableSeekBar, (s, integer) -> new TextWithColor(s, integer))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::onStringWithColorReceived, this::onError, this::onFinished);
    }


    private void startTimeObservable() {
        disposable2 = ObservableStore.getTimeFromServerObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::onTimeReceived, this::onError);
    }

    private void onTimeReceived(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onDestroy() {
        if (disposable != null) {
            disposable.dispose();
        }

        if (disposable2 != null) {
            disposable2.dispose();
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
