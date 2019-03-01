package hu.bme.aut.amorg.education.rxjava.observables


import android.graphics.Color
import android.widget.SeekBar
import hu.bme.aut.amorg.education.rxjava.network.TimeApi
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

object ObservableFactory {

    fun buildStringObservable(): Observable<String> = Observable.defer {
        Observable.interval(2, TimeUnit.SECONDS, Schedulers.computation()).map<String> { ms ->
            return@map when (ms.toInt() % 6) {
                0 -> "KitKat"
                1 -> "Lollipop"
                2 -> "Marshmallow"
                3 -> "Nougat"
                4 -> "Oreo"
                5 -> "Pie"
                else -> ""
            }
        }
    }

    fun buildNetworkObservable(): Single<String> = Single.defer { Single.just(TimeApi.fetchCurrentTime()) }

    fun buildObservableFromSeekBar(seekBar: SeekBar): Observable<Int> {
        return Observable.create { subscriber ->

            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    val color = Color.HSVToColor(floatArrayOf(progress.toFloat() / 100.0f * 360.0f, 0.6f, 0.6f))
                    subscriber.onNext(color)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}

                override fun onStopTrackingTouch(seekBar: SeekBar) {}
            })

        }

    }
}
