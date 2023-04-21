    public static OutputStream getOutputStream(URL url) throws IOException {
        try {
            if (url.getProtocol().equals("file")) {
                return new FileOutputStream(new File(url.toURI()));
            } else {
                final URLConnection connection = url.openConnection();
                connection.setDoOutput(true);
                return connection.getOutputStream();
            }
        } catch (final URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
