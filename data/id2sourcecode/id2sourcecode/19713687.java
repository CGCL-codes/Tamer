    public static Byte[] getSource(String urlS, String[] keys, String[][] values, WGetResult result) {
        WGet wget = new WGet();
        wget.buffer = new StringBuffer();
        try {
            URLConnection url = (new URL(urlS.replaceAll(" ", "+"))).openConnection();
            if (url instanceof HttpURLConnection) {
                wget.readHttpURL((HttpURLConnection) url, keys, values, result);
            } else {
                wget.readURL(url);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return wget.bytes.toArray(new Byte[wget.bytes.size()]);
    }
