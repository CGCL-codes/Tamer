    private static void updateStream(InputStream is, OutputStream os, int bufferSize) {
        byte[] buffer = new byte[bufferSize];
        try {
            int read = -1;
            if ((read = is.read(buffer)) != -1) {
                os.write(buffer, 0, read);
                os.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
