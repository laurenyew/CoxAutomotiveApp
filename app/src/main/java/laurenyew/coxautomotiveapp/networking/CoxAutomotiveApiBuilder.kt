package laurenyew.coxautomotiveapp.networking

import androidx.annotation.VisibleForTesting
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*

/**
 * @author Lauren Yew on 04/29/2018.
 *
 * Api Builder to keep the retrofit creation logic separate from the commands
 *
 * Note: Currently trusting all certs b/c was getting "CertificateRevokedException: Certificate has been revoked, reason: UNSPECIFIED"
 * b/c flickr certificates kept expiring for Android. Wouldn't put something like this in production,
 * but for a test-app, leaving for now.
 * Resource: https://futurestud.io/tutorials/retrofit-2-how-to-trust-unsafe-ssl-certificates-self-signed-expired
 */
object CoxAutomotiveApiBuilder {
    private const val BASE_URL = "http://api.coxauto-interview.com/api"
    private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val okHttpClient: OkHttpClient.Builder
    private val retrofit: Retrofit

    init {
        okHttpClient = setupOkHttp()
        retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient.build()).build()
    }

    fun <T> apiBuilder(apiClazz: Class<T>): T? = retrofit.create(apiClazz)

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun setupOkHttp(): OkHttpClient.Builder {
        //Setup HttpClient
        val httpClientBuilder = OkHttpClient.Builder()

        //Add headers
        httpClientBuilder.addInterceptor {
            val request = it.request()
                .newBuilder()
                .addHeader("Accept-Language", Locale.getDefault().language)
                .build()
            it.proceed(request)
        }
        return httpClientBuilder
    }
}