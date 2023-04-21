    public URL getLocalResource(String name) {
        URL url = null;
        ClassLoader loader = null;
        if (url == null && jiopiClassLoader != null) {
            url = jiopiClassLoader.getResourceLocal(name);
            if (url != null) loader = jiopiClassLoader;
        }
        if (url == null && localClassLoader != null) {
            url = localClassLoader.getResourceLocal(name);
            if (url != null) loader = localClassLoader;
        }
        if (loader != null && url != null && this.tempDir != null) {
            String fileName = new File(url.getFile()).getName();
            if (fileName.endsWith(".xml") || name.endsWith(".properties")) {
                if (this.jiopiClassLoader != null && this.jiopiClassLoader.moduleJar != null && this.jiopiClassLoader.moduleJar.jarType == ConfigConstants.MODULE_JAR) {
                    if (ccck.contextClassLoader != null) {
                        String contextResourceName = new StringBuilder(name).append(".jiopi.").append(this.name.name).toString();
                        URL contextURL = ccck.contextClassLoader.getResource(contextResourceName);
                        if (contextURL != null) {
                            String[] version = this.name.version.split("\\.");
                            StringBuilder sb = new StringBuilder(contextResourceName);
                            for (String v : version) {
                                sb.append(".").append(v);
                                URL testLoad = ccck.contextClassLoader.getResource(sb.toString());
                                if (testLoad != null) contextURL = testLoad;
                            }
                        }
                        if (contextURL != null) {
                            String nameMD5 = MD5Hash.digest(contextResourceName).toString().toLowerCase();
                            String jiopiResourceFilePath = FileUtil.joinPath(tempDir, nameMD5, name);
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
                String jiopiName = name + ".jiopi";
                URL jiopiURL = loader.getResource(jiopiName);
                if (jiopiURL != null) {
                    String nameMD5 = MD5Hash.digest(name).toString().toLowerCase();
                    String jiopiResourceFilePath = FileUtil.joinPath(tempDir, nameMD5, fileName);
                    File jiopiResourceFile = new File(jiopiResourceFilePath);
                    synchronized (jiopiResourceFilePath.intern()) {
                        if (!jiopiResourceFile.isFile()) {
                            try {
                                jiopiResourceFile = FileUtil.createNewFile(jiopiResourceFilePath, true);
                                String moduleDir = FileUtil.joinPath(this.moduleDir, "module");
                                String moduleTempDir = FileUtil.joinPath(this.tempDir, "module");
                                FileUtil.confirmDir(moduleDir, true);
                                FileUtil.confirmDir(moduleTempDir, true);
                                FileContentReplacer.replaceAll(jiopiURL, jiopiResourceFile, new String[] { "\\$\\{module-dir\\}", "\\$\\{module-temp-dir\\}" }, new String[] { moduleDir, moduleTempDir });
                            } catch (IOException e) {
                                logger.warn("", e);
                            }
                        }
                    }
                    if (jiopiResourceFile.isFile()) return FileUtil.toURL(jiopiResourceFilePath);
                }
            }
        }
        if (url == null && name.endsWith(".class")) {
            HashSet<IBeanClassLoader> loadedClassLoaders = null;
            boolean clear = false;
            if (url == null) {
                loadedClassLoaders = getLoadedClassLoaders(name);
                if (loadedClassLoaders.size() == 0) clear = true;
                loadedClassLoaders.add(this);
            }
            try {
                if (url == null && dependentBlueprintClassLoaders != null) {
                    for (IBeanClassLoader dependentBlueprint : dependentBlueprintClassLoaders) {
                        if (!loadedClassLoaders.contains(dependentBlueprint)) {
                            url = dependentBlueprint.getLocalResource(name);
                            if (url != null) break;
                        }
                    }
                }
            } finally {
                if (clear) {
                    loadedClassLoaders.clear();
                }
            }
        }
        if (url == null) {
            for (CommonLibClassLoader clcl : commonJarList.keySet()) {
                url = clcl.getResource(name, commonJarList.get(clcl));
                if (url != null) break;
            }
        }
        return url;
    }
