package com.vtvcab.on.aosnews.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.vtvcab.on.aosnews.R
import com.vtvcab.ui.VtvDRMView
import kotlinx.android.synthetic.main.activity_full_screen.*

class FullScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_full_screen)

        vtvView.init(
            this,
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJHdWlkVmFsdWUiOiI4MjQwZmRiYy0xNzJiLTQyYjctYWZjNC00M2UwZTM4MzUzMzUiLCJVc2VyTmFtZSI6ImRhdCIsImV4cCI6MTczMzY5MzI4MCwiaXNzIjoiV1NJc3N1ZXIiLCJhdWQiOiJXU0F1ZGllbmNlIn0.drYV13iVgRhzdanTaw_JH8MSbk9VRSZP5uNOkbDIjjY",
            intent.getStringExtra("detailUrl"),
            "AILATRIEUPHU",
            VtvDRMView.LANDSCAPE
        )
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra("currentPosition", vtvView.playerView.player.currentPosition)
        setResult(Activity.RESULT_OK,intent)
        finish()
    }

    override fun onStop() {
        super.onStop()
        vtvView.release()
    }

    override fun onRestart() {
        super.onRestart()
        vtvView.restart()
    }
}