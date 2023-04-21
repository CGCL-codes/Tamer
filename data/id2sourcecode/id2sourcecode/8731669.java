    protected File copyToLocal(URL url, String filePath, TaskStatus status, CheckSum check, boolean failIfChecksumFailed) throws FileCheckException, IOException, ResourceNotFoundException {
        if (url == null) {
            throw new IllegalArgumentException("the url could not be null");
        }
        File result = null;
        if (filePath == null) {
            String file = url.getFile();
            int lastSlashIndex = file.lastIndexOf("/");
            int lastBackSlashIndex = file.lastIndexOf("\\");
            file = file.substring(Math.max(lastSlashIndex + 1, lastBackSlashIndex + 1));
            try {
                result = File.createTempFile("c_f_" + url.getProtocol() + "_" + url.getHost() + "_" + file + "_", ".cop");
            } catch (IOException ex) {
                result = File.createTempFile("c_f_" + url.getProtocol() + "_" + url.getHost() + "_", ".cop");
            }
        } else {
            result = new File(filePath);
        }
        result.deleteOnExit();
        File parentFile = result.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        if (!result.exists()) {
            result.createNewFile();
        }
        boolean checkFile = true;
        try {
            File cachedFile = this.cache.get(url);
            if (cachedFile == null) {
                logger.debug("copying to local url content \"" + url + "\" to filePath : " + filePath + " with checksum : " + check);
                IOUtilities.copy(url.openStream(), result, status, new byte[2 * 1024]);
            } else {
                if (filePath != null) {
                    IOUtilities.copy(cachedFile, result);
                } else {
                    result = cachedFile;
                }
                checkFile = false;
            }
        } catch (FileNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
        if (checkFile) {
            if (CheckSum.NONE.equals(check)) {
                this.cache.put(url, result);
            } else {
                logger.debug("performing check of the copy with checksum : " + check);
                URL urlSign = new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getPath() + check.extension());
                File tmpFileSign = File.createTempFile("repository", ".xml" + check.extension());
                tmpFileSign.deleteOnExit();
                try {
                    IOUtilities.copy(urlSign.openStream(), tmpFileSign);
                } catch (FileNotFoundException e) {
                    ResourceNotFoundException resEx = new ResourceNotFoundException(e.getMessage());
                    resEx.setStackTrace(e.getStackTrace());
                    throw resEx;
                }
                if (check.isValid(result, tmpFileSign)) {
                    if (result != null) {
                        this.cache.put(url, result);
                    }
                } else {
                    FileCheckException exception = new FileCheckException(result.getAbsolutePath(), "check (method=" + check + ") invalid for repository declaration of " + this);
                    if (failIfChecksumFailed) {
                        throw exception;
                    } else {
                        logger.error("checksum failed for file : '" + result + "' with signature file : '" + tmpFileSign + "' with checksum method : " + check, exception);
                    }
                }
            }
        }
        if (result == null) {
            throw new ResourceNotFoundException(null);
        }
        return result;
    }
