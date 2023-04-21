    public List<URL> getResources(String name, boolean returnAfterFoundFirst) throws IOException {
        if (remoteBusy_res.get().booleanValue()) return null;
        boolean debug = logger.isDebugEnabled();
        List<URL> resources = new LinkedList<URL>();
        long totalStart = debug ? System.currentTimeMillis() : 0;
        remoteBusy_res.set(Boolean.TRUE);
        try {
            try {
                if (name.startsWith("/")) name = name.substring(1);
                List<ResourceMetaData> rmds = getResourcesMetaData(name);
                if (rmds == null) {
                    if (debug) logger.debug("getResources: Resource \"" + name + "\" not found in ResourcesMetaData! Returning null.");
                    return null;
                }
                if (filter != null && !filter.includeResource(name)) {
                    if (debug) logger.debug("getResources: Resource \"" + name + "\" was excluded by filter \"" + filter + "\"! Returning null.");
                    return null;
                }
                JFireRCLBackend jfireRCLBackend = null;
                try {
                    for (ResourceMetaData rmd : rmds) {
                        File localRes = new File(cacheDir, rmd.getRepositoryName() + File.separatorChar + rmd.getJar() + File.separatorChar + name);
                        if (!isLocalResourceOutOfSync(localRes, rmd)) {
                            if (debug) logger.debug("getResources: Locally cached resource is up-to-date (no need to download): " + localRes.getAbsolutePath());
                        } else {
                            File dir = localRes.getParentFile();
                            if (!dir.exists() && !dir.mkdirs()) logger.error("Creating directory \"" + localRes.getParent() + "\" failed!");
                            if (jfireRCLBackend == null) {
                                long obtainBeanStart = debug ? System.currentTimeMillis() : 0;
                                jfireRCLBackend = acquireJFireRCLBackend();
                                if (debug) logger.debug("getResources: Obtaining EJB proxy JFireRCLBackend took " + (System.currentTimeMillis() - obtainBeanStart) + " msec.");
                            }
                            long downloadStart = debug ? System.currentTimeMillis() : 0;
                            byte[] res = jfireRCLBackend.getResourceBytes(rmd);
                            if (debug) logger.debug("getResources: Downloading resource from server took " + (System.currentTimeMillis() - downloadStart) + " msec: " + localRes.getAbsolutePath());
                            synchronized (getResourceMutex(localRes)) {
                                if (!isLocalResourceOutOfSync(localRes, rmd)) logger.info("The resource has been downloaded by another thread in the meantime! Will not write it again: " + localRes); else {
                                    File tmpFile = File.createTempFile(localRes.getName(), ".tmp", localRes.getParentFile());
                                    FileOutputStream fo = new FileOutputStream(tmpFile);
                                    try {
                                        fo.write(res);
                                    } finally {
                                        fo.close();
                                    }
                                    tmpFile.setLastModified(rmd.getTimestamp());
                                    long start = System.currentTimeMillis();
                                    while (true) {
                                        if (System.currentTimeMillis() - start > 20000) throw new IOException("Could not delete the resource within timeout: " + localRes.getAbsolutePath());
                                        if (!localRes.exists()) break;
                                        if (!localRes.delete()) {
                                            try {
                                                Thread.sleep(500);
                                            } catch (InterruptedException x) {
                                            }
                                            continue;
                                        }
                                    }
                                    if (!tmpFile.renameTo(localRes)) {
                                        if (!localRes.exists()) throw new IOException("Could not replace the resource \"" + localRes.getAbsolutePath() + "\" by the new version \"" + tmpFile.getAbsolutePath() + "\"! Renaming failed and the resource does not exist now!");
                                        if (localRes.exists() && tmpFile.exists()) throw new IOException("Could not replace the resource \"" + localRes.getAbsolutePath() + "\" by the new version \"" + tmpFile.getAbsolutePath() + "\"! But the resource exists - hence it was probably created by another process!");
                                        if (localRes.exists() && !tmpFile.exists()) logger.warn("Renaming the temporary file \"" + tmpFile.getAbsolutePath() + "\" to the resource \"" + localRes.getAbsolutePath() + "\" failed, but the resource now exists and the temporary file does not. So it seems to have succeeded."); else throw new IllegalStateException("This should never happen!!!");
                                    }
                                }
                            }
                        }
                        resources.add(localRes.toURI().toURL());
                        if (returnAfterFoundFirst) break;
                    }
                } finally {
                    if (jfireRCLBackend != null) releaseJFireRCLBackend(jfireRCLBackend);
                }
                return resources;
            } catch (IOException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } finally {
            remoteBusy_res.set(Boolean.FALSE);
            if (debug) logger.debug("getResources: Resource \"" + name + "\": total duration: " + (System.currentTimeMillis() - totalStart) + " msec.");
        }
    }
