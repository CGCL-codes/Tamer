    private static boolean valid(URL url) {
        try {
            int responseCode = ((HttpURLConnection) url.openConnection()).getResponseCode();
            if (responseCode == 200) {
                return true;
            }
            if (responseCode == 404) {
                return false;
            }
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return false;
    }
