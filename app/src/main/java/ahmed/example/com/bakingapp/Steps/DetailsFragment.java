package ahmed.example.com.bakingapp.Steps;


import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import ahmed.example.com.bakingapp.Models.Recipe;
import ahmed.example.com.bakingapp.Models.Step;
import ahmed.example.com.bakingapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {

    public static final String RECIPE = "recipe";
    public static final String TABLET = "isTablet";

    public static final String POSITION = "position";
    private static final String VIDEO_POSITION = "VideoPosition";
    boolean isTablet;


    private View view;
    private String videoUrl;

    public DetailsFragment() {
        // Required empty public constructor
    }

    long videoPosition = -1;
    @BindView(R.id.video_view)
    SimpleExoPlayerView simpleExoPlayerView;
    @BindView(R.id.step_instruction)
    TextView stepInstruction;
    Step step;
    Recipe recipe;
    int position;
    SimpleExoPlayer player;
    @BindView(R.id.next)
    FloatingActionButton next;
    @BindView(R.id.previous)
    FloatingActionButton previous;
    @BindView(R.id.thumbnail_url)
    ImageView thumbnail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState
    ) {
        view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);

        if (savedInstanceState != null) {
            recipe = savedInstanceState.getParcelable(RECIPE);
            position = savedInstanceState.getInt(POSITION);
            isTablet = savedInstanceState.getBoolean(TABLET);
            videoPosition = savedInstanceState.getLong(VIDEO_POSITION);
            setData(recipe, position, isTablet);

        }
        setExoPLayer();
        setUi();
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECIPE, recipe);
        outState.putInt(POSITION, position);
        outState.putLong(VIDEO_POSITION, videoPosition);

    }

    private void setUi() {
        stepInstruction.setText(step.getDescription());
        if (isTablet) {
            previous.setVisibility(View.GONE);
            next.setVisibility(View.GONE);
        } else if (position == 0) {
            previous.setVisibility(View.GONE);
            next.setVisibility(View.VISIBLE);
        } else if (position == recipe.getSteps().size()) {
            previous.setVisibility(View.VISIBLE);
            next.setVisibility(View.GONE);
        } else {
            previous.setVisibility(View.VISIBLE);
            next.setVisibility(View.VISIBLE);
        }
        if (!step.getThumbnailURL().equals("")) {
            Picasso.with(getActivity()).load(step.getThumbnailURL()).into(thumbnail);
        }
    }

    @OnClick(R.id.next)
    void next() {
        clearPLayer();
        videoPosition = -1;
        setData(recipe, position + 1, isTablet);
        setExoPLayer();
        setUi();

    }

    @OnClick(R.id.previous)
    void previous() {
        clearPLayer();
        videoPosition = -1;
        setData(recipe, position - 1, isTablet);
        setExoPLayer();
        setUi();

    }

    /**
     * set SimpleExoPLayerView
     * and set the default Controls
     * and add the video uri to it
     */
    private void setExoPLayer() {
        if (player != null) return;
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
        simpleExoPlayerView.setUseController(true);
        simpleExoPlayerView.requestFocus();
        if (videoPosition != -1) player.seekTo(videoPosition);

        simpleExoPlayerView.setPlayer(player);
        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
        DefaultDataSourceFactory defaultDataSourceFactory =
                new DefaultDataSourceFactory(getActivity(),
                                             Util.getUserAgent(getActivity(),
                                                               getString(R.string.app_name)
                                             ),
                                             defaultBandwidthMeter
                );

        videoUrl = null;
        if (step.getThumbnailURL().equals("") && step.getVideoURL().equals("")) {
            simpleExoPlayerView.setVisibility(View.GONE);
            return;
        } else if (!step.getVideoURL().equals("")) {

            videoUrl = step.getVideoURL();
        }

        simpleExoPlayerView.setVisibility(View.VISIBLE);
        Uri videoUri = Uri.parse(videoUrl);

        MediaSource mediaSource = new ExtractorMediaSource(videoUri,
                                                           defaultDataSourceFactory,
                                                           new DefaultExtractorsFactory(),
                                                           null,
                                                           null
        );

        player.prepare(mediaSource);
        player.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups,
                                        TrackSelectionArray trackSelections
            ) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                player.stop();
                player.setPlayWhenReady(true);

            }

            @Override
            public void onPositionDiscontinuity() {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }
        });
        player.setPlayWhenReady(true);

    }

    @Override
    public void onPause() {
        super.onPause();
        clearPLayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (videoUrl != null) {
            setExoPLayer();
        }
    }

    public void setData(Recipe recipe, int position, boolean isTablet) {
        this.recipe = recipe;
        this.position = position;
        this.isTablet = isTablet;
        step = recipe.getSteps().get(position);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clearPLayer();

    }

    private void clearPLayer() {
        if (player != null) {
            player.stop();
            videoPosition = player.getCurrentPosition();
            player.release();
            player = null;
        }
    }
}
