    public static void copyStream(OutputStream outputStream, InputStream inputStream) {
        int c;
        try {
            while ((c = inputStream.read()) != -1) outputStream.write(c);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
