package me.micartey.grepcon.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLVerifier {

    private static final Pattern urlPattern = Pattern.compile("(https://[^/]*/[^a-z]*)");

    public static String formatUrl(String url) {
        if (!urlPattern.matcher(url).find()) {
            if (!url.startsWith("http")) {
                url = "https://" + url + "/";
            }
        }

        for (int i = 0; i < url.length(); i++) {
            Matcher matcher = urlPattern.matcher(url);
            if (matcher.find()) {
                url = matcher.group(1);
                break;
            }
            url = url.substring(0, url.length() - 1);
        }

        return url;
    }
}
