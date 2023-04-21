    protected URLConnection openConnection(URL url) throws IOException {
        if (!StringUtils.hasText(url.getPath())) {
            throw new MalformedURLException("must provide an image key.");
        } else if (StringUtils.hasText(url.getHost())) {
            throw new MalformedURLException("host part should be empty.");
        } else if (url.getPort() != -1) {
            throw new MalformedURLException("port part should be empty.");
        } else if (StringUtils.hasText(url.getQuery())) {
            throw new MalformedURLException("query part should be empty.");
        } else if (StringUtils.hasText(url.getRef())) {
            throw new MalformedURLException("ref part should be empty.");
        } else if (StringUtils.hasText(url.getUserInfo())) {
            throw new MalformedURLException("user info part should be empty.");
        }
        urlHandlerImageSource.getImage(url.getPath());
        Resource image = urlHandlerImageSource.getImageResource(url.getPath());
        if (image != null) {
            return image.getURL().openConnection();
        } else {
            throw new IOException("null image returned for key [" + url.getFile() + "].");
        }
    }
