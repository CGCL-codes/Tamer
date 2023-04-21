    public byte[] readResource(String resource, boolean classResource) throws Exception {
        URL url = locationOf(resource, classResource);
        URLConnection connection = url.openConnection();
        InputStream in = connection.getInputStream();
        byte[] contents = new byte[connection.getContentLength()];
        in.read(contents);
        return contents;
    }
