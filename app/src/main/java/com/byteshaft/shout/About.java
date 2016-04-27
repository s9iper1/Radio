package com.byteshaft.shout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class About extends AppCompatActivity implements View.OnClickListener {

    private TextView textView;
    private TextView link;
    public static String text = "To view more 8CCC resources please visit this link below.";
    private TextView infoLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_layout);
        textView = (TextView) findViewById(R.id.text_view);
        textView.setText(Html.fromHtml(AppGlobals.ABOUT_TEXT.replace("\n","<br />")));
        link = (TextView) findViewById(R.id.text);
        infoLink = (TextView) findViewById(R.id.info_link);
        infoLink.setOnClickListener(this);
        link.setText(text);
        AppCompatActivity appCompatActivity =  this;
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:return false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.info_link:
                System.out.println("CLICK");
                MainActivity.getInstance().externalUrlConfirmation("http://8ccc.com.au/schedule", About.this);
                break;
        }
    }
}
