    public File getFile(ProgressMonitor monitor) throws FileNotFoundException, IOException {
        if (wfs.isAppletMode()) throw new SecurityException("getFile cannot be used with applets.");
        if (false) {
            if (EventQueue.isDispatchThread()) {
                System.err.println("download on event thread! " + this.getNameExt());
            }
        }
        boolean download = false;
        if (monitor == null) throw new NullPointerException("monitor may not be null");
        Date remoteDate;
        if (wfs instanceof HttpFileSystem) {
            URL url = wfs.getURL(this.getNameExt());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            String userInfo = null;
            try {
                userInfo = KeyChain.getDefault().getUserInfo(url);
            } catch (CancelledOperationException ex) {
                throw new FileSystemOfflineException("user cancelled credentials");
            }
            if (userInfo != null) {
                String encode = Base64.encodeBytes(userInfo.getBytes());
                connection.setRequestProperty("Authorization", "Basic " + encode);
            }
            try {
                connection.connect();
                remoteDate = new Date(connection.getLastModified());
            } catch (IOException ex) {
                if (!((HttpFileSystem) wfs).isOffline()) {
                    throw ex;
                } else {
                    remoteDate = new Date(0);
                }
            }
        } else {
            remoteDate = new Date(localFile.lastModified());
        }
        if (localFile.exists()) {
            Date localFileLastModified = new Date(localFile.lastModified());
            if (remoteDate.after(localFileLastModified)) {
                FileSystem.logger.log(Level.INFO, "remote file is newer than local copy of {0}, download.", this.getNameExt());
                download = true;
            }
        } else {
            download = true;
        }
        if (download) {
            try {
                FileSystem.logger.log(Level.FINE, "downloading file {0}", getNameExt());
                if (!localFile.getParentFile().exists()) {
                    FileSystemUtil.maybeMkdirs(localFile.getParentFile());
                }
                File partFile = new File(localFile.toString() + ".part");
                wfs.downloadFile(pathname, localFile, partFile, monitor);
                FileSystem.logger.log(Level.FINE, "downloaded local file has date {0}", new Date(localFile.lastModified()));
            } catch (FileNotFoundException e) {
                throw e;
            } catch (IOException ex) {
                if (ex.getMessage() != null && ex.getMessage().contains("Forbidden")) {
                    throw ex;
                }
                if (this.wfs instanceof HttpFileSystem && !(ex instanceof InterruptedIOException)) {
                    if (this.wfs.isOffline()) {
                        ex.printStackTrace();
                        throw new FileSystem.FileSystemOfflineException("not found in local cache: " + getNameExt());
                    }
                }
                throw ex;
            } finally {
                monitor.finished();
            }
        }
        return localFile;
    }
