    public static String readString(URL url) throws IOException {
        BufferedReader in = null;
        StringBuffer buf = new StringBuffer("");
        try {
            URLConnection urlConnection = url.openConnection();
            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                buf.append(inputLine);
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return buf.toString();
    }
