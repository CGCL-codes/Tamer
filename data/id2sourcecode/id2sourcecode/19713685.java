    public static String getSource(String urlS) {
        WGet wget = new WGet();
        wget.buffer = new StringBuffer();
        try {
            URLConnection url = (new URL(urlS.replaceAll(" ", "+"))).openConnection();
            if (url instanceof HttpURLConnection) {
                wget.readHttpURL((HttpURLConnection) url);
            } else {
                wget.readURL(url);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return wget.buffer.toString();
    }
