package com.william.etanodv2.api

class VolunteerApi {
    companion object {
        val BASE_URL = "http://192.168.0.108/API_ETANOD/public/api/"

        val GET_ALL_URL = BASE_URL + "volunteer/"
        val GET_BY_ID_URL = BASE_URL + "volunteer/"
        val ADD_URL = BASE_URL + "volunteer"
        val UPDATE_URL = BASE_URL + "volunteer/"
        val DELETE_URL = BASE_URL + "volunteer/"
    }
}