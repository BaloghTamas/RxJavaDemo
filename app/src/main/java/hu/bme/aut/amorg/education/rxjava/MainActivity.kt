package hu.bme.aut.amorg.education.rxjava

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import hu.bme.aut.amorg.education.rxjava.model.TextWithColor
import hu.bme.aut.amorg.education.rxjava.observables.ObservableFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var stringDisposable: Disposable? = null
    private var networkDisposable: Disposable? = null

    private lateinit var stringObservable: Observable<String>
    private lateinit var seekBarObservable: Observable<Int>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startButton!!.setOnClickListener {
            startStringObservable()
            startTimeObservable()
        }

    }

    private fun startStringObservable() {
        stringObservable = ObservableFactory.buildStringObservable()
        seekBarObservable = ObservableFactory.buildObservableFromSeekBar(seekBar)

        stringDisposable = Observable.combineLatest(
                stringObservable,
                seekBarObservable,
                BiFunction<String, Int, TextWithColor> { text, integer ->
                    TextWithColor(text, integer)
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        { textWithColor: TextWithColor -> this.onStringWithColorReceived(textWithColor) },
                        { error: Throwable -> this.onError(error) },
                        { this.onFinished() }
                )
    }


    private fun startTimeObservable() {
        networkDisposable = ObservableFactory.buildNetworkObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        { this.onTimeReceived(it) },
                        { this.onError(it) }
                )
    }

    private fun onTimeReceived(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }


    override fun onDestroy() {
        if (stringDisposable != null) {
            stringDisposable!!.dispose()
        }

        if (networkDisposable != null) {
            networkDisposable!!.dispose()
        }
        super.onDestroy()
    }

    private fun onFinished() {

    }

    private fun onError(throwable: Throwable) {
        throwable.printStackTrace()
    }

    private fun onStringWithColorReceived(textWithColor: TextWithColor) {
        demoTextView.setTextColor(textWithColor.color)
        demoTextView.text = textWithColor.text

    }
}
