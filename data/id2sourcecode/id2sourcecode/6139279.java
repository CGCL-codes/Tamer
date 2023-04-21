    public InputStream obtain(String filename) throws ActivityProcessingException, ActivityTerminatedException, ActivityUserException {
        final URL url = DeliveryUtilities.createURL("http", mCurrentHost, filename);
        URLConnection connect;
        InputStream stream = null;
        try {
            connect = url.openConnection();
            connect.connect();
            stream = connect.getInputStream();
            return stream;
        } catch (IOException e) {
            throw new ActivityIOException(e);
        }
    }
