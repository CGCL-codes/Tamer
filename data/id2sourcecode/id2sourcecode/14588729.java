    public static String check(String urlvalue) {
        try {
            URL url = new URL(urlvalue);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            return in.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
