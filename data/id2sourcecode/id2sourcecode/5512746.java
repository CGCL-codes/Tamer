    private OutputStream openOutputStream(final URL url) throws ActivityUserException {
        OutputStream output = null;
        try {
            final URLConnection connection = url.openConnection();
            connection.setDoInput(false);
            connection.setDoOutput(true);
            output = connection.getOutputStream();
        } catch (IOException e) {
            throw new ActivityUserException(e);
        }
        return output;
    }
