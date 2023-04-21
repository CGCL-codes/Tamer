    private static InputStream getJIOPiJarXMLInputStream(URL jarURL) {
        File jarFile = new File(jarURL.getFile());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int jarType = AnnotationParser.parseJarFile(jarFile, out, false, true);
        if (jarType != ConfigConstants.UNKNOWN_RES) {
            if (out.size() < 1) {
                if (jarFile.isFile()) {
                    String xmlFileName = AnnotationParser.configFileNames[jarType];
                    String tempDirName = MD5Hash.digest(jarFile.getName()).toString();
                    String xmlFilePath = FileUtil.joinPath(jarFile.getParent(), tempDirName, xmlFileName);
                    File newXmlFile = new File(xmlFilePath);
                    boolean rebuild = true;
                    if (newXmlFile.exists()) {
                        rebuild = false;
                        if (newXmlFile.isFile()) {
                            if (jarFile.lastModified() != newXmlFile.lastModified()) {
                                rebuild = true;
                            }
                        }
                    }
                    if (rebuild) {
                        String tempFilePath = xmlFilePath + ".tmp";
                        File tmpFile = null;
                        MyFileLock fl = null;
                        BufferedOutputStream fos = null;
                        try {
                            tmpFile = FileUtil.createNewFile(tempFilePath, false);
                            if (tmpFile.isFile()) {
                                fl = FileUtil.tryLockTempFile(tmpFile, 100, ShareConstants.connectTimeout);
                                if (fl != null) {
                                    if (!newXmlFile.isFile() || jarFile.lastModified() != newXmlFile.lastModified()) {
                                        AnnotationParser.parseJarFile(jarFile, out, true, false);
                                        fos = new BufferedOutputStream(new FileOutputStream(newXmlFile));
                                        fos.write(out.toByteArray());
                                        IOUtil.close(fos);
                                        if (newXmlFile.isFile()) {
                                            newXmlFile.setLastModified(jarFile.lastModified());
                                            logger.info("build " + newXmlFile);
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            logger.error("", e);
                        } finally {
                            IOUtil.close(fos);
                            if (tmpFile != null) tmpFile.delete();
                            if (fl != null) try {
                                fl.release();
                            } catch (Exception e) {
                                logger.warn("", e);
                            }
                        }
                    } else {
                        FileInputStream fis = null;
                        try {
                            fis = new FileInputStream(newXmlFile);
                            IOUtil.copyStreams(fis, out);
                        } catch (Exception e) {
                            logger.error("read file error " + newXmlFile, e);
                        } finally {
                            IOUtil.close(fis);
                        }
                    }
                } else {
                    AnnotationParser.parseJarFile(jarFile, out, true, false);
                }
            }
            byte[] xmlFileContent = out.toByteArray();
            IOUtil.close(out);
            return new ByteArrayInputStream(xmlFileContent);
        }
        return null;
    }
