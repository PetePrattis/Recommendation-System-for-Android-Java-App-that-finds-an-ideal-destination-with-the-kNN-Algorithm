package com.unipi.p15013p15120.kastropoliteiesv2;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter {
    //class variables in which we give the values from the constructors
    Context context;
    public String[] nomoi;
    public String[] onomasies;
    public int[] images;
    public Integer[] ratings;
    String where;
    View view;
    LayoutInflater layoutInflater;

    //constructor for top, hot, matched and recommended locations list with 10 items
    public GridAdapter(Context context, String[] nomoi, String[] onomasies, int[] images, String where){
        //we give the values of the constructor variables to the class variables
        this.context = context;
        this.nomoi = nomoi;
        this.onomasies = onomasies;
        this.images = images;
        this.where = where;
    }
    //constructor for counties Lakonia and Messinia lists
    public GridAdapter(Context context, String[] onomasies, String where){
        //we give the values of the constructor variables to the class variables
        this.context = context;
        this.onomasies = onomasies;
        this.where = where;
    }
    //constructor for all locations list
    public GridAdapter(Context context, String[] nomoi, String[] onomasies, String where) {
        //we give the values of the constructor variables to the class variables
        this.context= context;
        this.nomoi = nomoi;
        this.onomasies= onomasies;
        this.where = where;
    }
    //constructor for locations rated by user list
    public GridAdapter(Context context, String[] nomoi, String[] onomasies, Integer[] ratings, String where) {
        //we give the values of the constructor variables to the class variables
        this.context= context;
        this.nomoi = nomoi;
        this.onomasies= onomasies;
        this.where = where;
        this.ratings = ratings;
    }

    @Override
    public int getCount() {
        return onomasies.length;
    }//we get the number of the items that will be shown in the grid view

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {//here we return the layout inflated view
        if(convertView == null){
            //we initialize the layout inflater using the context passed from the constructor
            final LayoutInflater layoutInflater = LayoutInflater.from(context);

            //according to the intent 'where' value we inflate the proper layout.xml
            if (where.equals("noimage"))
                convertView = layoutInflater.inflate(R.layout.location_item_no_image, null);
            else if (where.equals("ratings"))
                convertView = layoutInflater.inflate(R.layout.location_item_ratings, null);
            else if (where.equals("images"))
                convertView = layoutInflater.inflate(R.layout.location_item_image, null);
        }

        //according to the intent 'where' value we initialize the proper view variables for each layout
        if (where.equals("noimage")) {
            final TextView tvnomoi = convertView.findViewById(R.id.tvnomoi);
            final TextView tvonomasies = convertView.findViewById(R.id.tvonomasies);

            tvonomasies.setText(onomasies[position]);

            if (nomoi != null) {
                tvnomoi.setVisibility(View.VISIBLE);
                tvnomoi.setText(nomoi[position]);
            }
        }
        else if(where.equals("ratings")){
            final TextView tvnomoi = convertView.findViewById(R.id.tvnomoi);
            final TextView tvonomasies = convertView.findViewById(R.id.tvonomasies);
            final RatingBar ratingbar = convertView.findViewById(R.id.ratingBar);


            tvonomasies.setText(onomasies[position]);
            ratingbar.setRating(ratings[position]);

            if (nomoi != null) {
                tvnomoi.setVisibility(View.VISIBLE);
                tvnomoi.setText(nomoi[position]);
            }
        }
        else if (where.equals("images"))
        {
            final ImageView imageView = convertView.findViewById(R.id.imageview);
            final TextView tvnomoi = convertView.findViewById(R.id.tvnomoi);
            final TextView tvonomasies = convertView.findViewById(R.id.tvonomasies);

            //we resize the drawbles images to avoid out of memory error
            Bitmap b = BitmapFactory.decodeResource(context.getResources(), images[position]);

            //we reduce dimensions by a factor of 0.4
            float scaleFactor = 0.4f;
            int sizeX = Math.round(b.getWidth() * scaleFactor);
            int sizeY = Math.round(b.getHeight() * scaleFactor);

            try {
                Bitmap bitmapResized = Bitmap.createScaledBitmap(b, sizeX, sizeY, false);

                Drawable img = new BitmapDrawable(context.getResources(), bitmapResized);

                imageView.setImageDrawable(img);
            }catch(OutOfMemoryError e){//if resizing is still not enough
                //we set the image view as the noimage drawble
                imageView.setImageResource(R.drawable.noimage);

            }

            tvonomasies.setText(onomasies[position]);

            tvnomoi.setVisibility(View.VISIBLE);
            tvnomoi.setText(nomoi[position]);
        }

        //we return the view
        return convertView;
    }
}