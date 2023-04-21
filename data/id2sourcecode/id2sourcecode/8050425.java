    private static byte[] fetchImage(String urlString) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            URL url = new URL(urlString);
            InputStream is = url.openStream();
            for (int c = is.read(); c != -1; c = is.read()) baos.write(c);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }
