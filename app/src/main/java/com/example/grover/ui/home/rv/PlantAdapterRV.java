package com.example.grover.ui.home.rv;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.grover.R;
import com.example.grover.data.HomeRepository;
import com.example.grover.models.FirebaseDatabaseUser;
import com.example.grover.models.Plant;
import com.example.grover.ui.addplant.rv.TrefleSearchAdapterRV;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

public class PlantAdapterRV extends RecyclerView.Adapter<PlantAdapterRV.ViewHolder> {

    private ArrayList<Plant> mPlants;
    final private OnListItemClickListener mOnListItemClickListener;
    final private OnListItemLongClickListener mOnListItemLongClickListener;
    private DatabaseReference mDatabaseRef;
    private Context context;
    private FirebaseDatabaseUser owner;
    File localFile;

    public PlantAdapterRV(ArrayList<Plant> plants, OnListItemClickListener listener, OnListItemLongClickListener listener2){
        mPlants = plants;
        mOnListItemClickListener = listener;
        mOnListItemLongClickListener = listener2;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.listi_tem_plant_card, parent, false);
        return new ViewHolder(view);
    }

    public void setNewDataSet(ArrayList<Plant> nPlants){
        mPlants = nPlants;
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.name.setText(mPlants.get(position).getName());
        viewHolder.nameLatin.setText(mPlants.get(position).getLatinName());
        viewHolder.waterTExt.setText(mPlants.get(position).waterWhen());

        //Add margin top to the first row of cards
        if(position < 2){
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) viewHolder.card.getLayoutParams();
            lp.setMargins(15, (int) pixelsToDp(viewHolder, 250),15,15);
        }
        else{
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) viewHolder.card.getLayoutParams();
            lp.setMargins(15,15,15,15);
        }


        //Download and view image
        StorageReference ref = FirebaseStorage.getInstance().getReference();
        ref.child("images/" + mPlants.get(position).getImageId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                //viewHolder.icon.setImageURI(uri);
                Glide.with(context)
                        .load(uri)
                        .into(viewHolder.icon);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.e("tag", exception.toString());
            }
        });


        //If plant needs watering
        if(mPlants.get(position).waterToday()<=0){
            if(mPlants.get(position).waterToday()==0)
                viewHolder.hydroMeterBg.setImageResource(R.drawable.orange_gradient);
            else
                viewHolder.hydroMeterBg.setImageResource(R.drawable.red_gradient);

            //Move text up
            ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) viewHolder.waterTExt.getLayoutParams();
            lp.setMargins(0, (int)pixelsToDp(viewHolder, 0), 0, 0);
            viewHolder.waterTExt.setLayoutParams(lp);

            //Change text color and size
            viewHolder.waterTExt.setTextColor(viewHolder.context.getColor(R.color.white));
            viewHolder.waterTExt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);

            //Remove hydrometer
            viewHolder.hydroMeter.setLayoutParams(new FrameLayout.LayoutParams(0, (int) pixelsToDp(viewHolder, 15)));

            viewHolder.waterTExt.setElevation(pixelsToDp(viewHolder, 2));
        }
        else{
            viewHolder.hydroMeterBg.setImageResource(R.drawable.white_gradient);
            //Move text up
            ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) viewHolder.waterTExt.getLayoutParams();
            lp.setMargins(0, (int) pixelsToDp(viewHolder, 16), 0, 0);
            viewHolder.waterTExt.setLayoutParams(lp);
            //Change text color and size
            viewHolder.waterTExt.setTextColor(viewHolder.context.getColor(R.color.text_light));
            viewHolder.waterTExt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);

            double width = (mPlants.get(position).getDaysBetweenWater() - mPlants.get(position).getDaysSinceLastWater())/mPlants.get(position).getDaysBetweenWater() * 120;
            viewHolder.hydroMeter.setLayoutParams(new FrameLayout.LayoutParams((int) pixelsToDp(viewHolder, (int) width), (int) pixelsToDp(viewHolder, 15)));
        }

        if (mPlants.get(position).getTrefleId() != 0)
            viewHolder.trefleLogo.setVisibility(View.VISIBLE);
        else
            viewHolder.trefleLogo.setVisibility(View.INVISIBLE);

        if (owner == null){
            viewHolder.ownerContainer.setVisibility(View.GONE);
        }
        else {
            viewHolder.loadImage(owner.getPhotoUrl());
            viewHolder.ownerContainer.setVisibility(View.VISIBLE);
        }

        //Align card
        if (position % 2 == 0)
            viewHolder.cardHolder.setGravity(Gravity.RIGHT);
        else
            viewHolder.cardHolder.setGravity(Gravity.LEFT);

        viewHolder.waterAmount.setText(mPlants.get(position).getWaterLevel() + "x");
        viewHolder.location.setText(HomeRepository.getInstance().getHome().getValue().getRoomById(mPlants.get(position).getRoomId()));
    }
    public int getItemCount() {
        return mPlants.size();
    }

    public void setOwner(FirebaseDatabaseUser friend) {
        owner = friend;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        LinearLayout cardHolder;
        TextView name;
        TextView nameLatin;
        TextView waterAmount;
        TextView waterTExt;
        TextView location;
        ImageView icon;
        ImageView ownerImage;
        ImageView trefleLogo;
        ImageView drop1;
        CardView card;
        CardView ownerContainer;
        CardView hydroMeter;
        ImageView hydroMeterBg;
        Context context;

        ViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card);
            cardHolder = itemView.findViewById(R.id.cardHolder);
            ownerContainer = itemView.findViewById(R.id.ownerContainer);
            name = itemView.findViewById(R.id.plantName);
            nameLatin = itemView.findViewById(R.id.textView4);
            waterAmount = itemView.findViewById(R.id.textView6);
            waterTExt = itemView.findViewById(R.id.waterText);
            ownerImage = itemView.findViewById(R.id.ownerImage);
            location = itemView.findViewById(R.id.location);
            icon = itemView.findViewById(R.id.imageView2);
            hydroMeter = itemView.findViewById(R.id.hydroMeter);
            hydroMeterBg = itemView.findViewById(R.id.hydroMeterBg);
            drop1 = itemView.findViewById(R.id.drop1);
            trefleLogo = itemView.findViewById(R.id.trefle);
            context = itemView.getContext();
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnListItemClickListener.onListItemClick(getAdapterPosition());
        }
        @Override
        public boolean onLongClick(View v) {
            mOnListItemLongClickListener.onListItemLongClick(getAdapterPosition());
            return true;
        }

        public void loadImage(String url){
            new DownloadImageTask(ownerImage).execute(url);
        }

        private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
            ImageView bmImage;

            public DownloadImageTask(ImageView bmImage) {
                this.bmImage = bmImage;
            }

            protected Bitmap doInBackground(String... urls) {
                String urldisplay = urls[0];
                Bitmap mIcon11 = null;
                try {
                    InputStream in = new java.net.URL(urldisplay).openStream();
                    mIcon11 = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
                return mIcon11;
            }

            protected void onPostExecute(Bitmap result) {
                bmImage.setImageBitmap(result);
            }
        }
    }

    public interface OnTrefleResultsItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public interface OnListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }
    public interface OnListItemLongClickListener {
        void onListItemLongClick(int clickedItemIndex);
    }
    private float pixelsToDp(ViewHolder viewHolder, int pixels){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) pixels, viewHolder.context.getResources().getDisplayMetrics());
    }
}
