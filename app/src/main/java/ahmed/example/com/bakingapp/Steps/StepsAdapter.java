package ahmed.example.com.bakingapp.Steps;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ahmed.example.com.bakingapp.Models.Step;
import ahmed.example.com.bakingapp.R;

/**
 * Created by root on 22/09/17.
 */

public class StepsAdapter extends ArrayAdapter<Step> {

    public StepsAdapter(@NonNull Context context,
                        @NonNull List<Step> objects
    ) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.step_row, parent, false);
        }
        Step step = getItem(position);
        if (step.getVideoURL().equals("") && step.getThumbnailURL().equals("")) {
            ImageView containVideo = (ImageView) view.findViewById(R.id.play_image);
            containVideo.setVisibility(View.GONE);
        }
        TextView stepDes = (TextView) view.findViewById(R.id.step_description);
        stepDes.setText((position+1)+"."+step.getShortDescription());

        return view;

    }

}