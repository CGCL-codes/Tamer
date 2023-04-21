    public ResponseBuffer handleRequest(String request, int vfstype) throws KeepaliveException {
        ResponseBuffer response = new ResponseBuffer();
        String requestedDocument = request;
        String physicalRequestDirectory = wContext.getRequestDirectory();
        String bareFile = null;
        String docRoot = wContext.getDocRoot();
        String rewrittenDocument = null;
        String pathinfo = null;
        long currentUsedBandwidth = wContext.getCurrentUsedBandwidth();
        long currentMaxBandwidth = wContext.getCurrentMaxBandwidth();
        long ifModifiedSince = wContext.getClientContext().getIfModifiedSince();
        boolean isProtected = false, bandwidthExceeded = false, allowGzip = wContext.getClientContext().getAllowGzip();
        ArrayList filterList = wContext.getVendContext().getVend().getFilter(wContext.getClientContext().getMatchedHost());
        int filterListSize = filterList.size();
        int type = vfstype;
        if (type == ClientHandler.PAGE_NOT_FOUND_MAPPED || type == (ClientHandler.PAGE_VFS_BASED + ClientHandler.PAGE_NOT_FOUND_MAPPED)) {
            rewrittenDocument = wContext.getErrorPage(request);
            type = wContext.getVendContext().getFileAccess().getVFSType(rewrittenDocument, wContext.getClientContext().getMatchedHost());
            if (type != FileAccess.TYPE_UNKNOWN) {
                type = ClientHandler.PAGE_VFS_BASED + ClientHandler.PAGE_STATIC_DATA;
                Debug.debug("Filename '" + rewrittenDocument + "' matches a VFS lookup.");
            }
        }
        int periodIndex = request.indexOf(".");
        if (periodIndex != -1) {
            if (request.indexOf("/", periodIndex) != -1) {
                String extension = request.substring(periodIndex + 1, request.indexOf("/", periodIndex));
                if (BinarySupport.isBinary(extension)) {
                    pathinfo = request.substring(request.indexOf("/", periodIndex + 1));
                    request = request.substring(0, request.indexOf("/", periodIndex));
                }
            }
        }
        requestedDocument = request;
        Debug.debug("Handling request: request='" + request + "' origtype='" + vfstype + "' type='" + type + "' rewrittenDocument='" + rewrittenDocument + "' pathinfo='" + pathinfo + "'");
        int requestLastSlash = request.lastIndexOf("/");
        if (requestLastSlash != -1) {
            bareFile = request.substring(requestLastSlash + 1);
        } else {
            bareFile = request;
        }
        for (int i = 0; i < filterListSize; i++) {
            String fileFilter = (String) filterList.get(i);
            if (bareFile.equalsIgnoreCase(fileFilter) || requestedDocument.indexOf(fileFilter) != -1) {
                isProtected = true;
                break;
            }
        }
        if (isProtected) {
            StringBuffer body = WebResponseCodes.createGenericResponse(wContext, WebResponseCodes.HTTP_NOT_FOUND, "The requested resource '" + requestedDocument + "' was not found on this server.");
            response.setBody(body, allowGzip);
            response.setHeaders(wContext.createHeader(null, WebResponseCodes.HTTP_NOT_FOUND, response.getBodySize(), 0L, "text/html", false, allowGzip));
            response.setResponse(WebResponseCodes.HTTP_NOT_FOUND);
            return response;
        }
        boolean cachable = true;
        try {
            if (rewrittenDocument != null) {
                cachable = FileCache.getDefault().isCachable(rewrittenDocument);
                Debug.debug("Data is cachable for request '" + rewrittenDocument + "'");
            } else {
                cachable = FileCache.getDefault().isCachable(wContext.getDocRoot() + "/" + requestedDocument);
                Debug.debug("Data is cachable for request '" + wContext.getDocRoot() + "/" + requestedDocument + "'");
            }
        } catch (IOException e) {
        }
        if (cachable) {
            byte[] fileData = null;
            long lastModified = 0L;
            try {
                if (type > ClientHandler.PAGE_VFS_BASED) {
                    if (rewrittenDocument != null) {
                        fileData = wContext.getVendContext().getVend().getFileAccess().getFile(wContext, rewrittenDocument, wContext.getClientContext().getMatchedHost(), wContext.getVendContext().getVend().getRenderExtension(wContext.getClientContext().getMatchedHost()), wContext.getVendContext().getVend().getServerpageExtensions(), true);
                        if (lastModified == 0) {
                            lastModified = wContext.getVendContext().getVend().getFileAccess().lastModified(wContext, rewrittenDocument, wContext.getClientContext().getMatchedHost());
                        }
                        Debug.debug("File data for request '" + rewrittenDocument + "' size='" + fileData.length + "' lastModified='" + lastModified + "'");
                    } else {
                        fileData = wContext.getVendContext().getVend().getFileAccess().getFile(wContext, requestedDocument, wContext.getClientContext().getMatchedHost(), wContext.getVendContext().getVend().getRenderExtension(wContext.getClientContext().getMatchedHost()), wContext.getVendContext().getVend().getServerpageExtensions(), true);
                        if (lastModified == 0) {
                            lastModified = wContext.getVendContext().getVend().getFileAccess().lastModified(wContext, requestedDocument, wContext.getClientContext().getMatchedHost());
                        }
                        Debug.debug("File data for request '" + requestedDocument + "' size='" + fileData.length + "' lastModified='" + lastModified + "'");
                    }
                } else {
                    fileData = FileCache.getDefault().read(null, wContext.getDocRoot() + "/" + requestedDocument, wContext.getVendContext().getVend().getRenderExtension(wContext.getClientContext().getMatchedHost()), wContext.getVendContext().getVend().getServerpageExtensions(), false);
                    lastModified = FileCache.getDefault().lastModified(null, requestedDocument);
                    Debug.debug("File data for request '" + requestedDocument + "' size='" + fileData.length + "' lastModified='" + lastModified + "'");
                }
            } catch (Exception e) {
                StringBuffer body = WebResponseCodes.createGenericResponse(wContext, WebResponseCodes.HTTP_NOT_FOUND, "The requested resource '" + requestedDocument + "' was not found on this server.");
                response.setBody(body, allowGzip);
                response.setHeaders(wContext.createHeader(null, WebResponseCodes.HTTP_NOT_FOUND, response.getBodySize(), 0L, "text/html", false, allowGzip));
                response.setResponse(WebResponseCodes.HTTP_NOT_FOUND);
                return response;
            }
            currentUsedBandwidth += fileData.length;
            if (currentMaxBandwidth != 0 && (currentUsedBandwidth + fileData.length) > currentMaxBandwidth) {
                bandwidthExceeded = true;
            }
            if (bandwidthExceeded) {
                Debug.debug("Connection Failed: Bandwidth exceeded.");
                StringBuffer body = WebResponseCodes.createGenericResponse(wContext, WebResponseCodes.HTTP_BANDWIDTH_EXCEEDED, "The server is temporarily unable to service your request due to the site owner reaching his/her " + "bandwidth limit.  If you feel you have received this message in error, please contact the site " + "administrator at '" + wContext.getEmail() + "'");
                response.setBody(body, allowGzip);
                response.setHeaders(wContext.createHeader(null, WebResponseCodes.HTTP_BANDWIDTH_EXCEEDED, response.getBodySize(), 0L, "text/html", false, allowGzip));
                response.setResponse(WebResponseCodes.HTTP_BANDWIDTH_EXCEEDED);
                return response;
            }
            if (ifModifiedSince != 0L && ifModifiedSince >= lastModified) {
                response.setHeaders(wContext.createHeader(null, WebResponseCodes.HTTP_NOT_MODIFIED, 0, 0L, "text/html", false, allowGzip));
                response.setResponse(WebResponseCodes.HTTP_NOT_MODIFIED);
                return response;
            }
            BinaryContext bContext = null;
            if (BinarySupport.isBinary(bareFile)) {
                try {
                    bContext = BinarySupport.handleBinary(wContext.getDocRoot() + "/" + requestedDocument, request, pathinfo, wContext);
                } catch (Exception e) {
                    StringBuffer body = WebResponseCodes.createGenericResponse(wContext, WebResponseCodes.HTTP_INTERNAL_ERROR, "The requested resource '" + requestedDocument + "' failed to process: " + e.getMessage());
                    response.setBody(body, allowGzip);
                    response.setHeaders(wContext.createHeader(null, WebResponseCodes.HTTP_INTERNAL_ERROR, response.getBodySize(), 0L, "text/html", false, allowGzip));
                    response.setResponse(WebResponseCodes.HTTP_INTERNAL_ERROR);
                    return response;
                }
            }
            if (bContext != null) {
                response.setBody(bContext.getBody());
            } else {
                response.setBody(fileData);
            }
            if (bContext != null && bContext.getHeaders().length() != 0) {
                response.setHeaders(bContext.getHeaders());
            } else {
                response.setHeaders(wContext.createHeader(requestedDocument, WebResponseCodes.HTTP_OK, (int) fileData.length, lastModified, null, true, false));
            }
            response.setResponse(WebResponseCodes.HTTP_OK);
            return response;
        } else {
            File sendFile = new File(wContext.getDocRoot() + "/" + requestedDocument);
            if (!sendFile.exists()) {
                StringBuffer body = WebResponseCodes.createGenericResponse(wContext, WebResponseCodes.HTTP_NOT_FOUND, "The requested resource '" + requestedDocument + "' was not found on this server.");
                response.setBody(body, allowGzip);
                response.setHeaders(wContext.createHeader(null, WebResponseCodes.HTTP_NOT_FOUND, response.getBodySize(), 0L, "text/html", false, allowGzip));
                response.setResponse(WebResponseCodes.HTTP_NOT_FOUND);
                return response;
            }
            if (currentMaxBandwidth != 0 && (currentUsedBandwidth + sendFile.length()) > currentMaxBandwidth) {
                bandwidthExceeded = true;
            }
            if (bandwidthExceeded) {
                Debug.debug("Connection Failed: Bandwidth exceeded.");
                StringBuffer body = WebResponseCodes.createGenericResponse(wContext, WebResponseCodes.HTTP_BANDWIDTH_EXCEEDED, "The server is temporarily unable to service your request due to the site owner reaching his/her " + "bandwidth limit.  If you feel you have received this message in error, please contact the site " + "administrator at '" + wContext.getEmail() + "'");
                response.setBody(body, allowGzip);
                response.setHeaders(wContext.createHeader(null, WebResponseCodes.HTTP_BANDWIDTH_EXCEEDED, response.getBodySize(), 0L, "text/html", false, allowGzip));
                response.setResponse(WebResponseCodes.HTTP_BANDWIDTH_EXCEEDED);
                return response;
            }
            if (ifModifiedSince != 0L && ifModifiedSince >= sendFile.lastModified()) {
                response.setHeaders(wContext.createHeader(null, WebResponseCodes.HTTP_NOT_MODIFIED, 0, 0L, "text/html", false, allowGzip));
                response.setResponse(WebResponseCodes.HTTP_NOT_MODIFIED);
                return response;
            }
            if (MimeTypes.getDefault().getBinaryAssociation(wContext.getDocRoot() + "/" + requestedDocument) != null && (new File(MimeTypes.getDefault().getBinaryAssociation(wContext.getDocRoot() + "/" + requestedDocument)).exists())) {
                String processorBinary = MimeTypes.getDefault().getBinaryAssociation(wContext.getDocRoot() + "/" + requestedDocument);
                String unknownContent = "text/html";
                ArrayList environmentVariables = new ArrayList();
                String envp[] = null;
                String unknownString = NodeUtil.walkNodeTree(Server.getConfig(), "//configuration/object[@type='engine.default']/property[@type='engine.binaryunknowncontent']/@value");
                byte fileData[] = null;
                long lastModified = 0L;
                Debug.debug("File '" + requestedDocument + "' matches a binary file handler '" + processorBinary + "'");
                try {
                    fileData = FileCache.getDefault().read(null, wContext.getDocRoot() + "/" + requestedDocument, wContext.getVendContext().getVend().getRenderExtension(wContext.getClientContext().getMatchedHost()), wContext.getVendContext().getVend().getServerpageExtensions(), false);
                    lastModified = FileCache.getDefault().lastModified(null, wContext.getDocRoot() + "/" + requestedDocument);
                } catch (Exception e) {
                    StringBuffer body = WebResponseCodes.createGenericResponse(wContext, WebResponseCodes.HTTP_NOT_FOUND, "The requested resource '" + requestedDocument + "' was not found on this server.");
                    response.setBody(body, allowGzip);
                    response.setHeaders(wContext.createHeader(null, WebResponseCodes.HTTP_NOT_FOUND, response.getBodySize(), 0L, "text/html", false, allowGzip));
                    response.setResponse(WebResponseCodes.HTTP_NOT_FOUND);
                    return response;
                }
                if (unknownString != null) {
                    unknownContent = unknownString;
                }
                Process proc = null;
                StringBuffer output = new StringBuffer();
                String str;
                int n;
                String binaryString = NodeUtil.walkNodeTree(Server.getConfig(), "//configuration/object[@type='engine.default']/property[@type='engine.binaryapachesupport']/@value");
                StringBuffer ps = new StringBuffer();
                StringBuffer headersPs = new StringBuffer();
                if (binaryString != null && binaryString.equalsIgnoreCase("true")) {
                    if (wContext.getClientContext() != null) {
                        environmentVariables.add("SERVER_ADDR=" + wContext.getClientContext().getHost());
                        environmentVariables.add("SERVER_PORT=" + wContext.getClientContext().getPort());
                        environmentVariables.add("REMOTE_ADDR=" + wContext.getClientContext().getHost());
                        environmentVariables.add("REMOTE_PORT=" + wContext.getClientContext().getPort());
                        environmentVariables.add("DOCUMENT_ROOT=" + wContext.getDocRoot());
                        environmentVariables.add("REQUEST_URI=" + request);
                        int requestedDocumentQuestion = request.indexOf("?");
                        if (requestedDocumentQuestion != -1) {
                            environmentVariables.add("QUERY_STRING=" + request.substring(requestedDocumentQuestion + 1));
                        }
                        environmentVariables.add("REQUEST_METHOD=GET");
                        int envVarSize = environmentVariables.size();
                        if (envVarSize > 0) {
                            envp = new String[envVarSize];
                            for (int counter = 0; counter < envVarSize; counter++) {
                                envp[counter] = (String) environmentVariables.get(counter);
                            }
                        }
                        environmentVariables = null;
                    }
                    try {
                        proc = Runtime.getRuntime().exec(processorBinary, envp);
                        if (proc == null) {
                            StringBuffer body = WebResponseCodes.createGenericResponse(wContext, WebResponseCodes.HTTP_INTERNAL_ERROR, "Unable to process this requested file through the appropriate binary.");
                            response.setBody(body, allowGzip);
                            response.setHeaders(wContext.createHeader(null, WebResponseCodes.HTTP_INTERNAL_ERROR, response.getBodySize(), 0L, "text/html", false, allowGzip));
                            response.setResponse(WebResponseCodes.HTTP_NOT_FOUND);
                            return response;
                        }
                        OutputStream ostr = proc.getOutputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                        ostr.write(fileData, 0, fileData.length);
                        ostr.flush();
                        ostr.close();
                        boolean firstLine = true, returnSeen = false, contentTypeSeen = false, contentLengthSeen = false;
                        while ((str = br.readLine()) != null) {
                            if (firstLine) {
                                if (!str.startsWith("HTTP")) {
                                    headersPs.append("HTTP/1.1 200 OK\r\n");
                                } else {
                                    headersPs.append("\r\n");
                                }
                                firstLine = false;
                                continue;
                            } else if (!returnSeen && str.indexOf(":") != -1 && str.substring(0, str.indexOf(":")).equalsIgnoreCase("content-type")) {
                                contentTypeSeen = true;
                                headersPs.append(str + "\n");
                                continue;
                            } else if (!returnSeen && str.indexOf(":") != -1 && str.substring(0, str.indexOf(":")).equalsIgnoreCase("content-length")) {
                                contentLengthSeen = true;
                                headersPs.append(str + "\n");
                                continue;
                            } else if (!returnSeen && (str.equals("") || str.equals("\r") || str.equals("\n"))) {
                                returnSeen = true;
                                if (!contentTypeSeen) {
                                    headersPs.append("Content-Type: " + unknownContent + "\r\n\r\n");
                                    contentTypeSeen = true;
                                    continue;
                                }
                            } else if (!returnSeen) {
                                headersPs.append(str + "\n");
                                continue;
                            }
                            ps.append(str + "\n");
                        }
                        br.close();
                        if (!contentLengthSeen) {
                            headersPs = new StringBuffer(headersPs.toString().trim());
                            headersPs.append("\n");
                            headersPs.append("Content-Length: " + ps.length() + "\n");
                        }
                    } catch (IOException e) {
                        StringBuffer body = WebResponseCodes.createGenericResponse(wContext, WebResponseCodes.HTTP_INTERNAL_ERROR, "The requested resource '" + request + "' failed to run:<br/>" + Debug.getStackTrace(e) + "<p></p>\nPlease refer to server logs for information regarding why.");
                        response.setBody(body, allowGzip);
                        response.setHeaders(wContext.createHeader(null, WebResponseCodes.HTTP_INTERNAL_ERROR, response.getBodySize(), 0L, "text/html", false, allowGzip));
                        response.setResponse(WebResponseCodes.HTTP_INTERNAL_ERROR);
                        return response;
                    }
                }
                fileData = ps.toString().getBytes();
                response.setBody(fileData);
                if (headersPs.length() > 0) {
                    response.setHeaders(headersPs);
                } else {
                    response.setHeaders(wContext.createHeader(requestedDocument, WebResponseCodes.HTTP_OK, (int) fileData.length, lastModified, null, true, false));
                }
                response.setResponse(WebResponseCodes.HTTP_OK);
                return response;
            } else {
                FileChannel roChannel = null;
                try {
                    roChannel = new FileInputStream(sendFile).getChannel();
                } catch (FileNotFoundException e) {
                    StringBuffer body = WebResponseCodes.createGenericResponse(wContext, WebResponseCodes.HTTP_NOT_FOUND, "The requested resource '" + requestedDocument + "' was not found on this server.");
                    response.setBody(body, allowGzip);
                    response.setHeaders(wContext.createHeader(null, WebResponseCodes.HTTP_NOT_FOUND, response.getBodySize(), 0L, "text/html", false, allowGzip));
                    response.setResponse(WebResponseCodes.HTTP_NOT_FOUND);
                    return response;
                }
                StringBuffer headers = wContext.createHeader(requestedDocument, WebResponseCodes.HTTP_OK, (int) sendFile.length(), sendFile.lastModified(), null, true, false);
                ByteBuffer headersBuf = ByteBuffer.allocateDirect(headers.length());
                ByteBuffer buf = ByteBuffer.allocateDirect(2);
                headersBuf.put(headers.toString().getBytes());
                headersBuf.flip();
                buf.put(new String("\r\n").getBytes());
                buf.flip();
                SocketTools.sendBufferToSocket(headersBuf, wContext.getClientContext().getSocketChannel());
                SocketTools.sendBufferToSocket(buf, wContext.getClientContext().getSocketChannel());
                try {
                    Objects.free(headersBuf);
                } catch (Exception e) {
                    Debug.inform("Unable to free headers buffer: " + e.getMessage());
                }
                try {
                    Objects.free(buf);
                } catch (Exception e) {
                    Debug.inform("Unable to free data buffer: " + e.getMessage());
                }
                try {
                    RequestLogger.getDefault().log(wContext, WebResponseCodes.HTTP_OK, (int) roChannel.size());
                    if (wContext.getLabel() != null) {
                        BandwidthCache.getDefault().add(wContext.getLabel(), (int) roChannel.size());
                    }
                } catch (Exception e) {
                    Debug.inform("Unable to log request: " + e);
                }
                ByteBuffer roBuf = null;
                int fileLength = 0, sendChunks = Server.getMapBufferSize(), sendBlocks = Server.getMapBufferSize();
                try {
                    fileLength = (int) roChannel.size();
                } catch (IOException e) {
                    Debug.inform("Unable to get file length: " + e.getMessage());
                }
                if (sendChunks > fileLength) {
                    sendChunks = fileLength;
                }
                for (int position = 0; position < fileLength; ) {
                    if ((position + sendChunks) > fileLength) {
                        sendChunks = (fileLength - position);
                    }
                    try {
                        roBuf = roChannel.map(FileChannel.MapMode.READ_ONLY, position, sendChunks);
                    } catch (IOException e) {
                        break;
                    }
                    try {
                        SocketTools.sendBufferToSocket(roBuf, wContext.getClientContext().getSocketChannel());
                    } catch (KeepaliveException e) {
                        break;
                    }
                    try {
                        Objects.free(roBuf);
                    } catch (Exception e) {
                        Debug.inform("Unable to free memory mapped object: " + e.getMessage() + " (" + e + ")");
                    }
                    position += sendBlocks;
                }
                try {
                    Objects.free(roBuf);
                } catch (Exception e) {
                    Debug.inform("Unable to free memory mapped object: " + e.getMessage() + " (" + e + ")");
                }
                try {
                    roChannel.close();
                } catch (Exception e) {
                    Debug.debug("Unable to close mapped file: " + e);
                }
                headersBuf = null;
                buf = null;
                roBuf = null;
                roChannel = null;
                return null;
            }
        }
    }
