    protected void _fetchResource(String targetUrl, int op, String type, String referer) {
        URL url = null;
        try {
            url = new URL(new URL(referer), targetUrl);
        } catch (java.net.MalformedURLException e) {
            errorMessage("Error fetching resources from " + referer + " Exception: " + e);
        }
        if (webPageCache.containsKey(url.toString())) {
            logMessage("Resource Already Processed: " + url);
            return;
        }
        if (!_checkCache(url.toString())) {
            GetMethod getImageMethod = null;
            try {
                getImageMethod = new GetMethod(url.toString());
                getImageMethod.getParams().setSoTimeout(socketTimeout);
                getImageMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
                getImageMethod.setFollowRedirects(true);
                getImageMethod.setRequestHeader("Referer", referer);
                getImageMethod.setRequestHeader("User-Agent", "WPG Load Generator user: " + (id + 1) + " script: " + scriptName);
                int responseCode = httpClient.executeMethod(getImageMethod);
                if (test) {
                    Request req = new Request(url.toString(), "GET", responseCode, "Image/Binary/Resource");
                    req.queryString = getImageMethod.getQueryString();
                    req.respHeaders = getImageMethod.getResponseHeaders();
                    req.reqHeaders = getImageMethod.getRequestHeaders();
                    try {
                        logger.debug("saving resource: " + logDir + targetUrl);
                        File localFile = new File(logDir + targetUrl);
                        localFile.getParentFile().mkdirs();
                        BufferedInputStream is = new BufferedInputStream(getImageMethod.getResponseBodyAsStream());
                        BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(localFile));
                        int i;
                        while ((i = is.read()) != -1) os.write(i);
                        is.close();
                        os.close();
                    } catch (Exception e) {
                        logger.warn("Error saving resource: " + logDir + targetUrl + " Exception: " + e);
                    }
                    if (status != null) {
                        status.requests.addElement(req);
                    } else {
                        userIterationStatus.requests.addElement(req);
                    }
                }
                if (responseCode != HttpStatus.SC_OK) {
                    logMessage("GET Resource: " + url + " Response Code: " + responseCode + " " + HttpStatus.getStatusText(responseCode));
                    if (test) {
                        if (status != null) {
                            status.logs.addElement(logStr + " Response Code: " + responseCode + " " + HttpStatus.getStatusText(responseCode));
                        } else {
                            userIterationStatus.logs.addElement(logStr + " Response Code: " + responseCode + " " + HttpStatus.getStatusText(responseCode));
                        }
                    }
                    return;
                }
                logMessage("GET Resource: " + url + " Response Code: " + responseCode + " " + HttpStatus.getStatusText(responseCode));
                String lastModified = getImageMethod.getResponseHeader("last-modified").getValue();
                _updateCache(url.toString(), lastModified);
                if (getImageMethod != null) getImageMethod.releaseConnection();
            } catch (HttpException he) {
                errorMessage("HTTP Exception in Get " + he);
            } catch (java.io.IOException ioe) {
                errorMessage("IO Exception in Get " + ioe);
            } catch (Exception e) {
                errorMessage("Exception in Get " + e, e);
            } finally {
                if (getMethod != null) getMethod.releaseConnection();
            }
        } else {
            logMessage("Resource Cached: " + url);
            webPageCache.put(url.toString(), "whenever");
        }
        return;
    }
