package hu.bme.aut.amorg.education.rxjava;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Network {

    public static final String URL = "http://www.timeapi.org/utc/now";

    public static String getTimeFromApi() throws IOException {
        URL url = new URL(URL);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String read;

            while ((read = br.readLine()) != null) {
                sb.append(read);
            }

            br.close();
            return sb.toString();
        } finally {
            urlConnection.disconnect();
        }
    }
}
