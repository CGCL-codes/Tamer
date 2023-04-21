    public static byte[] decodeAndUnzip(String data) {
        byte[] decoded = Base64.decode(data, Base64.DEFAULT);
        ByteArrayOutputStream unzipped = new ByteArrayOutputStream();
        byte[] buffer = new byte[512];
        int count;
        try {
            GZIPInputStream zis = new GZIPInputStream(new ByteArrayInputStream(decoded));
            while ((count = zis.read(buffer)) != -1) unzipped.write(buffer, 0, count);
            unzipped.flush();
            zis.close();
        } catch (IOException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
        return unzipped.toByteArray();
    }
