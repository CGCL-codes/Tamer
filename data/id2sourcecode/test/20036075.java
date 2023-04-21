    private void addToScript(HttpMessageRequest request, HttpMessageResponse response) {
        if (this.name == null) setName(ScriptGUI.currentScript.name);
        try {
            if (destDir == null) {
                destDir = ScriptGUI.currentScript.path + System.getProperty("file.separator") + "RecordingLog" + System.getProperty("file.separator");
                new File(destDir).mkdirs();
                logger.info("Output Recording Log Dir: " + destDir);
            }
        } catch (Exception e) {
            logger.error("Exception creating recording log directory, Exception: " + e);
        }
        if (logger.isTraceEnabled()) {
            logger.trace("start line: " + request.getStartLine());
            logger.trace("protocol: " + request.getProtocol());
            logger.trace("to host: " + request.getToHost());
            logger.trace("to port: " + request.getToPort());
            logger.trace("version: " + request.getVersion());
            logger.trace("************* Request Header *****************");
            printHeaders(request.getHeaders());
            logger.trace("************* Request Header *****************");
            logger.trace("************* Response Header *****************");
            printHeaders(response.getHeaders());
            logger.trace("************* Response Header *****************");
        }
        List contentList = response.getHeaderValues(HttpMessage.HEADER_CONTENT_TYPE);
        String contentType = "";
        if (contentList != null) {
            contentType = (String) contentList.get(0);
        } else {
            return;
        }
        if (contentType.startsWith("text/css") || !contentType.startsWith("text/")) {
            File localFile = null;
            try {
                localFile = new File(destDir, request.getUri().toString());
                localFile.getParentFile().mkdirs();
                BufferedInputStream is = new BufferedInputStream(response.getBodyContentStream());
                BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(localFile));
                int i;
                while ((i = is.read()) != -1) os.write(i);
                is.close();
                os.close();
            } catch (Exception e) {
                logger.warn("error creating local resource: " + localFile.getPath() + " Exception: " + e);
            }
            return;
        }
        String referrer = "";
        List refList = request.getHeaderValues("referer");
        if (refList != null) referrer = (String) refList.get(0);
        String protocol = "http";
        if (request.getToPort() == 443) protocol = "https";
        ScriptGUI.currentScript.recordInd++;
        StringBuffer sb = new StringBuffer("\n");
        String url = "";
        try {
            url = request.getUri().toURL().toString();
        } catch (java.net.MalformedURLException e) {
            logger.error("MalformedURLException while building URL: " + e, e);
        }
        sb.append("fetchXML( \"" + url + "\",\n\t" + request.getMethod() + ",\n\t\"" + contentType + "\",\n\t\"" + referrer + "\"");
        if (request.getBodyContent() != null) {
            for (String body : new String(request.getBodyContent()).split("\n")) {
                sb.append(",\n\t\"").append(body).append("\"");
            }
        }
        if (refFormat == null) refFormat = new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss.SSS-");
        String reference = refFormat.format(new Date()) + ScriptGUI.currentScript.recordInd;
        sb.append("); //recording reference: " + reference + "\n");
        logger.debug("Adding function call: " + sb.toString());
        logRecordingInfo(reference, request, response);
        ScriptGUI.appendScript(sb.toString());
    }
