    public static void main(String[] args) throws IOException {
        InputStream inputStream = null;
        try {
            URL url = new URL("http://www.google.cn");
            inputStream = url.openStream();
            for (int c = inputStream.read(); c != -1; c = inputStream.read()) {
                System.out.write(c);
            }
            inputStream.close();
        } catch (MalformedURLException ex) {
            System.err.println("Not a URL Java understands.");
        } finally {
            if (inputStream != null) inputStream.close();
        }
    }
