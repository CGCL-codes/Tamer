    public static void removeFromCache(String cacheName, Object key) {
        URL url = null;
        HttpURLConnection connection = null;
        InputStream is = null;
        OutputStream os = null;
        try {
            url = new URL(EHCACHE_SERVER_BASE + "/" + cacheName + "/" + key);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "text/plain");
            connection.setDoOutput(true);
            connection.setRequestMethod("DELETE");
            connection.connect();
            String sampleData = "";
            byte[] sampleBytes = sampleData.getBytes("UTF-8");
            os = connection.getOutputStream();
            os.write(sampleBytes, 0, sampleBytes.length);
            os.flush();
            if (connection != null) {
                connection.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (Exception ignore) {
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (Exception ignore) {
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
