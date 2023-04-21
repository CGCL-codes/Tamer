    public static boolean isFilePresent(String imageUrl) {
        BufferedReader in = null;
        try {
            URL url = new URL(imageUrl);
            URLConnection urlConn = url.openConnection();
            int responseCode = ((HttpURLConnection) urlConn).getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                System.out.println("file not found: " + imageUrl + " HTTP Error " + responseCode + "  " + ((HttpURLConnection) urlConn).getResponseMessage());
                return false;
            }
            urlConn.getContent();
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            return true;
        } catch (FileNotFoundException fnfe) {
            return false;
        } catch (IOException fnfe) {
            return false;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception ex) {
                }
            }
        }
    }
