package io.github.biswajee.sirena;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.zip.Inflater;

/**
 * Created by Biswajit Roy on 03-08-2018.
 */

public class postAdapter extends RecyclerView.Adapter<postAdapter.postViewHolder> {
    @NonNull
    private String[] data = {"Hey There", "See you soon", "Hello wassup ?", "Bye", "See you soon", "Hello wassup ?", "Bye"};

    public postAdapter(String[] data){
        data = this.data;
    }

    @Override

    public postViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.post_list_view, parent, false);
        return new postViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull postViewHolder holder, int position) {

        String postContent = data[position];
        holder.postData.setText(postContent);

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class postViewHolder extends RecyclerView.ViewHolder{
        ImageView postMakerImage;
        TextView postData;
        public postViewHolder(View itemView) {
            super(itemView);
            postMakerImage = (ImageView) itemView.findViewById(R.id.post_maker_image);
            postData = (TextView) itemView.findViewById(R.id.postData);

        }
    }
}
