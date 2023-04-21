    private void downloadPlugin(InstallInfo ii) throws FileNotFoundException, IOException {
        URL url = new URL(pluginURLBase + '/' + ii.getURL());
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(baseDir + SystemUtils.FILE_SEPARATOR + "temp" + SystemUtils.FILE_SEPARATOR + ii.getId() + ".tmp");
        copyStream(is, os);
    }
