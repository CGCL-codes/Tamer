    public void run() {
        try {
            InputStream in = server.getInputStream();
            OutputStream out = server.getOutputStream();
            String request = "";
            String line = "";
            byte[] postData = new byte[0];
            while (true) {
                byte thisByte = (byte) in.read();
                if (thisByte == -1) {
                    break;
                }
                if (thisByte == '\n') {
                    if (line.length() == 0) {
                        break;
                    }
                    if (request == "") {
                        request = line;
                    } else {
                        int index = line.indexOf(": ");
                        if (index > 0) {
                            headers.put(line.substring(0, index), line.substring(index + 2, line.length()));
                        }
                    }
                    line = "";
                } else if (thisByte != '\r') {
                    line += (char) thisByte;
                }
            }
            headers.put("RemoteAddress", server.getInetAddress().getHostAddress());
            headers.put("LoopbackAddress", new Boolean(server.getInetAddress().isLoopbackAddress()).toString());
            int dataLength = 0;
            String contLength = (String) headers.get("Content-Length");
            if (contLength != null) {
                try {
                    dataLength = Integer.parseInt(contLength);
                } catch (Exception e) {
                }
            }
            if (dataLength > 0) {
                postDataBytes = new ByteArrayOutputStream();
                byte[] buff = new byte[1];
                int totaleRead = 0;
                while (totaleRead < dataLength) {
                    int read = in.read(buff);
                    if (read != -1) {
                        postDataBytes.write(buff, 0, read);
                        totaleRead += read;
                    } else {
                        break;
                    }
                }
                while (in.available() > 0) {
                    int read = in.read();
                    System.out.println("Removed Post Data Padding Byte = " + read);
                }
                postData = postDataBytes.toByteArray();
            }
            Thread.currentThread().setName(request);
            HTTPRequest httpRequest = new HTTPRequest(request, headers, postData, out);
            httpRequest.sendResponseData();
        } catch (Exception e) {
            System.out.println("Worker RequestObject Exception: " + e);
            e.printStackTrace();
        } finally {
            try {
                server.close();
            } catch (Exception e) {
            }
        }
    }
