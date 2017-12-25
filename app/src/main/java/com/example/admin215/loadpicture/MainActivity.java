package com.example.admin215.loadpicture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String[] imageUrls = {
            "http://srd-maestro.ru/content/catalog/75_south-landscape/1/fo3127.jpg",
            "http://img1.postila.ru/storage/6368000/6341379/48491bc488f1a3158b64913acd14a559.jpg",
            "http://is5.mzstatic.com/image/thumb/Music6/v4/1e/e4/57/1ee457b0-e296-a959-a424-56e2c5502ee0/source/100x100bb.jpg",
            "http://wallpaperscraft.ru/image/oblaka_pasmurno_nebo_tuchi_hmuroe_seroe_kusty_leto_zelen_55744_300x175.jpg"};
    TableRow tableRow;
    ProgressBar progressBar;
    Button loadButton;
    ArrayList imageView = new ArrayList();
    AsyncImageLoader loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tableRow = (TableRow) findViewById(R.id.images);
        progressBar = (ProgressBar) findViewById(R.id.loading_progress);
        loadButton = (Button) findViewById(R.id.button1);
        setUpViews();


    }

    protected void startLoading(View v){
        if(loader == null || loader.getStatus() == AsyncTask.Status.FINISHED) {
            loader = new AsyncImageLoader();
            loader.execute(imageUrls);
            progressBar.setProgress(0);
        }
    }
    private void setUpViews() {
        for (int i = 0; i < tableRow.getChildCount(); i++) {
            imageView.add((ImageView) tableRow.getChildAt(i));
        }
    }


    class AsyncImageLoader extends AsyncTask<String, Pair<Integer, Bitmap>, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            for (int i = 0; i < strings.length; i++) {
                try {
                    Bitmap image = getImageByUrl(strings[i]);
                    Thread.sleep(4000);
                    Pair<Integer, Bitmap> pair = new Pair<>(i, image);
                    publishProgress(pair);
                } catch (IOException e) {
                   // Toast.makeText(MainActivity.this, "Не могу загрузить картинку", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        private Bitmap getImageByUrl(String url) throws MalformedURLException, IOException {
            Bitmap image = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
            return image;
        }

        @Override
        protected void onProgressUpdate(Pair<Integer, Bitmap>... values) {
            super.onProgressUpdate(values);
            Bitmap image = values[0].second;
            int position = values[0].first;
            int current = (position + 1) * 100;
            progressBar.setProgress(current);
            ((ImageView)imageView.get(position)).setImageBitmap(image);
            ((ImageView)tableRow.getChildAt(position)).setImageBitmap(image);
        }
    }
}