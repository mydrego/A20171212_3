package com.example.student.a20171212_3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.textView);
        MyTask task = new MyTask();
        task.execute("http://8k.hdwallpaperup.com/wp-content/uploads/2016/11/Bakgrunn-Moraine-Lake-Albarta-8K-Wallpaper.jpg");
    }
    class MyTask extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            URL url = null;
            int len;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            try {
                // create the HttpURLConnection
                url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // 使用甚麼方法做連線
                connection.setRequestMethod("GET");

                // 是否添加參數(ex : json...等)
                //connection.setDoOutput(true);

                // 設定TimeOut時間
                connection.setReadTimeout(15 * 1000);
                connection.connect();
                int totalLength = connection.getContentLength();
                int sum = 0;
                InputStream is = connection.getInputStream();
                // 伺服器回來的參數
                while ((len = is.read(buffer)) != -1)
                {
                    sum += len;
                    publishProgress(100 * sum / totalLength);
                    output.write(buffer, 0, len);
                }
                byte[] result = output.toByteArray();
                Bitmap bm = BitmapFactory.decodeByteArray(result, 0, result.length);
                return bm;
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            tv.setText(String.valueOf(values[0]));
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ImageView img = (ImageView) findViewById(R.id.imageView);
            img.setImageBitmap(bitmap);
        }
    }
}
