    public void execute() throws BuildException {
        if (httpClient == null) {
            httpClient = new HttpClient();
        }
        HttpMethodBase method = createMethodIfNecessary();
        configureMethod(method);
        try {
            int statusCode = httpClient.executeMethod(method);
            if (statusCodeProperty != null) {
                Property p = (Property) getProject().createTask("property");
                p.setName(statusCodeProperty);
                p.setValue(String.valueOf(statusCode));
                p.perform();
            }
            Iterator it = responseHeaders.iterator();
            while (it.hasNext()) {
                ResponseHeader header = (ResponseHeader) it.next();
                Property p = (Property) getProject().createTask("property");
                p.setName(header.getProperty());
                Header h = method.getResponseHeader(header.getName());
                if (h != null && h.getValue() != null) {
                    p.setValue(h.getValue());
                    p.perform();
                }
            }
            if (responseDataProperty != null) {
                Property p = (Property) getProject().createTask("property");
                p.setName(responseDataProperty);
                p.setValue(method.getResponseBodyAsString());
                p.perform();
            } else if (responseDataFile != null) {
                FileOutputStream fos = null;
                InputStream is = null;
                try {
                    is = method.getResponseBodyAsStream();
                    fos = new FileOutputStream(responseDataFile);
                    byte buf[] = new byte[10 * 1024];
                    int read = 0;
                    while ((read = is.read(buf, 0, 10 * 1024)) != -1) {
                        fos.write(buf, 0, read);
                    }
                } finally {
                    FileUtils.close(fos);
                    FileUtils.close(is);
                }
            }
        } catch (IOException e) {
            throw new BuildException(e);
        } finally {
            cleanupResources(method);
        }
    }
