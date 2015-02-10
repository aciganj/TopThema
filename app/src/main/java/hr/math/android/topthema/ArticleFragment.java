package hr.math.android.topthema;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ArticleFragment extends Fragment {

    private boolean downloaded;
    private MediaPlayer mp;
    private File file;
    private File dst;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_article, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ;

        String title = getArguments().getString("title");
        final String link = getArguments().getString("mp3link");
        final String editedTitle = title.trim().replaceAll(" ", "_").replaceAll("[?!.]", "") + ".mp3";

        final File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        file = new File(path, editedTitle);

        final File folder = new File(Environment.getExternalStorageDirectory() + "/TopThemaMedia/");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        dst = new File(folder, editedTitle);


        getActivity().setTitle(title);

        TextView textTV = (TextView) getView().findViewById(R.id.textTV);
        String text = getArguments().getString("text");
        textTV.setText(Html.fromHtml(text));

        ImageButton imageButton = (ImageButton) getView().findViewById(R.id.imageButton1);
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (mp != null && mp.isPlaying()) {
                    mp.stop();
                    return;
                }

                if (!dst.exists()) {
                    String[] children = folder.list();
                    if (children.length > 3) {
                        List<File> childrenFiles = new ArrayList<File>();
                        for (int i = 0; i < children.length; i++) {
                            File child = new File(folder + "/" + children[i]);
                            childrenFiles.add(child);
                        }
                        Collections.sort(childrenFiles, new Comparator<File>() {
                            @Override
                            public int compare(File lhs, File rhs) {
                                if (lhs.lastModified() < rhs.lastModified()) {
                                    return -1;
                                }
                                if (lhs.lastModified() > rhs.lastModified()) {
                                    return 1;
                                }
                                return 0;
                            }
                        });
                        childrenFiles.get(0).delete();
                    }
                    Utilities.saveMP3(link, editedTitle, getActivity());
                    ImageButton ImButton = (ImageButton) getView().findViewById(R.id.imageButton1);
                    ImButton.setClickable(false);
                }

                if (dst != null && dst.exists()) {
                    AsyncTask<Void, Void, Void> bs = new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {

                            mp = MediaPlayer.create(getActivity(), Uri.fromFile(dst));
                            mp.setVolume(100, 100);
                            mp.start();
                            return null;
                        }
                    };
                    bs.execute();
                }
            }
        });


        getActivity().registerReceiver(copyAfterDownload,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    BroadcastReceiver copyAfterDownload = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                Utilities.copyFile(file, dst);
                ImageButton imageButton = (ImageButton) getView().findViewById(R.id.imageButton1);
                imageButton.setClickable(true);
            } catch (IOException e) {
                Log.w("myApp", "exception when copying files to internal storage");
            }
            if (file.delete()) {
                Log.w("myApp", "file deleted");
            }
        }
    };
/*
    @Override
    protected void onPause() {
        super.onPause();
        if (mp != null && mp.isPlaying()) {
            mp.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(copyAfterDownload);
    }
    */
}