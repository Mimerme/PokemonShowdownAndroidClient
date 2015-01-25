package com.pokemonshowdown.replays;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Battle log of all events are stored in
 * TODO : Allow cached copies of replays
 */
public abstract class RetrieveReplayTask extends AsyncTask<String, Void, String> implements ResponseHandler {
    String html = "";

    @Override
    protected String doInBackground(String... url) {
        Log.i("Getting Replay", url[0]);
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(url[0]);
            HttpResponse response = client.execute(request);

            InputStream in = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder str = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) != null)
            {
                str.append("::" + line);
            }
            in.close();
            html = str.toString();
            html = html.substring(html.indexOf("<script type=\"text/plain\" class=\"log\">") + 38);
            html = html.substring(0, html.indexOf("</script>"));
            Log.i("Sauce",html);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return html;
    }

    @Override
    protected void onPostExecute(String element) {
        OnResponseRecived(element);
        return;
    }

    @Override
    public abstract void OnResponseRecived(Object result);

}