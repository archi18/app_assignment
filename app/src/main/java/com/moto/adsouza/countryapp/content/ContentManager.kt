package com.moto.adsouza.countryapp.content

import android.content.Context
import android.os.Handler
import com.moto.adsouza.countryapp.R
import java.util.concurrent.atomic.AtomicBoolean

class ContentManager constructor(context: Context){
    private val COUNTRY_LIST: MutableList<Country> = ArrayList();
    private val COUNTRY_MAP: MutableMap<String, Country> = HashMap()
    private val COUNTRY_CACHE: MutableMap<String, CountryDetails> = HashMap()
    init {
        context.resources.getStringArray(R.array.countries_array).withIndex().forEach() {
            val country = Country(it.index.toString(), it.value)
            COUNTRY_LIST.add(country)
            COUNTRY_MAP.put(country.id, country)
        }
        INSTANCE = this
    }

    fun getCountryList(): List<Country> {
        return ArrayList<Country>(COUNTRY_LIST)
    }

    fun getCountryDetails(callBack: Handler, id: String) {
        if (COUNTRY_CACHE.containsKey(id)) {

        } else {
            // start background task. Once complete call background task
        }
    }

    companion object {
        private var INSTANCE: ContentManager? = null
        @JvmStatic
        fun getCountry(id: String): Country? {
            return INSTANCE?.COUNTRY_MAP?.get(id)
        }
    }

    data class Country(val id: String, val name: String)
    data class CountryDetails(val country: Country, val capital: String, val population: String, val area: String,
                              val region: String, val subRegion: String)

}