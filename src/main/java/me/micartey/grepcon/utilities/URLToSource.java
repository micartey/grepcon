package me.micartey.grepcon.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class URLToSource {

    public static Map.Entry<Integer, String> getURLSource(String url) throws IOException {
        URL urlObject = new URL(url);
        URLConnection urlConnection = urlObject.openConnection();
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

        HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;

        if (httpConnection.getResponseCode() >= 300 && httpConnection.getResponseCode() < 400) {
            String redirectUrl = httpConnection.getHeaderField("Location");

            if (redirectUrl == null) {
                return Map.entry(
                    500,
                    "Redirection could not be resolved"
                )
            }
            
            return getURLSource(redirectUrl);
        }

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8))) {
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = bufferedReader.readLine()) != null)
                stringBuilder.append(inputLine);

            return Map.entry(
                    httpConnection.getResponseCode(),
                    stringBuilder.toString()
            );
        } catch (Exception exception) {
            return Map.entry(
                    500,
                    exception.getMessage()
            );
        }
    }

}
