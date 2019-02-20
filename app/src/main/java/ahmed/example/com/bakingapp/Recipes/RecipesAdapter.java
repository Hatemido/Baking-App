package ahmed.example.com.bakingapp.Recipes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ahmed.example.com.bakingapp.Models.Recipe;
import ahmed.example.com.bakingapp.R;

/**
 * Created by root on 21/09/17.
 */

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.MyHolder> {

    private Context context;
    private List<Recipe> recipes;
    private onItemClickListener mOnItemClickListener;

    public RecipesAdapter(Context context,
                          List<Recipe> recipes, onItemClickListener mOnItemClickListener
    ) {
        this.context = context;
        this.recipes = recipes;
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(context).inflate(R.layout.baking_card_view,
                                                                 parent,
                                                                 false
        ));
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (recipes == null) return 0;
        return recipes.size();
    }

    interface onItemClickListener {

        void onItemClickActionListener(int position);
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView ingredients;
        TextView name;
        ImageView image;

        public MyHolder(View itemView) {
            super(itemView);
            ingredients = (TextView) itemView.findViewById(R.id.ingredients);
            name = (TextView) itemView.findViewById(R.id.video_Name);
            image = (ImageView) itemView.findViewById(R.id.recipe_image);
            itemView.setOnClickListener(this);
        }

        void bind(int position) {
            Recipe recipe = recipes.get(position);
            ingredients.setText("" + recipe.getIngredients().size());
            name.setText(recipe.getName());
            if(!recipe.getImage().equals("")){
                Picasso.with(context).load(recipe.getImage())
                       .placeholder(R.drawable.round_rectangle_white).into(image);
            }else {
                image.setVisibility(View.GONE);
            }

        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mOnItemClickListener.onItemClickActionListener(position);

        }
    }

}
//                Bitmap bitmap= ThumbnailUtils.createVideoThumbnail(recipe.getSteps().get(0).getVideoURL(), MediaStore.Video.Thumbnails.MINI_KIND);
//            image.setDefaultArtwork(bitmap);
//            }
//            new AsyncTask<Void, Void, Bitmap>() {
//
//                @Override
//                protected Bitmap doInBackground(Void... params) {
//                    Bitmap bitmap = null;
//                    String videoPath = null;
//                    for (Step steps : recipe.getSteps()) {
//                        if(!steps.getThumbnailURL().equals("")){
//                            videoPath=steps.getThumbnailURL();
//                            break;
//                        }
//                    }
//                    MediaMetadataRetriever mediaMetadataRetriever = null;
//                    try {
//                        mediaMetadataRetriever = new MediaMetadataRetriever();
//                        mediaMetadataRetriever.setDataSource(videoPath,
//                                                             new HashMap<String, String>()
//                        );
//                        //   mediaMetadataRetriever.setDataSource(videoPath);
//                        bitmap = mediaMetadataRetriever.getFrameAtTime();
//                    } catch (Exception e) {
//
//                    } finally {
//                        if (mediaMetadataRetriever != null)
//                            mediaMetadataRetriever.release();
//                    }
//                    return bitmap;
//                }
//
//                @Override
//                protected void onPostExecute(Bitmap bitmap) {
//                    super.onPostExecute(bitmap);
//                    if (bitmap != null)
//                        image.setImageBitmap(bitmap);
//                }
//            }.execute();