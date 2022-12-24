package me.micartey.grepcon.utilities;

public class URLVerifier {

    public static String formatUrl(String url) {

        // Verify that protocol exists
        if (!url.startsWith("http") || !url.contains("://")) {
            return formatUrl("https://" + url);
        }

        try {
            // Remove unnecessary url parts
            String[] domain = url.split("://")[1].split("/");
            if (domain.length > 1) {
                return formatUrl(domain[0]);
            }
        } catch(Throwable throwable) {
            System.out.println(url);
            throwable.printStackTrace();
        }

        // Make sure url ends with a "/"
        if (!url.endsWith("/")) {
            return formatUrl(url + "/");
        }

        return url;
    }
}
