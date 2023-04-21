    public static final StringBuffer post(URL url, StringBuffer data, String contentType) throws IOException, MalformedURLException {
        StringBuffer result;
        DataOutputStream streamOut = null;
        BufferedReader streamIn = null;
        try {
            URLConnection conn = url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-Type", contentType);
            streamOut = new DataOutputStream(new java.io.BufferedOutputStream(conn.getOutputStream()));
            streamOut.writeBytes(data.toString());
            streamOut.flush();
            streamOut.close();
            streamOut = null;
            result = new StringBuffer();
            String str = null;
            streamIn = new java.io.BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((str = streamIn.readLine()) != null) {
                result.append(str);
            }
            streamIn.close();
        } catch (MalformedURLException murle) {
            throw murle;
        } catch (IOException ioe) {
            throw ioe;
        } finally {
            try {
                if (streamIn != null) {
                    streamIn.close();
                }
            } catch (Exception ein) {
            }
            try {
                if (streamOut != null) {
                    streamOut.close();
                }
            } catch (Exception eout) {
            }
        }
        return result;
    }
