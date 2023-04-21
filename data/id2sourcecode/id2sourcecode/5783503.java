    public Result getPage() {
        final URLConnection openConnection;
        final InputStream in;
        try {
            openConnection = new URL(url).openConnection();
            in = openConnection.getInputStream();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            return new Result(IOUtils.toString(in), openConnection.getContentType());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
