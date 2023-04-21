    public final void convertToPdf(final URL url, final IHtmlToPdfTransformer.PageSize size, final List hf, final OutputStream out, final Map fproperties) throws CConvertException {
        Map properties = (fproperties != null) ? fproperties : new HashMap();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        int priority = Thread.currentThread().getPriority();
        try {
            Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[0], this.useClassLoader ? CClassLoader.getLoader("/main") : this.getClass().getClassLoader()));
            Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
            String uri = url.toExternalForm();
            if (uri.indexOf("://") != -1) {
                String tmp = uri.substring(uri.indexOf("://") + 3);
                if (tmp.indexOf('/') == -1) {
                    uri += "/";
                }
            }
            uri = uri.substring(0, uri.lastIndexOf("/") + 1);
            if (uri.startsWith("file:/")) {
                uri = uri.substring(6);
                while (uri.startsWith("/")) {
                    uri = uri.substring(1);
                }
                uri = "file:///" + uri;
            }
            try {
                IHtmlToPdfTransformer transformer = getTransformer(properties);
                transformer.transform(url.openStream(), uri, size, hf, properties, out);
            } catch (final CConvertException e) {
                throw e;
            } catch (final Exception e) {
                throw new CConvertException(e.getMessage(), e);
            }
        } finally {
            Thread.currentThread().setContextClassLoader(loader);
            Thread.currentThread().setPriority(priority);
        }
    }
