    public static DataSourceFactory getDataSourceFactory(URI uri, ProgressMonitor mon) throws IOException, IllegalArgumentException {
        String suri = DataSetURI.fromUri(uri);
        if (isAggregating(suri)) {
            String eext = DataSetURI.getExplicitExt(suri);
            if (eext != null) {
                DataSourceFactory delegateFactory = DataSourceRegistry.getInstance().getSource(eext);
                AggregatingDataSourceFactory factory = new AggregatingDataSourceFactory();
                factory.setDelegateDataSourceFactory(delegateFactory);
                return factory;
            } else {
                return new AggregatingDataSourceFactory();
            }
        }
        String ext = DataSetURI.getExplicitExt(suri);
        if (ext != null && !suri.startsWith("vap+X:")) {
            return DataSourceRegistry.getInstance().getSource(ext);
        }
        URI resourceUri;
        try {
            String resourceSuri = uri.getRawSchemeSpecificPart();
            resourceUri = new URI(resourceSuri);
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
        ext = DataSetURI.getExt(uri.toASCIIString());
        if (ext == null) ext = "";
        DataSourceFactory factory = null;
        factory = DataSourceRegistry.getInstance().getSource(ext);
        if (factory == null && (resourceUri.getScheme().equals("http") || resourceUri.getScheme().equals("https"))) {
            URL url = resourceUri.toURL();
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
            if (ext.equals("") || ext.equals("X")) {
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
