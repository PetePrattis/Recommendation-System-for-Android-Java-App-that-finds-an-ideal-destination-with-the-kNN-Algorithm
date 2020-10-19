package com.unipi.p15013p15120.kastropoliteiesv2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewOverlay;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;


//class prokeimenou na kanoume download photo
public class DownLoadImageTask extends AsyncTask<String,Void,Bitmap> {
    ImageView imgView;
    RoundedBitmapDrawable dr;
    MenuItem menuItem;
    Context context;

    public DownLoadImageTask(MenuItem menuItem, Context context) {this.menuItem = menuItem; this.context = context;}
    public DownLoadImageTask(ImageView imgView, Context context) {this.imgView = imgView; this.context = context;}

        /*
            doInBackground(Params... params)
                Override this method to perform a computation on a background thread.
         */
    protected Bitmap doInBackground(String...urls){
        String urlOfImage = urls[0];
        Bitmap logo = null;
        try{
            InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */


            logo = BitmapFactory.decodeStream(is);

        }catch(Exception e){ // Catch the download exception
            e.printStackTrace();
        }
        return logo;
    }

        /*
            onPostExecute(Result result)
                Runs on the UI thread after doInBackground(Params...).
         */
    protected void onPostExecute(Bitmap result){

        if (result!=null) {
            //for user profile picture in home page
            if (menuItem != null)
                menuItem.setIcon(makeRound(result));
            //imgButton.setImageDrawable(dr);
            else
                //for user prof pic in profile dialog
                imgView.setImageBitmap(result);
        }
    }

    //rounded image
    private RoundedBitmapDrawable makeRound(Bitmap result)
    {
       dr = RoundedBitmapDrawableFactory.create(context.getResources(), result);
       dr.setCornerRadius(Math.max(result.getWidth(), result.getHeight()) / 2.0f);
        //else
          //  dr = RoundedBitmapDrawableFactory.create(context.getResources(),BitmapFactory.decodeResource(context.getResources(),R.drawable.logo));
        return dr;
    }
}
