package com.moto.adsouza.countryapp.content

import android.content.Context
import android.os.Handler
import com.moto.adsouza.countryapp.R
import java.util.concurrent.atomic.AtomicBoolean

class ContentManager constructor(context: Context){
    private val COUNTRY_LIST: MutableList<Country> = ArrayList();
    private val COUNTRY_MAP: MutableMap<String, Country> = HashMap()
    private val COUNTRY_CACHE: MutableMap<String, CountryDetails> = HashMap()
    private val EDUCATION_LIST: MutableList<Education> = ArrayList();
    private val EXPERIENCE_LIST: MutableList<Experience> = ArrayList();
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

    private fun genEducationList()  {
        EDUCATION_LIST.add(Education("1", "Illinois Institute of Technology", "Masters",
                "Computer Science","2016-2018"))
        EDUCATION_LIST.add(Education("2", "University of Mumbai", "Bachelors",
                "Information Technology", "2011-2014"))
    }

    private fun genExperienceList() {
        EXPERIENCE_LIST.add(Experience("1", "Motorola Mobility", "Oct 2018 - Present",
                "Multimedia Software Engg", "Chicago"))
        EXPERIENCE_LIST.add(Experience("2", "TransUnion", "May 2017 - Oct 2018",
                "Software Engg", "Chicago"))
        EXPERIENCE_LIST.add(Experience("3", "Oracle", "Aug 2014 - Aug 2016",
                "Software Engg", "Mumbai"))
    }

    fun getEducationList(): List<Education>  {
        if (EDUCATION_LIST.isEmpty()) genEducationList()
        return ArrayList<Education>(EDUCATION_LIST)
    }

    fun getExperienceList(): List<Experience> {
        if (EXPERIENCE_LIST.isEmpty()) genExperienceList()
        return ArrayList<Experience>(EXPERIENCE_LIST)
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
    data class Education(val id: String, val collName: String, val level: String, val major: String,
                         val gradYear: String)
    data class Experience(val id: String, val compName: String, val dur: String, val title: String,
                          val location: String)
}