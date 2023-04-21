    public void processFromHref(final String href, final ReadingProcessor processor) throws IOException {
        if (LOGGER.isLogging(Level.DEBUG)) {
            LOGGER.debug(String.format(LocaleMessages.getInstance().getMessage("retrieve.file.from"), href));
        }
        URL url = null;
        File f = null;
        boolean isfile = false;
        try {
            url = new URL(href);
        } catch (MalformedURLException e) {
            try {
                url = detectWithRootUrls(href);
            } catch (MalformedURLException e1) {
                f = new File(href);
                isfile = true;
                if (!(f.isFile() && f.canRead())) {
                    isfile = false;
                    for (File root : rootdirs) {
                        f = new File(root, href);
                        if (f.isFile() && f.canRead()) {
                            isfile = true;
                            break;
                        }
                    }
                }
            }
        }
        InputStream in = null;
        if (null != url) {
            in = url.openStream();
        } else if (isfile) {
            in = new FileInputStream(f);
        } else {
            throw new IOException(LocaleMessages.getInstance().getMessage("retrieve.file.from.nothing"));
        }
        read(processor, in);
    }
