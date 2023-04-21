    public boolean isLocal() {
        if (wfs.isAppletMode()) return false;
        boolean download = false;
        if (localFile.exists()) {
            synchronized (wfs) {
                Date remoteDate;
                long localFileLastAccessed = wfs.getLastAccessed(this.getNameExt());
                if (System.currentTimeMillis() - localFileLastAccessed > 60000) {
                    try {
                        if (wfs instanceof HttpFileSystem) {
                            URL url = wfs.getURL(this.getNameExt());
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setRequestMethod("HEAD");
                            connection.connect();
                            remoteDate = new Date(connection.getLastModified());
                        } else {
                            remoteDate = new Date(localFile.lastModified());
                        }
                        Date localFileLastModified = new Date(localFile.lastModified());
                        if (remoteDate.after(localFileLastModified)) {
                            FileSystem.logger.info("remote file is newer than local copy of " + this.getNameExt() + ", download.");
                            download = true;
                        }
                        wfs.markAccess(this.getNameExt());
                    } catch (IOException ex) {
                        return false;
                    }
                }
            }
        } else {
            download = true;
        }
        return !download;
    }
