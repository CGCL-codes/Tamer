    protected ArrayList filter(final URL[] urls) {
        ArrayList list;
        URL url;
        String ref;
        list = new ArrayList();
        for (int i = 0; i < urls.length; i++) {
            url = urls[i];
            ref = url.toExternalForm();
            if (!mDiscardCGI || (-1 == ref.indexOf("/cgi-bin/"))) if (!mDiscardQueries || (-1 == ref.indexOf("?"))) if (!mVisited.containsKey(ref)) {
                try {
                    url.openConnection();
                    list.add(url);
                } catch (IOException ioe) {
                }
            }
        }
        return (list);
    }
