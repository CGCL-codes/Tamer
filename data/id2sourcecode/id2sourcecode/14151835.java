    public StringBuffer render(RenderEngine c) {
        if (c.isBreakState() || !c.canRender("u")) {
            return new StringBuffer();
        }
        String logTime = null;
        if (c.getWorkerContext() != null) {
            logTime = c.getWorkerContext().getWorkerStart();
        }
        StringBuffer buffer = new StringBuffer();
        StringBuffer locationBuffer = new StringBuffer();
        StringBuffer contentTypeBuffer = new StringBuffer();
        boolean postMode = false;
        varname = TagInspector.processElement(varname, c);
        output = TagInspector.processElement(output, c);
        timeout = TagInspector.processElement(timeout, c);
        urlAttribute = TagInspector.processElement(urlAttribute, c);
        contenttypeAttribute = TagInspector.processElement(contenttypeAttribute, c);
        postAttribute = TagInspector.processElement(postAttribute, c);
        callProc = TagInspector.processElement(callProc, c);
        trim = TagInspector.processElement(trim, c);
        boolean trimData = true;
        if (trim != null && trim.equalsIgnoreCase("false")) {
            trimData = false;
        }
        if (timeout == null || timeout.equals("")) {
            timeout = HTTP_TIMEOUT;
        }
        if (urlAttribute.equals("")) {
            c.setExceptionState(true, "HTTP requires a URL address to be present in the attribute list.");
            return new StringBuffer();
        }
        if (!contenttypeAttribute.equals("")) {
            contentTypeBuffer.append(contenttypeAttribute);
        } else {
            contentTypeBuffer.append("application/x-www-form-urlencoded");
        }
        StringBuffer postData = null;
        if (postAttribute.equalsIgnoreCase("true")) {
            postMode = true;
            if (tags.get("post") != null) {
                DocumentTag postTag = (DocumentTag) tags.get("post");
                postData = postTag.render(c);
            }
        }
        URL url = null;
        HttpURLConnection conn = null;
        HttpsURLConnection connSecure = null;
        InputStream istream = null, estream = null;
        boolean errorState = false;
        locationBuffer = new StringBuffer(urlAttribute);
        OutgoingLimit.getDefault().addHttpIp(locationBuffer.toString().trim());
        if (OutgoingLimit.getDefault().isHttpLimit()) {
            c.setExceptionState(true, "Outgoing connection limit exceeded.");
            return new StringBuffer();
        }
        String locationURL = locationBuffer.toString();
        String authenticationUser = null, authenticationPassword = null;
        if (locationURL.indexOf("@") != -1) {
            String authenticationLine = null;
            String locationURI = null;
            String locationAddress = null;
            if (locationURL.toLowerCase().startsWith("http://")) {
                locationURI = "http://";
            } else if (locationURL.toLowerCase().startsWith("https://")) {
                locationURI = "https://";
            }
            locationAddress = locationURL.substring(locationURI.length());
            authenticationLine = locationAddress.substring(0, locationAddress.indexOf("@"));
            if (authenticationLine.indexOf(":") != -1) {
                authenticationUser = authenticationLine.substring(0, authenticationLine.indexOf(":"));
                authenticationPassword = authenticationLine.substring(authenticationLine.indexOf(":") + 1);
            } else {
                authenticationUser = authenticationLine;
            }
            if (locationAddress.indexOf("@") > locationAddress.indexOf("/")) {
                authenticationUser = null;
                authenticationPassword = null;
            } else {
                locationAddress = locationAddress.substring(locationAddress.indexOf("@") + 1);
                locationURL = locationURI + locationAddress;
                Debug.user(logTime, "Authentication HTTP: User='" + authenticationUser + "' Pass='" + authenticationPassword + "' URI='" + locationURI + "' Addr='" + locationAddress + "' URL='" + locationURL + "'");
            }
        }
        try {
            url = new URL(locationBuffer.toString());
            if (locationBuffer.toString().toLowerCase().startsWith("https:")) {
                connSecure = (HttpsURLConnection) url.openConnection();
                Debug.inform("Opened a SECURE connection to '" + locationBuffer.toString() + "'");
            } else {
                conn = (HttpURLConnection) url.openConnection();
                Debug.inform("Opened a standard connection to '" + locationBuffer.toString() + "'");
            }
        } catch (Exception e) {
            c.setExceptionState(true, "Http is unable to retrieve URL for '" + locationBuffer + "': " + e.getMessage());
            return new StringBuffer();
        }
        if (conn != null) {
            conn.setFollowRedirects(true);
        } else {
            connSecure.setFollowRedirects(true);
        }
        if (c.getClientContext() != null && c.getClientContext().getRequestHeader("cookie") != null) {
            if (conn != null) {
                conn.setRequestProperty("Cookie", c.getClientContext().getRequestHeader("cookie"));
            } else {
                connSecure.setRequestProperty("Cookie", c.getClientContext().getRequestHeader("cookie"));
            }
        }
        if (authenticationUser != null) {
            if (authenticationPassword != null) {
                if (conn != null) {
                    conn.setRequestProperty("Authorization", "Basic " + Base64.encode(authenticationUser + ":" + authenticationPassword));
                } else {
                    connSecure.setRequestProperty("Authorization", "Basic " + Base64.encode(authenticationUser + ":" + authenticationPassword));
                }
            } else {
                if (conn != null) {
                    conn.setRequestProperty("Authorization", "Basic " + Base64.encode(authenticationUser + ":"));
                } else {
                    connSecure.setRequestProperty("Authorization", "Basic " + Base64.encode(authenticationUser + ":"));
                }
            }
        }
        if (conn != null) {
            conn.setRequestProperty("User-Agent", "Java/" + System.getProperty("java.version") + " (compatible) " + Defines.SERVER_NAME + "/" + Defines.SERVER_VERSION + " " + BuildInfo.getBuild());
        } else {
            connSecure.setRequestProperty("User-Agent", "Java/" + System.getProperty("java.version") + " (compatible) " + Defines.SERVER_NAME + "/" + Defines.SERVER_VERSION + " " + BuildInfo.getBuild());
        }
        if (timeout != null && !timeout.equals("")) {
            try {
                java.util.Properties p = java.lang.System.getProperties();
                int connTimeout = Integer.parseInt(timeout);
                connTimeout *= 1000;
                p.put("sun.net.client.defaultConnectTimeout", Integer.toString(connTimeout));
                p.put("sun.net.client.defaultReadTimeout", Integer.toString(connTimeout));
                java.lang.System.setProperties(p);
            } catch (Exception e) {
            }
        }
        if (!postMode) {
            Debug.user(logTime, "Set to use GET, URL='" + locationURL + "'");
            try {
                if (conn != null) {
                    estream = conn.getErrorStream();
                } else {
                    estream = connSecure.getErrorStream();
                }
            } catch (Exception e) {
                if (c.isProtectedVariable(varname)) {
                    c.setExceptionState(true, "Attempted to modify a read-only variable '" + varname + "'");
                    return new StringBuffer();
                }
                c.getVariableContainer().setVariable(varname, e.getMessage());
                return new StringBuffer();
            }
            try {
                if (conn != null) {
                    istream = conn.getInputStream();
                } else {
                    istream = connSecure.getInputStream();
                }
            } catch (Exception e) {
                errorState = true;
            }
            if (!errorState) {
                boolean xmlMode = false;
                if (conn != null) {
                    Debug.user(logTime, "Content type = '" + conn.getContentType() + "'");
                } else {
                    Debug.user(logTime, "Content type = '" + connSecure.getContentType() + "'");
                }
                if (output == null || (output != null && output.equalsIgnoreCase("xml"))) {
                    if (conn != null) {
                        if (conn.getContentType().matches("*xml*")) {
                            xmlMode = true;
                        }
                    } else {
                        if (connSecure.getContentType().matches("*xml*")) {
                            xmlMode = true;
                        }
                    }
                }
                byte data[] = null;
                int curPos = 0, contentLength = 0;
                if (conn != null) {
                    contentLength = conn.getContentLength();
                } else {
                    contentLength = connSecure.getContentLength();
                }
                if (contentLength == -1) {
                    String byteSize = NodeUtil.walkNodeTree(Server.getConfig(), "//configuration/object[@type='engine.tunable']/" + "property[@type='engine.unknowncontentsize']/@value");
                    if (byteSize == null) {
                        contentLength = 4096;
                    } else {
                        contentLength = Integer.parseInt(byteSize);
                    }
                }
                if (conn != null) {
                    handleHeaders(locationBuffer, conn.getHeaderFields());
                } else {
                    handleHeaders(locationBuffer, connSecure.getHeaderFields());
                }
                data = new byte[contentLength];
                try {
                    int dataRead = 0;
                    while ((dataRead = istream.read(data, curPos, contentLength - curPos)) != -1) {
                        if (dataRead == 0) {
                            break;
                        }
                        curPos += dataRead;
                    }
                } catch (Exception e) {
                    c.setExceptionState(true, "&lt;Http&gt; is unable to read data from HTTP connection: " + e.getMessage());
                    return new StringBuffer();
                }
                if (xmlMode) {
                    String newData = new String(data);
                    newData = newData.trim();
                    ByteArrayInputStream bais = new ByteArrayInputStream(newData.getBytes());
                    RenderEngine engine = new RenderEngine(null);
                    DocumentEngine docEngine = null;
                    try {
                        docEngine = new DocumentEngine(bais);
                    } catch (Exception e) {
                        c.setExceptionState(true, "&lt;Http&gt; encountered an error trying to render the document: " + e.getMessage());
                        return new StringBuffer();
                    }
                    engine.setDocumentEngine(docEngine);
                    c.addNodeSet(varname, docEngine.rootTag.thisNode);
                } else {
                    if (output != null && output.equalsIgnoreCase("base64")) {
                        if (c.isProtectedVariable(varname)) {
                            c.setExceptionState(true, "Attempted to modify a read-only variable '" + varname + "'");
                            return new StringBuffer();
                        }
                        c.getVariableContainer().setVariable(varname, Base64.encode(data));
                    } else {
                        if (c.isProtectedVariable(varname)) {
                            c.setExceptionState(true, "Attempted to modify a read-only variable '" + varname + "'");
                            return new StringBuffer();
                        }
                        c.getVariableContainer().setVariable(varname, new String(data));
                    }
                }
            } else {
                if (conn != null) {
                    handleHeaders(locationBuffer, conn.getHeaderFields());
                } else {
                    handleHeaders(locationBuffer, connSecure.getHeaderFields());
                }
                byte data[] = null;
                int curPos = 0, contentLength = 0;
                if (conn != null) {
                    contentLength = conn.getContentLength();
                } else {
                    contentLength = connSecure.getContentLength();
                }
                if (contentLength == -1) {
                    String byteSize = NodeUtil.walkNodeTree(Server.getConfig(), "//configuration/object[@type='engine.tunable']/" + "property[@type='engine.unknowncontentsize']/@value");
                    if (byteSize == null) {
                        contentLength = 4096;
                    } else {
                        contentLength = Integer.parseInt(byteSize);
                    }
                }
                data = new byte[contentLength];
                try {
                    int dataRead = 0;
                    while ((dataRead = estream.read(data, curPos, contentLength - curPos)) != -1) {
                        if (dataRead == 0) {
                            break;
                        }
                        curPos += dataRead;
                    }
                } catch (Exception e) {
                    c.setExceptionState(true, "Http is unable to read data from HTTP Connection: " + e.getMessage());
                    return new StringBuffer();
                }
            }
        } else {
            int contentLength = postData.toString().length();
            String postBoundary = "siliken" + System.currentTimeMillis();
            boolean filePost = false;
            if (tags.get("attachment") != null) {
                filePost = true;
            }
            if (filePost) {
                contentTypeBuffer = new StringBuffer("multipart/form-data; boundary=" + postBoundary);
                Debug.inform("Set content type to '" + contentTypeBuffer + "'");
            }
            if (conn != null) {
                conn.setRequestProperty("Content-type", contentTypeBuffer.toString());
                loadCookies(locationBuffer, conn, c);
                conn.setAllowUserInteraction(false);
                conn.setDoOutput(true);
                conn.setDoInput(true);
            } else {
                connSecure.setRequestProperty("Content-type", contentTypeBuffer.toString());
                loadCookies(locationBuffer, connSecure, c);
                connSecure.setAllowUserInteraction(false);
                connSecure.setDoOutput(true);
                connSecure.setDoInput(true);
            }
            try {
                if (conn != null) {
                    conn.setRequestMethod("POST");
                } else {
                    connSecure.setRequestMethod("POST");
                }
            } catch (Exception e) {
                c.setExceptionState(true, "Http is unable to set request method to POST: " + e.getMessage());
                return new StringBuffer();
            }
            try {
                DataOutputStream dos = null;
                if (conn != null) {
                    dos = new DataOutputStream(conn.getOutputStream());
                } else {
                    dos = new DataOutputStream(connSecure.getOutputStream());
                }
                if (postData != null) {
                    String postSegments[] = postData.toString().split("&");
                    for (int i = 0; i < postSegments.length; i++) {
                        dos.writeBytes("--" + postBoundary + "\r\n");
                        if (postSegments[i].indexOf("=") != -1) {
                            String key = postSegments[i].substring(0, postSegments[i].indexOf("="));
                            String value = postSegments[i].substring(postSegments[i].indexOf("=") + 1);
                            dos.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n");
                            dos.writeBytes(value);
                            dos.writeBytes("\r\n");
                        }
                    }
                } else {
                    dos.writeBytes(postData.toString());
                    dos.writeBytes("\r\n");
                }
                if (postData != null) {
                    Iterator it = tags.keySet().iterator();
                    while (it.hasNext()) {
                        String key = (String) it.next();
                        if (key.startsWith("attachment") && key.indexOf("[") != -1) {
                            boolean rootless = false, isFile = false;
                            String filename = null, postVarname = null;
                            DocumentTag tag = (DocumentTag) tags.get(key);
                            String fileElement = null, rootlessElement = null, typeElement = null;
                            fileElement = TagInspector.get(tag.thisNode, "filename");
                            rootlessElement = TagInspector.get(tag.thisNode, "rootless");
                            typeElement = TagInspector.get(tag.thisNode, "type");
                            postVarname = TagInspector.get(tag.thisNode, "var");
                            fileElement = TagInspector.processElement(fileElement, c);
                            rootlessElement = TagInspector.processElement(rootlessElement, c);
                            typeElement = TagInspector.processElement(typeElement, c);
                            postVarname = TagInspector.processElement(postVarname, c);
                            if (typeElement.equalsIgnoreCase("file")) {
                                isFile = true;
                            } else if (typeElement.equalsIgnoreCase("data")) {
                                isFile = false;
                            } else {
                                c.setExceptionState(true, "Attachment specified, but invalid type - must be file or data.");
                            }
                            if (fileElement == null || fileElement.equals("")) {
                                c.setExceptionState(true, "Attachment must contain a filename.");
                            }
                            if (rootlessElement.equalsIgnoreCase("true")) {
                                rootless = true;
                            }
                            if (postVarname == null || postVarname.equals("")) {
                                c.setExceptionState(true, "Post requires a variable name when posting an attachment.");
                            }
                            filename = fileElement;
                            filePost = true;
                            String currentDocroot = null;
                            if (c.getWorkerContext() == null) {
                                if (c.getRenderContext().getCurrentDocroot() == null) {
                                    currentDocroot = ".";
                                } else {
                                    currentDocroot = c.getRenderContext().getCurrentDocroot();
                                }
                            } else {
                                currentDocroot = c.getWorkerContext().getDocRoot();
                            }
                            if (rootless) {
                                if (c.getVendContext().getVend().getIgnorableDocroot(c.getClientContext().getMatchedHost())) {
                                    currentDocroot = "";
                                }
                            }
                            if (!currentDocroot.endsWith("/")) {
                                if (!currentDocroot.equals("") && currentDocroot.length() > 0) {
                                    currentDocroot += "/";
                                }
                            }
                            dos.writeBytes("--" + postBoundary + "\r\n");
                            dos.writeBytes("Content-Disposition: form-data; name=\"" + postVarname + "\"; filename=\"" + filename + "\"\r\n\r\n");
                            byte[] attachmentBytes = null;
                            if (isFile) {
                                attachmentBytes = FileCache.getDefault().read(c.getWorkerContext(), currentDocroot + filename, c.getVendContext().getVend().getRenderExtension(c.getClientContext().getMatchedHost()), c.getVendContext().getVend().getServerpageExtensions(), false);
                            } else {
                                attachmentBytes = tag.render(c).toString().getBytes();
                            }
                            dos.writeBytes(new String(attachmentBytes));
                            dos.writeBytes("\r\n");
                        }
                    }
                    dos.writeBytes("--" + postBoundary + "--\r\n");
                }
                dos.flush();
                dos.close();
            } catch (Exception e) {
                c.setExceptionState(true, "&lt;Http&gt; is unable to write post data to URL connection: " + e.getMessage());
                return new StringBuffer();
            }
            try {
                if (conn != null) {
                    estream = conn.getErrorStream();
                } else {
                    estream = connSecure.getErrorStream();
                }
            } catch (Exception e) {
                if (c.isProtectedVariable(varname)) {
                    c.setExceptionState(true, "Attempted to modify a read-only variable '" + varname + "'");
                    return new StringBuffer();
                }
                c.getVariableContainer().setVariable(varname, e.getMessage());
                return new StringBuffer();
            }
            try {
                if (conn != null) {
                    istream = conn.getInputStream();
                } else {
                    istream = connSecure.getInputStream();
                }
            } catch (Exception e) {
                c.setExceptionState(true, "Request failed: " + e.getMessage());
                return new StringBuffer();
            }
            if (!errorState) {
                int responseCode = 0;
                try {
                    if (conn != null) {
                        responseCode = conn.getResponseCode();
                    } else {
                        responseCode = connSecure.getResponseCode();
                    }
                } catch (Exception e) {
                    c.setExceptionState(true, "&lt;Http&gt; is unable to retrieve response code from post: " + e.getMessage());
                    return new StringBuffer();
                }
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    c.setExceptionState(true, "&lt;Http&gt; is unable to connect to '" + locationBuffer + "': HTTP error code = " + String.valueOf(responseCode));
                    return new StringBuffer();
                }
                boolean xmlMode = false;
                if (conn != null) {
                    Debug.user(logTime, "Content type = '" + conn.getContentType() + "'");
                } else {
                    Debug.user(logTime, "Content type = '" + connSecure.getContentType() + "'");
                }
                if (output == null || (output != null && output.equalsIgnoreCase("xml"))) {
                    if (conn != null) {
                        if (conn.getContentType().matches("*xml*")) {
                            xmlMode = true;
                        }
                    } else {
                        if (connSecure.getContentType().matches("*xml*")) {
                            xmlMode = true;
                        }
                    }
                }
                if (conn != null) {
                    handleHeaders(locationBuffer, conn.getHeaderFields());
                } else {
                    handleHeaders(locationBuffer, connSecure.getHeaderFields());
                }
                byte data[] = null;
                int curPos = 0;
                if (conn != null) {
                    contentLength = conn.getContentLength();
                } else {
                    contentLength = connSecure.getContentLength();
                }
                if (contentLength == -1) {
                    String byteSize = NodeUtil.walkNodeTree(Server.getConfig(), "//configuration/object[@type='engine.tunable']/" + "property[@type='engine.unknowncontentsize']/@value");
                    if (byteSize == null) {
                        contentLength = 4096;
                    } else {
                        contentLength = Integer.parseInt(byteSize);
                    }
                }
                data = new byte[contentLength];
                try {
                    int dataRead = 0;
                    while ((dataRead = istream.read(data, curPos, contentLength - curPos)) != -1) {
                        if (dataRead == 0) {
                            break;
                        }
                        curPos += dataRead;
                    }
                } catch (Exception e) {
                    c.setExceptionState(true, "&lt;Http&gt; is unable to read data from HTTP Connection: " + e.getMessage());
                    return new StringBuffer();
                }
                if (xmlMode) {
                    String newData = new String(data);
                    if (trimData) {
                        newData = newData.trim();
                    }
                    ByteArrayInputStream bais = new ByteArrayInputStream(newData.getBytes());
                    RenderEngine engine = new RenderEngine(null);
                    DocumentEngine docEngine = null;
                    try {
                        docEngine = new DocumentEngine(bais);
                    } catch (Exception e) {
                        c.setExceptionState(true, "&lt;Http&gt; is unable to render the retrieved document: " + e.getMessage());
                        return new StringBuffer();
                    }
                    engine.setDocumentEngine(docEngine);
                    c.addNodeSet(varname, docEngine.rootTag.thisNode);
                } else {
                    String newData = new String(data);
                    if (trimData) {
                        newData = newData.trim();
                    }
                    if (output != null && output.equalsIgnoreCase("base64")) {
                        if (c.isProtectedVariable(varname)) {
                            c.setExceptionState(true, "Attempted to modify a read-only variable '" + varname + "'");
                            return new StringBuffer();
                        }
                        c.getVariableContainer().setVariable(varname, Base64.encode(newData));
                    } else {
                        if (c.isProtectedVariable(varname)) {
                            c.setExceptionState(true, "Attempted to modify a read-only variable '" + varname + "'");
                            return new StringBuffer();
                        }
                        c.getVariableContainer().setVariable(varname, newData);
                    }
                }
            } else {
                byte data[] = null;
                int curPos = 0;
                if (conn != null) {
                    contentLength = conn.getContentLength();
                } else {
                    contentLength = connSecure.getContentLength();
                }
                if (contentLength == -1) {
                    String byteSize = NodeUtil.walkNodeTree(Server.getConfig(), "//configuration/object[@type='engine.tunable']/" + "property[@type='engine.unknowncontentsize']/@value");
                    if (byteSize == null) {
                        contentLength = 4096;
                    } else {
                        contentLength = Integer.parseInt(byteSize);
                    }
                }
                data = new byte[contentLength];
                try {
                    int dataRead = 0;
                    while ((dataRead = estream.read(data, curPos, contentLength - curPos)) != -1) {
                        if (dataRead == 0) {
                            break;
                        }
                        curPos += dataRead;
                    }
                } catch (Exception e) {
                    c.setExceptionState(true, "Http is unable to read data from HTTP Connection: " + e.getMessage());
                    return new StringBuffer();
                }
            }
        }
        try {
            if (istream != null) {
                istream.close();
            }
            if (estream != null) {
                estream.close();
            }
            if (conn != null) {
                conn.disconnect();
            } else {
                connSecure.disconnect();
            }
        } catch (MalformedURLException e) {
            c.setExceptionState(true, "&lt;Http&gt; request to site '" + locationBuffer + "' is invalid: " + e.getMessage());
        } catch (IOException e) {
            c.setExceptionState(true, "&lt;Http&gt; request to site '" + locationBuffer + "' failed to connect.");
        }
        if (callProc != null && !callProc.equals("")) {
            Call call = new Call();
            call.callProcedure(c, null, null, callProc, null);
        }
        return new StringBuffer();
    }
