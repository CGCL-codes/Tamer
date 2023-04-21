    @Suppress
    public void testMalformedUrl() throws Exception {
        URL url = new URL("http://www.google.com/cgi-bin/myscript?g={United+States}+Borders+Mexico+{Climate+change}+Marketing+{Automotive+industry}+News+Health+Internet");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        int status = conn.getResponseCode();
        android.util.Log.d("URLTest", "status: " + status);
    }
