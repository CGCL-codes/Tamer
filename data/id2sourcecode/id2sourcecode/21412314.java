    public static String loadFromURL(URL url) {
        if (url == null) {
            return null;
        }
        try {
            InputStreamReader reader = new InputStreamReader(url.openStream());
            StringBuilder sb = new StringBuilder();
            char[] buf = new char[4096];
            int read;
            while ((read = reader.read(buf)) > 0) {
                sb.append(buf, 0, read);
            }
            ;
            reader.close();
            return sb.toString();
        } catch (IOException e) {
            return null;
        }
    }
