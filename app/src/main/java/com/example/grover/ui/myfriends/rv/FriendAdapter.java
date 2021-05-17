package com.example.grover.ui.myfriends.rv;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.grover.R;
import com.example.grover.models.FirebaseDatabaseUser;

import java.io.InputStream;
import java.util.ArrayList;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    private ArrayList<FirebaseDatabaseUser> friends;
    final private OnListItemClickListener onListItemClickListener;

    public FriendAdapter(OnListItemClickListener onListItemClickListener) {
        this.friends = new ArrayList<>();
        this.onListItemClickListener = onListItemClickListener;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_friend, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.name.setText(friends.get(position).getName());
        viewHolder.loadImage(friends.get(position).getPhotoUrl());
    }

    public int getItemCount() {
        return friends.size();
    }

    public void setItemList(ArrayList<FirebaseDatabaseUser> firebaseDatabaseUsers) {
        friends = firebaseDatabaseUsers;
    }

    public interface OnListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        ImageView profilePic;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.friendName);
            profilePic = itemView.findViewById(R.id.imageView13);
            itemView.setOnClickListener(this);
        }
        public void loadImage(String url){
            new DownloadImageTask(profilePic).execute(url);
        }

        @Override
        public void onClick(View v) {
            onListItemClickListener.onListItemClick(getAdapterPosition());
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
}

