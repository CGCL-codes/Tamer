    public static void cat(URL url) {
        try {
            cat(url.openStream(), url.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
