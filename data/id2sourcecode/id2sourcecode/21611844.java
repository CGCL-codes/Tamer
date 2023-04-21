    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("Test", "[*] SendDataService()");
        URL url = null;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL("http://pjlantz.com/data.php?data=Hello");
            urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
            while ((line = rd.readLine()) != null) ;
            rd.close();
            urlConnection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
