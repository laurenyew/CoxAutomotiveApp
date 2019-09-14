package laurenyew.coxautomotiveapp.networking.commands

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import laurenyew.coxautomotiveapp.networking.api.CoxAutomotiveApiBuilder
import laurenyew.coxautomotiveapp.networking.api.CoxAutomotiveApi
import kotlin.coroutines.CoroutineContext

/**
 * This command makes the network call with the API
 */
open class BaseNetworkCommand : CoroutineScope {
    private var job = Job()
    var api: CoxAutomotiveApi? = CoxAutomotiveApiBuilder.apiBuilder(CoxAutomotiveApi::class.java)

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    fun finish() {
        job.cancel()
    }
}