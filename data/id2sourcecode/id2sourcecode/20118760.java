    public void execute() throws BuildException {
        verifyArguments();
        URL url = buildURL();
        try {
            URLConnection connection = url.openConnection();
            connection.setUseCaches(getUseCaches());
            long localTimestamp = getTimestamp();
            if (localTimestamp != 0) {
                if (verbose) {
                    Date t = new Date(localTimestamp);
                    log("local file date : " + t.toString());
                }
                connection.setIfModifiedSince(localTimestamp);
            }
            HttpAuthenticationStrategy authStrategy = getAuthStrategy();
            if (authStrategy != null) {
                authStrategy.setAuthenticationHeader(connection, null, username, password);
            }
            HttpRequestParameter header;
            for (int i = 0; i < headers.size(); i++) {
                header = (HttpRequestParameter) headers.get(i);
                connection.setRequestProperty(header.getName(), header.getValue());
            }
            String method = getRequestMethod();
            HttpURLConnection httpConnection = null;
            if (connection instanceof HttpURLConnection) {
                httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod(method);
            }
            log("making " + method + " to " + url);
            connection = doConnect(connection);
            if (!onConnected(connection)) {
                return;
            }
            if (connection instanceof HttpURLConnection) {
                httpConnection = (HttpURLConnection) connection;
            }
            if (httpConnection != null) {
                if (localTimestamp != 0) {
                    if (getResponseCode(httpConnection) == HttpURLConnection.HTTP_NOT_MODIFIED) {
                        log("Local file is up to date - so nothing was downloaded");
                        noteSuccess();
                        return;
                    }
                }
            }
            InputStream is = getInputStream(connection);
            if (is == null) {
                log("Can't get " + url, Project.MSG_ERR);
                if (getFailOnError()) {
                    return;
                }
                throw new BuildException("Can't reach URL");
            }
            OutputStream out = null;
            if (dest != null) {
                log("Saving output to " + dest, Project.MSG_DEBUG);
                out = new FileOutputStream(dest);
            } else {
                if (destinationPropname != null) {
                    log("Saving output to property " + destinationPropname, Project.MSG_DEBUG);
                    out = new ByteArrayOutputStream(blockSize * 1024);
                } else {
                    out = new NullOutputStream();
                }
            }
            int contentLength = connection.getHeaderFieldInt("Content-Length", -1);
            int bytesRead = 0;
            byte[] buffer = new byte[blockSize * 1024];
            int length;
            while ((length = is.read(buffer)) >= 0 && (contentLength == -1 || bytesRead < contentLength)) {
                bytesRead += length;
                out.write(buffer, 0, length);
                if (verbose) {
                    showProgressChar('.');
                }
            }
            if (verbose) {
                showProgressChar('\n');
            }
            if (out instanceof ByteArrayOutputStream) {
                getProject().setProperty(destinationPropname, out.toString());
            }
            out.flush();
            out.close();
            is.close();
            is = null;
            out = null;
            if (!onDownloadFinished(connection)) {
                return;
            }
            if (useTimestamp) {
                long remoteTimestamp = connection.getLastModified();
                if (verbose) {
                    Date t = new Date(remoteTimestamp);
                    log("last modified = " + t.toString() + ((remoteTimestamp == 0) ? " - using current time instead" : ""));
                }
                if (remoteTimestamp != 0) {
                    touchFile(dest, remoteTimestamp);
                }
            }
            String failureString = null;
            if (contentLength > -1 && bytesRead != contentLength) {
                failureString = "Incomplete download -Expected " + contentLength + "received " + bytesRead + " bytes";
            } else {
                if (httpConnection != null && useResponseCode) {
                    int statusCode = httpConnection.getResponseCode();
                    if (statusCode < 200 || statusCode > 299) {
                        failureString = "Server error code " + statusCode + " received";
                    }
                }
            }
            if (failureString == null) {
                noteSuccess();
            } else {
                if (failOnError) throw new BuildException(failureString); else log(failureString, Project.MSG_ERR);
            }
        } catch (IOException ioe) {
            log("Error performing " + getRequestMethod() + " on " + url + " : " + ioe.toString(), Project.MSG_ERR);
            if (failOnError) {
                throw new BuildException(ioe);
            }
        }
    }
