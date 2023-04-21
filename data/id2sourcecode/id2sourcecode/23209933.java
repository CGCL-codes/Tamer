    public CrueIOResu<CrueConfigMetier> readConfigMetier(final URL url) {
        CrueIOResu<CrueConfigMetier> newData = new CrueIOResu<CrueConfigMetier>();
        final CtuluLog analyser = new CtuluLog(BusinessMessages.RESOURCE_BUNDLE);
        newData.setAnalyse(analyser);
        if (url == null) {
            analyser.addError("file.url.null.error");
            return newData;
        }
        String readVersion = new XmlVersionFinder().getVersion(url);
        if (!readVersion.equals(coeurConfig.getXsdVersion())) {
            analyser.addFatalError("configFile.versionNotCompatible", url.getFile(), readVersion, coeurConfig.getXsdVersion());
            return newData;
        }
        InputStream in = null;
        CrueConfigMetier res = null;
        try {
            in = url.openStream();
            res = readConfigMetier(in, analyser);
        } catch (final IOException e) {
            LOGGER.log(Level.FINE, e.getMessage(), e);
            analyser.addError("io.xml.error", e.getMessage());
        } finally {
            CtuluLibFile.close(in);
        }
        newData.setMetier(res);
        return newData;
    }
