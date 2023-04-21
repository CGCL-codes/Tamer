    public static DataSourceFactory getDataSourceFactory(URI uri, ProgressMonitor mon) throws IOException, IllegalArgumentException {
        if (isAggregating(uri.toString())) {
            return new AggregatingDataSourceFactory();
        }
        int i = uri.getScheme().indexOf(".");
        if (i != -1) {
            String ext = uri.getScheme().substring(0, i);
            return DataSourceRegistry.getInstance().getSource(ext);
        }
        URL url = uri.toURL();
        String file = url.getPath();
        i = file.lastIndexOf(".");
        String ext = i == -1 ? "" : file.substring(i);
        String surl = url.toString();
        i = surl.indexOf("?");
        if (i != -1) {
            i = surl.indexOf("?", i + 1);
            if (i != -1) {
                throw new IllegalArgumentException("too many ??'s!");
            }
        }
        DataSourceFactory factory = null;
        factory = DataSourceRegistry.getInstance().getSource(ext);
        if (factory == null && url.getProtocol().equals("http")) {
            mon.setTaskSize(-1);
            mon.started();
            mon.setProgressMessage("doing HEAD request to find dataset type");
            URLConnection c = url.openConnection();
            String mime = c.getContentType();
            if (mime == null) {
                throw new IOException("failed to connect");
            }
            String cd = c.getHeaderField("Content-Disposition");
            if (cd != null) {
                int i0 = cd.indexOf("filename=\"");
                i0 += "filename=\"".length();
                int i1 = cd.indexOf("\"", i0);
                String filename = cd.substring(i0, i1);
                i0 = filename.lastIndexOf(".");
                ext = filename.substring(i0);
            }
            mon.finished();
            factory = DataSourceRegistry.getInstance().getSourceByMime(mime);
        }
        if (factory == null) {
            if (ext.equals("")) {
                throw new NonResourceException("resource has no extension or mime type");
            } else {
                factory = DataSourceRegistry.getInstance().getSource(ext);
            }
        }
        if (factory == null) {
            throw new IllegalArgumentException("Unsupported extension: " + ext);
        }
        return factory;
    }
