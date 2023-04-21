    public void executeMethod(Hashtable<String, String> data) {
        URL url;
        HttpURLConnection connection = null;
        try {
            url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            String urlParameters = getUrlParameters(data);
            connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            if (cookie != null) {
                connection.setRequestProperty("Cookie", cookie);
            }
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            int i = 1;
            String hdrKey = null;
            while ((hdrKey = connection.getHeaderFieldKey(i)) != null) {
                if (hdrKey.equals("Set-Cookie")) {
                    cookie = connection.getHeaderField(i);
                }
                i++;
            }
            InputStream is = connection.getInputStream();
            lastResult = File.createTempFile("cached", "horizon");
            FileOutputStream out = new FileOutputStream(lastResult);
            final int BUFFER_SIZE = 1 << 10 << 6;
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;
            while ((bytesRead = is.read(buffer)) > -1) {
                out.write(buffer, 0, bytesRead);
            }
            is.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
