    private byte[] getImageData(String oldImageUrl) throws Exception {
        log.entering(UpdateTask.class.getName(), "getImagedata");
        byte[] oldImageData = null;
        final URL url = new URL(oldImageUrl);
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        final InputStream inputStream = connection.getInputStream();
        int available = inputStream.available();
        oldImageData = new byte[available];
        int read = inputStream.read();
        int i = 0;
        while (read != -1 && i < available) {
            oldImageData[i] = (byte) read;
            read = inputStream.read();
            i++;
        }
        inputStream.close();
        log.exiting(UpdateTask.class.getName(), "getImagedata");
        return oldImageData;
    }
