    private static String readResource(URL url) throws IOException {
        return FileCopyUtils.copyToString(new InputStreamReader(url.openStream()));
    }
