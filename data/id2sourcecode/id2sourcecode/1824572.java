    public static String getHttpGetRequestResponse(String endpoint, Map<String, String> params) {
        String address = UrlBuilder(endpoint, params);
        String data = null;
        try {
            URL url = new URL(address);
            URLConnection ucon = url.openConnection();
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayBuffer baf = new ByteArrayBuffer(50);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }
            data = new String(baf.toByteArray());
        } catch (Exception e) {
            Log.e("AndroidIOUtil.java", "I/O Exception: Error in getting HTTP Request response.");
        }
        return data;
    }
