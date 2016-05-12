package hu.bme.aut.amorg.education.rxjava;


import android.graphics.Color;
import android.widget.SeekBar;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Single;

public class ObservableStore {


    public static Observable<String> getBetterStringObservable() {
        return Observable.defer(() -> Observable.timer(0, 2, TimeUnit.SECONDS).map(aLong -> {
            int ms = aLong.intValue();
            switch (ms % 4) {
                case 0:
                    return "Alma";
                case 1:
                    return "Körte";
                case 2:
                    return "Szilva";
                case 3:
                    return "Barack";
            }
            return null;
        }));
    }


    public static Observable<Integer> createObservableFromSeekBar(SeekBar seekBar) {
        return Observable.create(subscriber -> {
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
            });

        });

    }


    public static Single<String> getTimeFromServerObservable() {
        return Single.defer(() -> Single.just(Network.getTimeFromApi()));
    }
}
