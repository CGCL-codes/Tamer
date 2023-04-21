    public void openConnection(String urlString) {
        try {
            url = new URL(urlString);
            URLConnection con = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) con;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println(this.getClass().getName() + ": HTTP Connection OK");
            }
            grabHttp();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
