package com.nazri.restaurantfoodrecognizer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Element adsElement = new Element();
        adsElement.setTitle("Developer : Nazri Hamzah" +
                "\nSupervisor : Dr. Hayati Abd Rahman" +
                "\nLecturer : Dr. Atiqah Sia Abdullah");

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.mipmap.ic_launcher)
                .setDescription("iFoodTag Food Recognizer")
                .addItem(new Element().setTitle("Version 2.0"))
                .addItem(adsElement)
                .addGroup("Connect With Me")
                .addEmail("nazri2ham@gmail.com")
                .addInstagram("nazhamz")
                .addItem(createCopyright())
                .create();

        setContentView(aboutPage);

    }
    private Element createCopyright()
    {
        final Element copyright = new Element();
        final String copyrightString = String.format("Copyright %d by Nazri Hamzah", Calendar.getInstance().get(Calendar.YEAR));
        copyright.setTitle(copyrightString);
        copyright.setIcon(R.drawable.ic_copyright_black_24dp);
        copyright.setGravity(Gravity.CENTER);
        copyright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(About.this,copyrightString,Toast.LENGTH_SHORT).show();
            }
        });
        return copyright;
    }
}
