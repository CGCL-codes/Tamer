    protected void downloadFile(String filename, File f, File partFile, ProgressMonitor monitor) throws IOException {
        Lock lock = getDownloadLock(filename, f, monitor);
        if (lock == null) {
            return;
        }
        logger.log(Level.FINE, "downloadFile({0})", filename);
        try {
            URL remoteURL = new URL(root.toString() + filename);
            URLConnection urlc = remoteURL.openConnection();
            String userInfo;
            try {
                userInfo = KeyChain.getDefault().getUserInfo(root);
            } catch (CancelledOperationException ex) {
                throw new IOException("user cancelled at credentials entry");
            }
            if (userInfo != null) {
                String encode = Base64.encodeBytes(userInfo.getBytes());
                urlc.setRequestProperty("Authorization", "Basic " + encode);
            }
            HttpURLConnection hurlc = (HttpURLConnection) urlc;
            if (hurlc.getResponseCode() == 404) {
                logger.log(Level.INFO, "{0} URL: {1}", new Object[] { hurlc.getResponseCode(), remoteURL });
                throw new FileNotFoundException("not found: " + remoteURL);
            } else if (hurlc.getResponseCode() != 200) {
                logger.log(Level.INFO, "{0} URL: {1}", new Object[] { hurlc.getResponseCode(), remoteURL });
                throw new IOException(hurlc.getResponseCode() + ": " + hurlc.getResponseMessage() + "\n" + remoteURL);
            }
            Date d = null;
            List<String> sd = urlc.getHeaderFields().get("Last-Modified");
            if (sd != null && sd.size() > 0) {
                d = new Date(sd.get(sd.size() - 1));
            }
            monitor.setTaskSize(urlc.getContentLength());
            if (!f.getParentFile().exists()) {
                logger.log(Level.FINE, "make dirs {0}", f.getParentFile());
                FileSystemUtil.maybeMkdirs(f.getParentFile());
            }
            if (partFile.exists()) {
                logger.log(Level.FINE, "clobber file {0}", f);
                if (!partFile.delete()) {
                    logger.log(Level.INFO, "Unable to clobber file {0}, better use it for now.", f);
                    return;
                }
            }
            if (partFile.createNewFile()) {
                InputStream in;
                in = urlc.getInputStream();
                logger.log(Level.FINE, "transferring bytes of {0}", filename);
                FileOutputStream out = new FileOutputStream(partFile);
                monitor.setLabel("downloading file");
                monitor.started();
                try {
                    copyStream(in, out, monitor);
                    monitor.finished();
                    out.close();
                    in.close();
                    if (d != null) {
                        try {
                            partFile.setLastModified(d.getTime() + 10);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    if (f.exists()) {
                        logger.log(Level.FINE, "deleting old file {0}", f);
                        if (!f.delete()) {
                            throw new IllegalArgumentException("unable to delete " + f);
                        }
                    }
                    if (!partFile.renameTo(f)) {
                        throw new IllegalArgumentException("rename failed " + partFile + " to " + f);
                    }
                } catch (IOException e) {
                    out.close();
                    in.close();
                    if (partFile.exists() && !partFile.delete()) {
                        throw new IllegalArgumentException("unable to delete " + partFile);
                    }
                    throw e;
                }
            } else {
                throw new IOException("couldn't create local file: " + f);
            }
        } finally {
            lock.unlock();
        }
    }
