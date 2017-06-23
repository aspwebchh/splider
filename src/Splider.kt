package splider

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit



fun main(args:Array<String>) {
    val fangshijie = Fangshijie()
    //val yinwang = YinWang()
    val runnable = Runnable {
        fangshijie.handle()
        //yinwang.handle()
    }
    val service = Executors.newSingleThreadScheduledExecutor()
    service.scheduleAtFixedRate(runnable, 1, 3600, TimeUnit.SECONDS)
}