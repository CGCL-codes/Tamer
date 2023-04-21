    public static void downloadFile(final HttpServletResponse response, final URL url) throws IOException {
        Assert.notNull(response, "response");
        Assert.notNull(url, "url");
        InputStream inputStream = url.openStream();
        byte[] bytes = IOUtils.toByteArray(inputStream);
        String fileName = FileUtils.getName(url);
        ResponseUtils.downloadFile(response, bytes, fileName);
    }
