package com.example.grover.ui.addplant.rv;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grover.R;
import com.example.grover.models.trefle.TrefleSearchQueryStripped;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TrefleSearchAdapterRV extends RecyclerView.Adapter<TrefleSearchAdapterRV.ViewHolderTrefleSearch> {
    private List<TrefleSearchQueryStripped> resultList;
    final private OnTrefleResultsItemClickListener mOnListItemClickListener;

    public TrefleSearchAdapterRV(OnTrefleResultsItemClickListener listener){
        resultList = new ArrayList<>();
        mOnListItemClickListener = listener;

    }
    public void setNewDataSet(List<TrefleSearchQueryStripped> rl){
        resultList = rl;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolderTrefleSearch onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_trefle_search,parent,false);
        return new ViewHolderTrefleSearch(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTrefleSearch viewHolder, int position) {
        viewHolder.commonName.setText(resultList.get(position).common_name);
        viewHolder.species.setText(resultList.get(position).species);
        viewHolder.loadImage(resultList.get(position).image_url);
        if (resultList.get(position).selected)
            viewHolder.selected.setVisibility(View.VISIBLE);
        else
            viewHolder.selected.setVisibility(View.INVISIBLE);
    }


    @Override
    public int getItemCount() {
        return resultList.size();
    }
    class ViewHolderTrefleSearch extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView commonName;
        TextView species;
        ImageView image;
        ImageView selected;

        ViewHolderTrefleSearch(View itemView) {
            super(itemView);
            commonName = itemView.findViewById(R.id.commonNameSQ);
            species = itemView.findViewById(R.id.speciesSQ);
            image = itemView.findViewById(R.id.imageView10);
            selected = itemView.findViewById(R.id.selectedCheck);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnListItemClickListener.onListItemClick(getAdapterPosition());
        }
        public void loadImage(String url){
            new DownloadImageTask(image).execute(url);
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
}
