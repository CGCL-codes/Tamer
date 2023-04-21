    public String doPostObject(String URI, String data) {
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
            Log.d(TAG + "Flow   doPostObject   ///", "Encode = " + writer.getEncoding() + "\n XML = \n" + data + "\n");
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK || conn.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT) {
                Log.d(TAG + "Flow   doPostObject   ///", "[doPost] Successfully Posted");
                Log.d(TAG + "Flow", "[doPostObject] from Post / show = " + conn.getResponseCode() + " message = " + conn.getResponseMessage());
                return conn.getResponseMessage();
            } else {
                Log.d(TAG + "Flow", "[doPostObject] Failed to Post / Err = " + conn.getResponseCode() + " message = " + conn.getResponseMessage());
            }
        } catch (MalformedURLException mfe) {
            mfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return "error";
    }
