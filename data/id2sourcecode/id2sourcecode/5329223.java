        @SuppressWarnings("unchecked")
        public synchronized URL[] getResources() {
            if (this.resources == null) {
                if (release == null) initRelease();
                if (release == null) return null;
                List<Element> resources = release.elements("resource");
                ArrayList<URL> resourceURLList = new ArrayList<URL>();
                for (Element resource : resources) {
                    String resourcePath = resource.getTextTrim();
                    try {
                        URL resourceURL = baseURI.resolve(resourcePath).toURL();
                        resourceURLList.add(resourceURL);
                    } catch (MalformedURLException e) {
                        logger.error(e);
                    }
                }
                ArrayList<URL> localResourceURLList = new ArrayList<URL>();
                String releaseDirPath = FileUtil.joinPath(moduleDirPath, this.version);
                File cacheDir = FileUtil.confirmDir(releaseDirPath, true);
                File confDir = null;
                for (URL remote : resourceURLList) {
                    File localFile = RemoteFileManager.getRemoteFile(remote, cacheDir, creds);
                    if (localFile.isFile()) {
                        if (localFile.getName().endsWith(".jar")) {
                            localResourceURLList.add(FileUtil.toURL(localFile.getAbsolutePath()));
                        } else {
                            if (confDir == null) {
                                String confDirPath = FileUtil.joinPath(releaseDirPath, "conf");
                                confDir = FileUtil.confirmDir(confDirPath, true);
                                if (confDir.isDirectory()) {
                                    localResourceURLList.add(FileUtil.toURL(confDir.getAbsolutePath()));
                                }
                            }
                            if (confDir.isDirectory()) {
                                File newFile = new File(confDir, new File(remote.getFile()).getName());
                                try {
                                    IOUtil.copyFile(localFile, newFile);
                                } catch (IOException e) {
                                    logger.error("", e);
                                }
                            }
                        }
                    } else logger.warn("can't load file :" + remote);
                }
                this.resources = localResourceURLList.toArray(new URL[localResourceURLList.size()]);
            }
            return this.resources;
        }
