package hu.bme.aut.amorg.education.rxjava;


import android.graphics.Color;
import android.widget.SeekBar;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class ObservableStore {


    public static Observable<String> getBetterStringObservable() {
        return Observable.defer(() -> Observable.interval(2, TimeUnit.SECONDS, Schedulers.computation()).map(aLong -> {
            int ms = aLong.intValue();
            switch (ms % 6) {
                case 0:
                    return "KitKat";
                case 1:
                    return "Lollipop";
                case 2:
                    return "Marshmallow";
                case 3:
                    return "Nougat";
                case 4:
                    return "Oreo";
                case 5:
                    return "P...";
            }
            return null;
        }));
    }


    public static Observable<Integer> createObservableFromSeekBar(SeekBar seekBar) {
        return Observable.create(subscriber -> seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int color = Color.HSVToColor(new float[]{((float) progress) / 100.0f * 360.0f, 0.6f, 0.6f});
                subscriber.onNext(color);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        }));

    }


    public static Single<String> getTimeFromServerObservable() {
        return Single.defer(() -> Single.just(Network.getTimeFromApi()));
    }
}
