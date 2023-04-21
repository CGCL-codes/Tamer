    private void updateConfigurationContent() {
        this.logger.finer("Start to read out the downloader to write a new configuration file for the uploader...");
        Iterator it = this.downloadFiles.keySet().iterator();
        StringBuffer buf = new StringBuffer();
        while (it.hasNext()) {
            String typeId = (String) it.next();
            DownloadFile downloadFile = (DownloadFile) this.downloadFiles.get(typeId);
            typeId = typeId.toLowerCase();
            long l = downloadFile.getLastModified();
            if (l > 0) {
                String s = downloadFile.getLastUpdate();
                buf.append("\n").append(typeId + ".lastupdate=" + s);
                this.logger.finer("Added time for last update (last download) '" + s + "' found for: " + typeId);
            } else {
                buf.append("\n").append(typeId + ".lastupdate=");
                this.logger.finer("Empty lastupdate because last modified is '0' found for: " + typeId);
            }
            String s = Long.toString(l);
            buf.append("\n").append(typeId + ".lastmodified=" + s);
            this.logger.finer("Added lastmodified '" + s + "' for: " + typeId);
            int i = downloadFile.getWait();
            s = Integer.toString(i);
            buf.append("\n").append(typeId + ".wait=" + s);
            this.logger.finer("Added download intervall (wait) '" + s + "' for: " + typeId);
            boolean b = downloadFile.getUpToDate();
            String status = downloadFile.getDownloadStatus();
            if (b) {
                if (status == null) {
                    buf.append("\n").append(typeId + ".downloadstatus=" + "<html><font color='red'>Not set yet</font></html>");
                    this.logger.finer("Error in setting up to date info '" + b + "' for: " + typeId + ". File is up to date but the status was never set.");
                } else {
                    buf.append("\n").append(typeId + ".downloadstatus=" + status);
                    this.logger.finer("File up to date. Take existing status because it was not empty for: " + typeId);
                }
            } else {
                if (status == null) {
                    buf.append("\n").append(typeId + ".downloadstatus=" + "<html><font color='red'>Has never been updated</font></html>");
                    this.logger.finer("Added up to date info '" + b + "' for: " + typeId);
                } else {
                    buf.append("\n").append(typeId + ".downloadstatus=" + status);
                    this.logger.finer("File not up to date. Take existing status because it was not empty for: " + typeId);
                }
            }
            s = downloadFile.getLocalStatus();
            if (s != null) {
                buf.append("\n").append(typeId + ".localstatus=" + s);
                this.logger.finer("Added status (missing/found) '" + s + "' for: " + typeId);
            } else {
                this.logger.finer("No status (missing/found) found for: " + typeId);
            }
            s = downloadFile.getLocalDirectoryRelativ();
            if (s != null) {
                buf.append("\n").append(typeId + ".dir=" + s);
                this.logger.finer("Added local directory (relativ) '" + s + "' for: " + typeId);
            } else {
                this.logger.finer("No local directory (relativ) found for: " + typeId);
            }
            s = downloadFile.getUrl();
            if (s != null) {
                buf.append("\n").append(typeId + ".url.selected=" + s);
                this.logger.finer("Added selected URL '" + s + "' for: " + typeId);
            } else {
                this.logger.finer("No selected URL found for: " + typeId);
            }
            List urls = downloadFile.getUrls();
            if (urls != null) {
                Iterator itURLs = urls.iterator();
                while (itURLs.hasNext()) {
                    s = (String) itURLs.next();
                    buf.append("\n").append(typeId + ".url.listitem=" + s);
                    this.logger.finer("Added URL '" + s + "' for: " + typeId);
                }
            } else {
                this.logger.finer("No URLs found for: " + typeId);
            }
        }
        String s = this.proxy.getProxySet();
        if (s != null) {
            buf.append("\n").append("proxy.set=" + s);
            this.logger.finer("Added proxy.set " + s);
        } else {
            this.logger.finer("No http.proxyPass found");
        }
        s = this.proxy.getProxyHost();
        if (s != null) {
            buf.append("\n").append("http.proxyHost=" + s);
            this.logger.finer("Added proxy.proxyHost " + s);
        } else {
            this.logger.finer("No http.proxyPass found");
        }
        s = this.proxy.getProxyPort();
        if (s != null) {
            buf.append("\n").append("http.proxyPort=" + s);
            this.logger.finer("Added proxy.proxyPort " + s);
        } else {
            this.logger.finer("No http.proxyPass found");
        }
        s = this.proxy.getProxyUser();
        if (s != null) {
            buf.append("\n").append("http.proxyUser=" + s);
            this.logger.finer("Added proxy.proxyUser " + s);
        } else {
            this.logger.finer("No http.proxyPass found");
        }
        s = this.proxy.getProxyPass();
        if (s != null) {
            buf.append("\n").append("http.proxyPass=" + s);
            this.logger.finer("Added proxy.proxyPass *****");
        } else {
            this.logger.finer("No http.proxyPass found");
        }
        this.logger.finer("Finished to read out the downloader to write a new configuration file for the uploade");
        this.configFileContent = buf.toString();
        this.configFileContent = this.configFileContent.replaceAll("(?m)^\\s*$^\\s*", "");
        this.saveConfiguration();
    }
