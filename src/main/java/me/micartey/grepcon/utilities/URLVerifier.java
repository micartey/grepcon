package me.micartey.grepcon.utilities;

public class URLVerifier {

    public static String formatUrl(String url) {

        // Skip verification of no link is supplied
        if (url.isEmpty()) {
            return url;
        }

        // Verify that protocol exists
        if (!url.startsWith("http") || !url.contains("://")) {
            return formatUrl("https://" + url);
        }

        // Remove unnecessary url parts
        String[] domain = url.split("://")[1].split("/");
        if (domain.length > 1) {
            return formatUrl(domain[0]);
        }

        // Make sure url ends with a "/"
        if (!url.endsWith("/")) {
            return formatUrl(url + "/");
        }

        return url;
    }
}
