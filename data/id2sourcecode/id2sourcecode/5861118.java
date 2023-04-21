    public InputStream doPost(String URI, String data) {
        URL url = null;
        conn = null;
        try {
            url = new URL(URI);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/xml");
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(data);
            writer.flush();
            Log.d(TAG + "Flow   doPost   ///", "Encode = " + writer.getEncoding() + "\n XML = \n" + data + "\n");
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK || conn.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT) {
                Log.d(TAG + "Flow   doPost   ///", "[doPost] Successfully Posted");
                Log.d(TAG + "Flow", "[doPost] from Post / show = " + conn.getResponseCode() + " message = " + conn.getResponseMessage());
                return conn.getInputStream();
            } else {
                Log.d(TAG + "Flow", "[doPost] Failed to Post / Err = " + conn.getResponseCode() + " message = " + conn.getResponseMessage());
            }
        } catch (MalformedURLException mfe) {
            mfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }
