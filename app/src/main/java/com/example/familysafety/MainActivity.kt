package com.example.familysafety

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    val permission= arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.READ_CONTACTS,

    )
    val permissioncode =78

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        askForPermission()

        val  bottomBar = findViewById<BottomNavigationView>(R.id.bottom_bar)

        bottomBar.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    inflateFragment(HomeFragment.newInstance())
                }
                R.id.nav_dboard -> {
                    inflateFragment(MapsFragment())
                }
                R.id.nav_gaurd -> {
                    inflateFragment(GuardFragment.newInstance())
                }
                R.id.nav_profile -> {
                    inflateFragment(ProfileFragment.newInstance())
                }
            }
            true
          }
        bottomBar.selectedItemId=R.id.nav_home
    }

    private fun askForPermission() {
        ActivityCompat.requestPermissions(this,permission,permissioncode)
    }

    private fun inflateFragment(newInstance: Fragment) {
      val transaction=supportFragmentManager.beginTransaction()
      transaction.replace(R.id.container,newInstance)
       transaction.commit()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
        if(requestCode==permissioncode)
        {
          if (allPermissionGranted())
          {

          }else
          {

          }
        }
    }

    private fun openCamera() {
        val intent=Intent("android.media.action.IMAGE_CAPTURE")
        startActivity(intent)
    }

    private fun allPermissionGranted(): Boolean {
        for(item in permission)
        {
            if(ContextCompat.checkSelfPermission(this,item)!=PackageManager.PERMISSION_GRANTED)
            {
                return false
            }
        }
       return true
    }

}