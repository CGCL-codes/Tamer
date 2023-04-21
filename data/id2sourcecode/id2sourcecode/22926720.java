    private void downloadFile() {
        String sUrl = this.downloadFile.getUrl();
        this.logger.log(Level.FINER, "Preparing to download the URL: {0}", sUrl);
        if (sUrl == null) {
            this.downloadFile.setDownloadStatus("<html>font color='green'>No URL selected</font></html>");
            this.logger.fine("Nothing to download. URL not set.");
            return;
        }
        URL url = null;
        try {
            url = new URL(sUrl);
        } catch (MalformedURLException ex) {
            this.logger.log(Level.SEVERE, null, ex);
            this.downloadFile.setDownloadStatus("<html>font color='red'>Malformed URL</font></html>");
            loggingBoard.logMessage("Malformed URL: '" + sUrl + "'. Exception: '" + ex.toString() + "'. Message of Exception is: '" + ex.getMessage() + "'.");
            return;
        }
        String remoteFile = url.getFile();
        this.logger.log(Level.FINER, "Trying to find a file name in the URL: {0}", sUrl);
        String[] splittees = remoteFile.split("/");
        int length = splittees.length;
        String filename = splittees[length - 1];
        this.logger.log(Level.FINER, "Found the file name: {0} in the URL: {1}", new Object[] { filename, sUrl });
        String localDirectoryAbsolute = System.getProperty("user.dir");
        String localFile = localDirectoryAbsolute + "/" + filename;
        this.logger.log(Level.FINER, "Trying to download the remote file ''{0}'' to ''{1}''...", new Object[] { sUrl, localFile });
        HttpURLConnection http = null;
        long byteCount = 0;
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            String proxySet = proxy.getProxySet();
            if (proxySet.equalsIgnoreCase("true")) {
                Properties systemSettings = System.getProperties();
                String proxyHost = proxy.getProxyHost();
                String proxyPort = proxy.getProxyPort();
                String proxyUser = proxy.getProxyUser();
                String proxyPass = proxy.getProxyPass();
                systemSettings.put("proxySet", "true");
                systemSettings.put("http.proxyHost", proxyHost);
                systemSettings.put("http.proxyPort", proxyPort);
                if (proxyUser != null && !proxyUser.equals("")) {
                    Authenticator.setDefault(new SimpleAuthenticator(proxyUser, proxyPass));
                }
            }
            http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("HEAD");
            int responseCode = http.getResponseCode();
            String responseMessage = http.getResponseMessage();
            String longResponseMessage = responseCode + ", " + responseMessage;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                this.logger.log(Level.FINER, "File exists on server. URL: {0}", sUrl);
            } else {
                this.downloadFile.setDownloadStatus("<html><font color='red'>" + longResponseMessage + "</font></html>");
                String message = "File not found on server or problems with proxy: '" + sUrl + "'. Server Response '" + longResponseMessage + "'.";
                this.logger.info(message);
                loggingBoard.logMessage(message);
                return;
            }
            long lastModifiedOnServer = http.getLastModified();
            long lastModifiedOnDisk = this.downloadFile.getLastModified();
            String timeOnServer = TimeStamp.getFullTimstamp(new Date(lastModifiedOnServer));
            String timeOnDisc = TimeStamp.getFullTimstamp(new Date(lastModifiedOnDisk));
            if (lastModifiedOnServer > lastModifiedOnDisk) {
                this.logger.log(Level.FINER, "File on server is newer ''{0}'' than on local disc ''{1}''. Downloading url ''{2}''...", new Object[] { timeOnServer, timeOnDisc, sUrl });
            } else if (lastModifiedOnServer == lastModifiedOnDisk) {
                if (lastModifiedOnServer == 0) {
                    this.logger.warning("The Server gave a lastModified() of '0'. It this happens it might be ok. Proceeding with download.");
                    loggingBoard.logMessage("WATCH THIS: Server responded with '0' for lastModified(). Proceed download of " + sUrl);
                } else {
                    this.downloadFile.setDownloadStatus("<html><font color='red'>File on Server was not updated</font></html>");
                    this.logger.log(Level.FINER, "File on server is as old ''{0}'' as on local disc ''{1}''. No need to download url ''{2}''.", new Object[] { timeOnServer, timeOnDisc, sUrl });
                    return;
                }
            } else {
                this.downloadFile.setDownloadStatus("<html><font color='red'>File on Server older than local copy</font></html>");
                String message = "File on server '" + timeOnServer + "' is older than local disc '" + timeOnDisc + "'. Did you change the server? How ever, no need to download '" + sUrl + "'...";
                this.logger.info(message);
                loggingBoard.logMessage(message);
                return;
            }
            this.logger.log(Level.FINER, "Opening connection for url: {0}", sUrl);
            in = new BufferedInputStream(new DataInputStream(url.openStream()));
            out = new BufferedOutputStream(new DataOutputStream(new FileOutputStream(localFile)));
            byte[] bbuf = new byte[4096];
            length = -1;
            while ((length = in.read(bbuf)) != -1) {
                if (!this.run) {
                    String msg = "Download was stopped befor completed.";
                    this.logger.fine(msg);
                    this.downloadFile.setDownloadStatus("<html><font color='yellow'>" + msg + "</font></html>");
                    loggingBoard.logMessage(msg);
                    return;
                }
                out.write(bbuf, 0, length);
                byteCount = byteCount + length;
            }
            this.logger.log(Level.FINEST, "Wrote ''{0}'' bytes into file ''{1}''.", new Object[] { byteCount, localFile });
            in.close();
            in = null;
            out.close();
            out = null;
            http.disconnect();
            http = null;
            this.downloadFile.setLastModified(lastModifiedOnServer);
            this.downloadFile.setDownloadStatus("<html><font color='green'>File up to date with Server</font></html>");
            this.logger.log(Level.INFO, "Downloaded file ''{0}'' to ''{1}''.", new Object[] { sUrl, localFile });
            loggingBoard.logMessage("Downloaded: " + sUrl);
        } catch (java.net.UnknownHostException ex) {
            this.downloadFile.setDownloadStatus("<html><font color='red'>Connection failed</font></html>");
            this.logger.log(Level.FINER, "Download failed, URL: {0}. Exception: {1}. Original messages: {2}", new Object[] { sUrl, ex.toString(), ex.getLocalizedMessage() });
        } catch (Exception ex) {
            this.downloadFile.setDownloadStatus("<html><font color='red'>Download failed</font></html>");
            this.logger.log(Level.FINER, "Download failed, URL: {0}. Exception: {1}. Original messages: {2}", new Object[] { sUrl, ex.toString(), ex.getLocalizedMessage() });
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
            if (http != null) {
                try {
                    http.disconnect();
                } catch (Exception e) {
                }
            }
        }
    }
