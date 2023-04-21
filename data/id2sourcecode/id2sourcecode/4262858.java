        public URL getResourceLocal(String name) {
            URL url = super.getResource(name);
            if (url != null) {
                if (name.endsWith(".xml") || name.endsWith(".properties")) {
                    String contextResourceName = new StringBuilder(name).append(".jiopi.").append(parentClassLoader.poolName).append(".").append(parentClassLoader.groupName).toString();
                    if (parentClassLoader.contextClassLoader != null) {
                        URL contextURL = parentClassLoader.contextClassLoader.getResource(contextResourceName);
                        if (contextURL != null) {
                            String nameMD5 = MD5Hash.digest(contextResourceName).toString().toLowerCase();
                            String jiopiResourceFilePath = FileUtil.joinPath(parentClassLoader.commonTempDir, parentClassLoader.poolName, parentClassLoader.groupName, nameMD5, name);
                            File jiopiResourceFile = new File(jiopiResourceFilePath);
                            synchronized (jiopiResourceFilePath.intern()) {
                                if (!jiopiResourceFile.isFile()) {
                                    try {
                                        jiopiResourceFile = FileUtil.createNewFile(jiopiResourceFilePath, true);
                                        FileContentReplacer.replaceAll(contextURL, jiopiResourceFile, new String[] {}, new String[] {});
                                    } catch (IOException e) {
                                        logger.warn("", e);
                                    }
                                }
                            }
                            if (jiopiResourceFile.isFile()) return FileUtil.toURL(jiopiResourceFilePath);
                        }
                    }
                }
                String fileName = new File(url.getFile()).getName();
                if (fileName.endsWith(".xml") || name.endsWith(".properties")) {
                    String jiopiName = name + ".jiopi";
                    URL jiopiURL = super.getResource(jiopiName);
                    if (jiopiURL != null) {
                        String nameMD5 = MD5Hash.digest(name).toString().toLowerCase();
                        String jiopiResourceFilePath = FileUtil.joinPath(parentClassLoader.commonTempDir, parentClassLoader.poolName, parentClassLoader.groupName, nameMD5, fileName);
                        File jiopiResourceFile = new File(jiopiResourceFilePath);
                        synchronized (jiopiResourceFilePath.intern()) {
                            if (!jiopiResourceFile.isFile()) {
                                try {
                                    jiopiResourceFile = FileUtil.createNewFile(jiopiResourceFilePath, true);
                                    String commonDir = FileUtil.joinPath(this.parentClassLoader.commonDir, parentClassLoader.poolName, parentClassLoader.groupName);
                                    String commonTempDir = FileUtil.joinPath(this.parentClassLoader.commonTempDir, parentClassLoader.poolName, parentClassLoader.groupName);
                                    FileUtil.confirmDir(commonDir, true);
                                    FileUtil.confirmDir(commonTempDir, true);
                                    FileContentReplacer.replaceAll(jiopiURL, jiopiResourceFile, new String[] { "\\$\\{common-dir\\}", "\\$\\{common-temp-dir\\}" }, new String[] { commonDir, commonTempDir });
                                } catch (IOException e) {
                                    logger.warn("", e);
                                }
                            }
                        }
                        if (jiopiResourceFile.isFile()) return FileUtil.toURL(jiopiResourceFilePath);
                    }
                }
            }
            return url;
        }
