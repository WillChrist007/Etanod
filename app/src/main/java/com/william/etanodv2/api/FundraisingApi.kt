package com.william.etanodv2.api

class FundraisingApi {
    companion object {
        val BASE_URL = "http://192.168.0.102/ci4-apiserver/public/"

        val GET_ALL_URL = BASE_URL + "fundraising/"
        val GET_BY_ID_URL = BASE_URL + "fundraising/"
        val ADD_URL = BASE_URL + "fundraising"
        val UPDATE_URL = BASE_URL + "fundraising/"
        val DELETE_URL = BASE_URL + "fundraising/"
    }
}