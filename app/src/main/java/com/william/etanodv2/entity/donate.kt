package com.william.etanodv2.entity

class donate (val judul: String, val jumlah: Double, val durasi: String) {
    companion object {
        @JvmField
        var listOfDonate = arrayOf(
            donate("Tornado Di Neraka", 0.0, "1 Minggu"),
            donate("Banjir Di Jakarta", 1282500.0, "5 Jam"),
            donate("Banjir Di Konoha", 173782.0, "1 Hari"),
            donate("Gempa Di Atlantis", 827128.0, "3 Jam"),
            donate("Gempa Di Batam", 87369.0, "2 Hari"),
            donate("Panti Jompo Tua", 112672.0, "6 Jam"),
            donate("Orang Cacat", 327282.0, "5 Hari"),
            donate("Panti Asuhan YTim", 4357572.0, "50 Menit"),
            donate("Kebakaran Desa", 986382.0, "24 Jam"),
            donate("Bantuan Pemulung", 218572.0, "9 Hari")
        )
    }
}