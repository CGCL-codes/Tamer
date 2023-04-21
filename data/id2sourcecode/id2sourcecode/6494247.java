    private static Properties load(URL url) {
        Properties props = new Properties();
        try {
            InputStream is = null;
            try {
                is = url.openStream();
                props.load(is);
            } finally {
                if (is != null) is.close();
            }
        } catch (IOException e) {
            String message = NLS.bind(Messages.exception_missingFile, url.toExternalForm());
            BundleHelper.getDefault().getLog().log(new Status(IStatus.WARNING, IPDEBuildConstants.PI_PDEBUILD, IPDEBuildConstants.EXCEPTION_READING_FILE, message, null));
        }
        return props;
    }
