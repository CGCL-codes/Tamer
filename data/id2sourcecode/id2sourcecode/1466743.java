    public static String getPayLoad(String urlAsString) {
        String result = null;
        InputStream inputStream = null;
        URL url;
        try {
            url = new URL(urlAsString);
            final HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setConnectTimeout(60000);
            httpConnection.setReadTimeout(60000);
            inputStream = httpConnection.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            copy(inputStream, bos);
            result = new String(bos.toByteArray());
            inputStream.close();
            httpConnection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.warn("Failed to connect at " + (new Date()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
