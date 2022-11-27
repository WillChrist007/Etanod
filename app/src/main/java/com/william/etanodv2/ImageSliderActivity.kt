package com.william.etanodv2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import com.denzcoskun.imageslider.constants.ScaleTypes

class ImageSliderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_slider)

        val imageList = ArrayList<SlideModel>() // Create image list


        imageList.add(SlideModel(
            "https://c1.wallpaperflare.com/preview/161/626/530/donations-donation-box-charity-donate.jpg",
            "Setiap sumbangan yang Anda berikan sangat berarti bagi mereka yang membutuhkan.",
            ScaleTypes.CENTER_CROP))

        imageList.add(SlideModel(
            "https://w0.peakpx.com/wallpaper/752/380/HD-wallpaper-help-black-hand-butterflies-female-flowers-hands-helping-male-white-hand-thumbnail.jpg",
            "Manusia diciptakan untuk saling menolong sesamanya.",
            ScaleTypes.CENTER_CROP))

        imageList.add(SlideModel(
            "https://images.unsplash.com/photo-1532629345422-7515f3d16bb6?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8ZG9uYXRpb258ZW58MHx8MHx8&w=1000&q=80",
            "Jadilah pelopor untuk menolong sesama Anda.",
            ScaleTypes.CENTER_CROP))

        imageList.add(SlideModel(
            "https://images.unsplash.com/photo-1488521787991-ed7bbaae773c?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8M3x8c21pbGluZyUyMGNoaWxkcmVufGVufDB8fDB8fA%3D%3D&w=1000&q=80",
            "Terima Kasih telah berpartisipasi menciptakan dunia yang lebih baik.",
            ScaleTypes.CENTER_CROP))

        val imageSlider = findViewById<ImageSlider>(R.id.image_slider)
        imageSlider.setImageList(imageList)
    }
}