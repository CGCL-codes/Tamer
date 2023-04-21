    private void addFilesToJar(JarOutputStream jos, File source, String targetName, String baseName, List<String> excludes, Map<String, String> contents) throws IOException, RitaException {
        if (!targetName.startsWith(baseName)) {
            throw new RitaInternalError(targetName + " is not under " + baseName);
        }
        if (contents.containsKey(targetName) && !source.isDirectory()) {
            LOG.warn("Skipping duplicate file " + targetName + " from " + source.getName() + ", already found " + "in " + contents.get(targetName));
            return;
        }
        if (!contents.containsKey(targetName)) {
            contents.put(targetName, source.getPath());
            if (source.isDirectory()) {
                if (targetName.length() > 0) {
                    jos.putNextEntry(new ZipEntry(targetName));
                }
            } else {
                FileInputStream in = new FileInputStream(source);
                addToJar(jos, targetName, source.length(), source.lastModified(), in);
                in.close();
                return;
            }
        }
        for (File subFile : source.listFiles()) {
            String potentialName = targetName + subFile.getName();
            if (subFile.isDirectory()) {
                potentialName += "/";
            }
            boolean isExcluded = false;
            if (potentialName.indexOf(baseName) != 0) {
                throw new RitaInternalError(potentialName + " is not under " + baseName);
            }
            String nameUnderBasename = potentialName.substring(baseName.length());
            for (String exclude : excludes) {
                if (FilenameUtils.wildcardMatch(nameUnderBasename, exclude)) {
                    LOG.debug("Skipping excluded file " + nameUnderBasename + " from " + source.getName() + " which matches " + exclude);
                    isExcluded = true;
                    break;
                }
            }
            if (isExcluded) {
            } else {
                addFilesToJar(jos, subFile, potentialName, baseName, excludes, contents);
            }
        }
    }
