        @Override
        public void run() {
            try {
                HTTPInputStream in = new HTTPInputStream(new BufferedInputStream(socket.getInputStream()));
                BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
                try {
                    String request = in.readAsciiLine();
                    if (request == null || !(request.endsWith(" HTTP/1.0") || request.endsWith("HTTP/1.1"))) {
                        writeHTTPError(out, 500, "Invalid Method.");
                        return;
                    }
                    boolean isPostMethod = false;
                    if (request.startsWith("POST ")) {
                        isPostMethod = true;
                    } else if (!request.startsWith("GET ")) {
                        writeHTTPError(out, 500, "Invalid Method.");
                        return;
                    }
                    String resourcePath = request.substring((isPostMethod ? "POST " : "GET ").length(), request.length() - " HTTP/1.0".length());
                    HTTPRequest httpRequest = new HTTPRequest(resourcePath);
                    httpRequest.setPostMethod(isPostMethod);
                    if (isPostMethod) {
                        HTTPData[] httpDataArray;
                        String contentType = null;
                        int contentLength = -1;
                        for (String header; (header = in.readAsciiLine()).length() > 0; ) {
                            if (header.startsWith("Content-Length: ")) {
                                contentLength = Integer.parseInt(header.substring("Content-Length: ".length()));
                            } else if (header.startsWith("Content-Type: ")) {
                                contentType = header.substring("Content-Type: ".length());
                            }
                        }
                        if (contentType != null && contentType.startsWith("multipart/")) {
                            byte[] dataBytes;
                            if (contentLength > 0) {
                                dataBytes = new byte[contentLength];
                                in.read(dataBytes);
                            } else {
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                byte[] bytes = new byte[1024];
                                for (int i; (i = in.read(bytes)) != -1; baos.write(bytes, 0, i)) {
                                }
                                dataBytes = baos.toByteArray();
                            }
                            String boundary = "--" + contentType.substring(contentType.indexOf("boundary=") + "boundary=".length());
                            byte[] boundaryBytes = boundary.getBytes("UTF-8");
                            List<Integer> indexList = new ArrayList<Integer>();
                            for (int i = 0; i < dataBytes.length - boundaryBytes.length; i++) {
                                boolean isFound = true;
                                for (int j = 0; j < boundaryBytes.length; j++) {
                                    if (dataBytes[i + j] != boundaryBytes[j]) {
                                        isFound = false;
                                        break;
                                    }
                                }
                                if (isFound) {
                                    indexList.add(i);
                                    i += boundaryBytes.length;
                                }
                            }
                            httpDataArray = new HTTPData[indexList.size() - 1];
                            for (int i = 0; i < httpDataArray.length; i++) {
                                HTTPData httpData = new HTTPData();
                                httpDataArray[i] = httpData;
                                int start = indexList.get(i);
                                ByteArrayInputStream bais = new ByteArrayInputStream(dataBytes, start, indexList.get(i + 1) - start - in.getLineSeparator().length());
                                HTTPInputStream din = new HTTPInputStream(bais);
                                din.readAsciiLine();
                                Map<String, String> headerMap = httpData.getHeaderMap();
                                for (String header; (header = din.readAsciiLine()).length() > 0; ) {
                                    String key = header.substring(header.indexOf(": "));
                                    String value = header.substring(key.length() + ": ".length());
                                    headerMap.put(key, value);
                                }
                                ByteArrayOutputStream aos = new ByteArrayOutputStream();
                                for (int n; (n = din.read()) != -1; aos.write(n)) {
                                }
                                httpData.setBytes(aos.toByteArray());
                            }
                        } else {
                            InputStreamReader reader = new InputStreamReader(in, "UTF-8");
                            String dataContent;
                            if (contentLength > 0) {
                                char[] chars = new char[contentLength];
                                int offset = 0;
                                while (chars.length > offset) {
                                    int n = reader.read(chars, offset, chars.length - offset);
                                    offset = n == -1 ? chars.length : offset + n;
                                }
                                dataContent = new String(chars);
                            } else {
                                StringBuilder sb = new StringBuilder();
                                char[] chars = new char[1024];
                                for (int i; (i = reader.read(chars)) != -1; sb.append(chars, 0, i)) {
                                }
                                dataContent = sb.toString();
                            }
                            HTTPData httpData = new HTTPData();
                            Map<String, String> headerMap = httpData.getHeaderMap();
                            for (String content : dataContent.split("&")) {
                                int eqIndex = content.indexOf('=');
                                if (eqIndex > 0) {
                                    String key = content.substring(0, eqIndex);
                                    String value = Utils.decodeURL(content.substring(eqIndex + 1));
                                    headerMap.put(key, value);
                                } else {
                                    headerMap.put(content, "");
                                }
                            }
                            httpDataArray = new HTTPData[] { httpData };
                        }
                        httpRequest.setHTTPPostDataArray(httpDataArray);
                    }
                    WebServerContent webServerContent = getWebServerContent(httpRequest);
                    InputStream resourceStream_ = null;
                    if (webServerContent != null) {
                        try {
                            resourceStream_ = webServerContent.getInputStream();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (resourceStream_ == null) {
                        if (DEBUG_PRINT_REQUESTS) {
                            System.err.println("Web Server " + (isPostMethod ? "POST" : "GET") + ": " + resourcePath + " -> 404 (not found)");
                        }
                        writeHTTPError(out, 404, "File Not Found.");
                        return;
                    }
                    if (DEBUG_PRINT_REQUESTS) {
                        System.err.println("Web Server " + (isPostMethod ? "POST" : "GET") + ": " + resourcePath + " -> 200 (OK)");
                    }
                    BufferedInputStream resourceStream = new BufferedInputStream(resourceStream_);
                    writeHTTPHeaders(out, 200, webServerContent.getContentType(), webServerContent.getContentLength(), webServerContent.getLastModified());
                    byte[] bytes = new byte[4096];
                    for (int i; (i = resourceStream.read(bytes)) != -1; out.write(bytes, 0, i)) {
                    }
                    try {
                        resourceStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } finally {
                    out.flush();
                    out.close();
                    in.close();
                    socket.close();
                }
            } catch (Exception e) {
            } finally {
                semaphore.release();
            }
        }
