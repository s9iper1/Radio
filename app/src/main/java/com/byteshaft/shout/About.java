package com.byteshaft.shout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

public class About extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_layout);
        textView = (TextView) findViewById(R.id.text_view);
        textView.setText(Html.fromHtml(AppGlobals.ABOUT_TEXT.replace("\n","<br />")));
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
}
