    protected URLConnection getURLConnection(String originalPath, boolean virtual) throws IOException {
        ServletContextAndPath csAndP = getServletContextAndPath(originalPath, virtual);
        ServletContext context = csAndP.getServletContext();
        String path = csAndP.getPath();
        URL url = context.getResource(path);
        if (url == null) {
            throw new IOException("Context did not contain resource: " + path);
        }
        URLConnection urlConnection = url.openConnection();
        return urlConnection;
    }
