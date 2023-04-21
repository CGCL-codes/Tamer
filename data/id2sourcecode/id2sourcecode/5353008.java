    private boolean updateReportJar(String name, File jarFilePath, JarFile reportjar, Long lastModified, boolean weakCheck) {
        this.reportWorkingDirs = null;
        try {
            Manifest mf = reportjar.getManifest();
            HashMap reportDescription = new HashMap();
            reportDescription.put(REPORTJAR_ENTRY_LASTMODIFIED, lastModified);
            Attributes reportAttributes = mf.getAttributes(MANIFESTFILE_SECTION);
            if (reportAttributes == null) {
                logger.error("Got a Jar to handle in 'checkReportsJar' without a '" + MANIFESTFILE_SECTION + "' section in the manifest! (" + jarFilePath + ")");
                return false;
            }
            Iterator reportAttributesIterator = reportAttributes.keySet().iterator();
            while (reportAttributesIterator.hasNext()) {
                String repKey = ((Name) reportAttributesIterator.next()).toString();
                reportDescription.put(repKey, reportAttributes.getValue(repKey));
            }
            if (!isReportDescriptionValid(reportDescription, jarFilePath.toString())) {
                return false;
            }
            File jarsWorkDir = new File(getReportJarExpressFilesPath(name));
            if (!jarsWorkDir.exists()) {
                jarsWorkDir.mkdir();
            }
            String baseDir = jarsWorkDir.getAbsolutePath();
            File jasperWorkDir = new File(getReportJarJasperFilesPath(name));
            if (!jasperWorkDir.exists()) {
                jasperWorkDir.mkdir();
            }
            reportDescription.put(REPORTJAR_ENTRY_JAPSERDIRNAME, jasperWorkDir.getAbsolutePath());
            reportDescription.put(REPORTJAR_ENTRY_CACHEENTRIES, new HashMap());
            reportDescription.put(REPORTJAR_ENTRY_JASPERENTRIES, new HashMap());
            Enumeration jarEntries = reportjar.entries();
            boolean foundJesFile = false;
            boolean foundLanguageFile = false;
            while (jarEntries.hasMoreElements()) {
                JarEntry currEntry = (JarEntry) jarEntries.nextElement();
                String entryName = currEntry.getName();
                if (entryName.startsWith(META_INF_SUBDIR_NAME)) {
                    continue;
                }
                boolean putToCache = true;
                if (entryName.startsWith(JASPER_SUBDIR_NAME) || entryName.endsWith(COMPILED_JAVA_EXTENSION)) {
                    putToCache = false;
                }
                String targetFilename = baseDir.concat(XEnvironmentManager.FILE_SEPARATOR).concat(entryName);
                File target = new File(targetFilename);
                String jesFilePrefix = (String) reportDescription.get(MANIFESTFILE_MANDATORY_JESNAME);
                foundJesFile = foundJesFile || matchesFilePattern(target.getName(), jesFilePrefix, MANDATORY_JES_EXTENSION);
                String languageFilePrefix = (String) reportDescription.get(MANIFESTFILE_MANDATORY_i18FILENAME);
                foundLanguageFile = foundLanguageFile || matchesFilePattern(target.getName(), languageFilePrefix, MANDATORY_i18FILENAME_EXTENSION);
                InputStream is = null;
                FileOutputStream fos = null;
                long jarEntryLastModified = 0;
                jarEntryLastModified = currEntry.getTime();
                try {
                    if (jarEntryLastModified != target.lastModified()) {
                        is = reportjar.getInputStream(currEntry);
                        if (targetFilename.endsWith("/")) {
                            target.mkdir();
                        } else {
                            target.createNewFile();
                            fos = new FileOutputStream(target);
                            byte[] file_buffer = new byte[4096];
                            int bytes_read = is.read(file_buffer);
                            while (bytes_read != -1) {
                                fos.write(file_buffer, 0, bytes_read);
                                bytes_read = is.read(file_buffer);
                            }
                            if (putToCache) {
                                String key = "/".concat(entryName);
                                ((HashMap) reportDescription.get(REPORTJAR_ENTRY_CACHEENTRIES)).put(key, target);
                                expressFilePathCache.put(key, target);
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error("Cannot update report " + name, e);
                } finally {
                    if (is != null) {
                        is.close();
                    }
                    if (fos != null) {
                        fos.flush();
                        fos.close();
                        target.setLastModified(jarEntryLastModified);
                    }
                }
                if (targetFilename.endsWith(REPORTFILE_SOURCE_EXTENSION)) {
                    ((HashMap) reportDescription.get(REPORTJAR_ENTRY_JASPERENTRIES)).put(target.getAbsolutePath(), new Long(target.lastModified()));
                }
            }
            if (!foundJesFile) {
                logger.error("Did not find the given '" + MANIFESTFILE_MANDATORY_JESNAME + "' entry in the jarfile '" + jarFilePath + "'");
                return false;
            } else if (!foundLanguageFile) {
                logger.error("Did not find the given '" + MANIFESTFILE_MANDATORY_i18FILENAME + "' entry in the jarfile '" + jarFilePath + "'");
                return false;
            }
            boolean compileResult = compileJasperFiles(jasperWorkDir, reportDescription, weakCheck);
            if (!compileResult) {
                logger.error("There was an error when compiling the jasper files");
                return false;
            }
            this.registerLanguageKits(jarsWorkDir);
            jasperReports.put(name, reportDescription);
        } catch (IOException ioe) {
            logger.error("Error checking report " + name, ioe);
            return false;
        } catch (Exception e) {
            logger.error("Something serious happened in 'checkReportJar'", e);
        } finally {
            try {
                reportjar.close();
            } catch (Exception e) {
                logger.error(e);
            }
        }
        cachedirty = true;
        return true;
    }
