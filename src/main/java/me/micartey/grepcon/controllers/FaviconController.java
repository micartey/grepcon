package me.micartey.grepcon.controllers;

import me.micartey.grepcon.utilities.URLToSource;
import me.micartey.grepcon.utilities.URLVerifier;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/api/v1/favicon")
public class FaviconController {

    private final Pattern pattern = Pattern.compile("<(link rel=\"[a-z ]*icon\"[^<>]*)>");
    private final Pattern assetUrl = Pattern.compile("href=\"([^<>\"]*)\"");

    @CrossOrigin
    @GetMapping("/list/raw")
    public ResponseEntity<List<String>> getFaviconsFromUrl(@RequestParam String url, @RequestParam String fallback) throws IOException {

        url = URLVerifier.formatUrl(url);
        Map.Entry<Integer, String> response = URLToSource.getURLSource(url);

        if(response.getKey() != 200) {
            return ResponseEntity.badRequest().body(List.of(fallback));
        }

        if(pattern.matcher(response.getValue()).find()) {
            Matcher matcher = pattern.matcher(response.getValue());
            List<String> matches = new LinkedList<>();

            while(matcher.find()) {
                for(int group = 0; group < matcher.groupCount(); group++) {
                    Matcher assetMatcher = assetUrl.matcher(matcher.group(group));

                    while(assetMatcher.find()) {
                        for(int assetGroup = 0; assetGroup < matcher.groupCount(); assetGroup++) {
                            String asset = assetMatcher.group(assetGroup)
                                    .substring(6, assetMatcher.group(assetGroup).length() - 1);

                            matches.add(String.format("%s%s",
                                    asset.startsWith("http") ? "" : url,
                                    asset.startsWith("/") ? asset.substring(1) : asset
                            ));
                        }
                    }
                }
            }

            return ResponseEntity.ok(matches.size() > 0 ? matches : List.of(fallback));
        }

        return ResponseEntity.ok(List.of(url + "favicon.ico"));
    }

    @CrossOrigin
    @GetMapping
    public ResponseEntity<byte[]> getFaviconFromUrl(@RequestParam String url, @RequestParam String fallback) throws IOException {
        ResponseEntity<List<String>> favicons = getFaviconsFromUrl(url, fallback);
        List<String> entries = favicons.getBody();
        Collections.reverse(entries);
        String imageUrl = entries.get(0);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] chunk = new byte[4096];
            int bytesRead;

            HttpURLConnection connection = (HttpURLConnection) new URL(imageUrl).openConnection();
            InputStream stream = connection.getInputStream();

            while ((bytesRead = stream.read(chunk)) > 0)
                outputStream.write(chunk, 0, bytesRead);

            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(outputStream.toByteArray());
        }
    }
}
