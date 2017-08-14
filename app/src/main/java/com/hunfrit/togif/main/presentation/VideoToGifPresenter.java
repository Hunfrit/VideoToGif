package com.hunfrit.togif.main.presentation;

import android.content.Context;

import com.hunfrit.togif.main.AsyncTask.VideoToGifAsyncTask;
import com.hunfrit.togif.main.View.MainView;


/**
 * Created by Artem Shapovalov on 10.08.2017.
 */

public class VideoToGifPresenter {

    private MainView view;

    public VideoToGifPresenter(MainView view) {this.view = view; }

    public void convertToGif(int millis){
        VideoToGifAsyncTask toGIF = new VideoToGifAsyncTask(view);
        toGIF.execute(millis);
    }
}
