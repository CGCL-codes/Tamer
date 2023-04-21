    @Override
    public void update(final ModelUpdater document, final Platform platform, final File dest) throws IOException {
        if (log.isDebugEnabled()) log.debug(HelperLog.methodStart(document, platform, dest));
        if (null == document) {
            throw new RuntimeExceptionIsNull("document");
        }
        if (null == platform) {
            throw new RuntimeExceptionIsNull("platform");
        }
        if (null == dest) {
            throw new RuntimeExceptionIsNull("dest");
        }
        URL location = document.getLocation(platform);
        if (null == location && Platform.ANY != platform) {
            location = document.getLocation(Platform.ANY);
        }
        if (null == location) {
            throw new IllegalArgumentException("no valid location found");
        }
        final File source = new File(location.getFile());
        if (source.exists()) {
            if (HelperObject.isEquals(source, dest)) {
                throw new RuntimeExceptionIsEquals("location", "dest");
            }
            HelperIO.copy(source, dest);
        } else {
            HelperIO.writeFile(dest, HelperNet.readUrl(location), false);
        }
        if (log.isDebugEnabled()) log.debug(HelperLog.methodExit());
    }
