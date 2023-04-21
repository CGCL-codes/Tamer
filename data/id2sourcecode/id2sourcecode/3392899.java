    protected InputStream getActualInputStream() {
        InputStream is = null;
        URLConnection connection = null;
        try {
            URL url = URI.create(getLocation()).toURL();
            connection = url.openConnection();
            connection.connect();
            InputStream bytesInput = connection.getInputStream();
            byte[] bytes = IOUtils.toByteArray(bytesInput);
            is = new ByteArrayInputStream(bytes);
        } catch (MalformedURLException e) {
            cleanUpHttpURLConnection(connection);
            throw new DbConException("Detected a malformed URL; aborting", e);
        } catch (FileNotFoundException e) {
            cleanUpHttpURLConnection(connection);
            throw new DbConException("Could not find the specified file at the given URL", e);
        } catch (IOException e) {
            cleanUpHttpURLConnection(connection);
            throw new DbConException("Detected IO problems whilst reading URL's content", e);
        }
        return is;
    }
