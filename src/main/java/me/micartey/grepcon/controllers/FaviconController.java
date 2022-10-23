package me.micartey.grepcon.controllers;

import me.micartey.grepcon.utilities.URLToSource;
import me.micartey.grepcon.utilities.URLVerifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/api/v1/favicon")
public class FaviconController {

    private final Pattern pattern = Pattern.compile("<(link\\srel=\"[a-z ]*icon\"[^<>]*)>");
    private final Pattern assetUrl = Pattern.compile("href=\"([^<>\"]*)\"");

    @GetMapping
    public ResponseEntity<List<String>> getWebsiteUrl(@RequestParam String url, @RequestParam String fallback) throws IOException {

        url = URLVerifier.formatUrl(url);

        Map.Entry<Integer, String> response = URLToSource.getURLSource(url);

        if (response.getKey() != 200) {
            return ResponseEntity.badRequest().body(List.of(fallback));
        }

        if (pattern.matcher(response.getValue()).find()) {
            Matcher matcher = pattern.matcher(response.getValue());
            List<String> matches = new LinkedList<>();

            while (matcher.find()) {
                for (int group = 0; group < matcher.groupCount(); group++) {
                    Matcher assetMatcher = assetUrl.matcher(matcher.group(group));

                    while(assetMatcher.find()) {
                        for (int assetGroup = 0; assetGroup < matcher.groupCount(); assetGroup++)
                            matches.add(
                                    url + assetMatcher.group(assetGroup)
                                            .substring(6, assetMatcher.group(assetGroup).length() - 1)
                            );
                    }
                }
            }

            return ResponseEntity.ok(matches);
        }

        return ResponseEntity.ok(List.of(url + "/favicon.ico"));
    }
}
