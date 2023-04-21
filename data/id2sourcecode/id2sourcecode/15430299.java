    private static void getVMDKFile(boolean put, String fileName, String uri, long diskCapacity) {
        HttpsURLConnection conn = null;
        BufferedOutputStream bos = null;
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 64 * 1024;
        try {
            if (LOG_ENABLED) {
                System.out.println("Destination host URL: " + uri);
            }
            HostnameVerifier hv = new HostnameVerifier() {

                public boolean verify(String urlHostName, SSLSession session) {
                    if (LOG_ENABLED) {
                        System.out.println("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
                    }
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(hv);
            URL url = new URL(uri);
            conn = (HttpsURLConnection) url.openConnection();
            List cookies = (List) headers.get("Set-cookie");
            cookieValue = (String) cookies.get(0);
            StringTokenizer tokenizer = new StringTokenizer(cookieValue, ";");
            cookieValue = tokenizer.nextToken();
            String path = "$" + tokenizer.nextToken();
            String cookie = "$Version=\"1\"; " + cookieValue + "; " + path;
            Map map = new HashMap();
            map.put("Cookie", Collections.singletonList(cookie));
            ((BindingProvider) vimPort).getRequestContext().put(MessageContext.HTTP_REQUEST_HEADERS, map);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setChunkedStreamingMode(maxBufferSize);
            if (put) {
                conn.setRequestMethod("PUT");
                if (LOG_ENABLED) {
                    System.out.println("HTTP method: PUT");
                }
            } else {
                conn.setRequestMethod("POST");
                if (LOG_ENABLED) {
                    System.out.println("HTTP method: POST");
                }
            }
            conn.setRequestProperty("Cookie", cookie);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/x-vnd.vmware-streamVmdk");
            conn.setRequestProperty("Content-Length", String.valueOf(diskCapacity));
            conn.setRequestProperty("Expect", "100-continue");
            bos = new BufferedOutputStream(conn.getOutputStream());
            if (LOG_ENABLED) {
                System.out.println("Local file path: " + fileName);
            }
            long fileSize = new File(fileName).length();
            InputStream io = new FileInputStream(fileName);
            BufferedInputStream bis = new BufferedInputStream(io);
            bytesAvailable = bis.available();
            if (LOG_ENABLED) {
                System.out.println("vmdk available bytes: " + bytesAvailable);
            }
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            bytesRead = bis.read(buffer, 0, bufferSize);
            long bytesWrote = bytesRead;
            TOTAL_BYTES_WRITTEN += bytesRead;
            while (bytesRead >= 0) {
                bos.write(buffer, 0, bufferSize);
                bos.flush();
                bytesAvailable = bis.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesWrote += bufferSize;
                TOTAL_BYTES_WRITTEN += bufferSize;
                buffer = null;
                buffer = new byte[bufferSize];
                bytesRead = bis.read(buffer, 0, bufferSize);
                if ((bytesRead == 0) && (bytesWrote >= diskCapacity)) {
                    bytesRead = -1;
                }
                if (LOG_ENABLED) {
                    System.out.println("Progress: " + (TOTAL_BYTES_WRITTEN * 100.00 / fileSize));
                }
            }
            try {
                DataInputStream dis = new DataInputStream(conn.getInputStream());
                dis.close();
            } catch (SocketTimeoutException stex) {
                if (LOG_ENABLED) {
                    System.out.println("From (ServerResponse): " + stex);
                }
            } catch (IOException ioex) {
                if (LOG_ENABLED) {
                    System.out.println("From (ServerResponse): " + ioex);
                }
            }
            if (LOG_ENABLED) {
                System.out.println("Writing vmdk to the output stream done");
            }
            bis.close();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                bos.flush();
                bos.close();
                conn.disconnect();
            } catch (SOAPFaultException sfe) {
                printSoapFaultException(sfe);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
