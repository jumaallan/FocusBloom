import com.joelkanyi.focusbloom.FocusBloomApp
import com.joelkanyi.focusbloom.di.KoinInit
import org.jetbrains.skiko.wasm.onWasmReady

fun main() {
    KoinInit().init()
    onWasmReady {
        BrowserViewportWindow("Focus Bloom") {
            FocusBloomApp()
        }
    }
}
