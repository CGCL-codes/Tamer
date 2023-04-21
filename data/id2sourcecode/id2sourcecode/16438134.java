    private InputStream openInputStream(final URL url) throws ActivityUserException {
        InputStream input = null;
        try {
            final URLConnection connection = url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(false);
            input = connection.getInputStream();
            return input;
        } catch (IOException e) {
            throw new ActivityUserException(new ActivityIOException(e));
        }
    }
