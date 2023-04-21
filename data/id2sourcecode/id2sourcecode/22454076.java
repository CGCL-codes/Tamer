    protected void loadResourceLocations() {
        try {
            for (String path : resourceLocations) {
                FileObject fo = VFSUtils.resolveFile(path);
                if (fo.exists()) {
                    URL url = fo.getURL();
                    url.openConnection();
                    if (fastDeploy) {
                        if (log.isDebugEnabled()) {
                            log.debug("Fast deploy : " + url);
                            AdminSqlQueryFactory builder = null;
                            for (DirectoryListener listener : scanner.getDirectoryListeners()) {
                                if (listener instanceof AdminSqlQueryFactory) {
                                    builder = (AdminSqlQueryFactory) listener;
                                }
                            }
                            File file = new File(url.getFile());
                            fastDeploy(file, builder);
                        }
                    }
                    scanner.addScanURL(url);
                }
            }
        } catch (Exception e) {
        }
    }
