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
                logger.warn("Error creating local resource: " + localFile.getPath() + " Exception: " + e, e);
            }
            return;
        }
        String referrer = "";
        List refList = request.getHeaderValues("referer");
        if (refList != null) referrer = (String) refList.get(0);
        String protocol = "http";
        if (request.getToPort() == 443) protocol = "https";
        ScriptGUI.currentScript.recordInd++;
        StringBuffer sb = new StringBuffer("\nstartTransaction(\"" + ScriptGUI.currentScript.getTransactionPrefix() + ScriptGUI.currentScript.recordInd + "\");\n");
        String url = "";
        try {
            url = request.getUri().toURL().toString();
        } catch (java.net.MalformedURLException e) {
            logger.error("MalformedURLException while building URL: " + e, e);
        }
        sb.append("fetchURL( \"" + url + "\",\n\t" + request.getMethod() + ",\n\t\"" + contentType + "\",\n\t\"" + referrer + "\"");
        if (request.getMethod().equals("POST")) {
            boolean multiPartPost = false;
            String mpBoundary = null;
            Iterator it = request.getHeaderValues(HttpMessage.HEADER_CONTENT_TYPE).iterator();
            String ct = null;
            while (it.hasNext()) {
                ct = (String) it.next();
                logger.trace("read header content type: " + ct);
                if (ct.startsWith("multipart/")) {
                    multiPartPost = true;
                    mpBoundary = (ct.split("boundary="))[1];
                    logger.trace("boundary=" + mpBoundary);
                }
            }
            try {
                if (multiPartPost) {
                    Vector<String> mpScript = parseMultiPost(new BufferedReader(new InputStreamReader(request.getBodyContentStream())), mpBoundary);
                    for (int i = 0; i < mpScript.size(); i++) sb.append((i > 0 ? "\t" : ",\n\tnew Part[] { ") + (String) mpScript.elementAt(i) + (i + 1 < mpScript.size() ? ",\n" : " }\n"));
                } else {
                    NameValuePair[] postParameters = parsePost(new BufferedReader(new InputStreamReader(request.getBodyContentStream())));
                    for (int i = 0; i < postParameters.length; i++) sb.append((i > 0 ? "\t" : ",\n\tnew NameValuePair[] { ") + "new NameValuePair(\"" + postParameters[i].getName() + "\", \"" + postParameters[i].getValue() + "\")" + (i + 1 < postParameters.length ? ",\n" : " }\n"));
                }
            } catch (IOException e) {
                logger.error("IOException in request read: " + e);
                e.printStackTrace();
            }
        } else {
            sb.append("\n");
        }
        if (refFormat == null) refFormat = new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss.SSS-");
        String reference = refFormat.format(new Date()) + ScriptGUI.currentScript.recordInd;
        sb.append("); //recording reference: " + reference + "\n");
        sb.append("if(stopTransaction()) return;\n");
        logger.debug("Adding function call: " + sb.toString());
        logRecordingInfo(reference, request, response);
        ScriptGUI.appendScript(sb.toString());
    }
