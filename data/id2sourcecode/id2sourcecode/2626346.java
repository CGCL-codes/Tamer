    public URL getResource(String name) {
        boolean fromParent = false;
        URL url = super.getResource(name);
        if (url == null && parentClassLoader != null) {
            url = parentClassLoader.getResource(name);
            fromParent = true;
        }
        if (url != null) {
            String jiopiName = name + ".jiopi";
            URL jiopiURL = null;
            if (!fromParent) {
                jiopiURL = super.getResource(jiopiName);
            } else {
                jiopiURL = parentClassLoader.getResource(jiopiName);
            }
            if (jiopiURL != null) {
                String fileName = new File(url.getFile()).getName();
                if (fileName.endsWith(".xml")) {
                    String nameMD5 = MD5Hash.digest(name).toString().toLowerCase();
                    String jiopiResourceFilePath = FileUtil.joinPath(tempDir, nameMD5, fileName);
                    File jiopiResourceFile = new File(jiopiResourceFilePath);
                    synchronized (jiopiResourceFilePath.intern()) {
                        if (!jiopiResourceFile.isFile()) {
                            try {
                                jiopiResourceFile = FileUtil.createNewFile(jiopiResourceFilePath, true);
                                FileContentReplacer.replaceAll(jiopiURL, jiopiResourceFile, new String[] { "\\$\\{jiopi-work-dir\\}" }, new String[] { workDir });
                            } catch (IOException e) {
                                LoaderLogUtil.logExceptionTrace(BootstrapConstants.bootstrapLogger, Level.WARNING, e);
                                return null;
                            }
                        }
                    }
                    if (jiopiResourceFile.isFile()) {
                        return FileUtil.toURL(jiopiResourceFilePath);
                    } else {
                        return null;
                    }
                }
            }
        }
        return url;
    }
