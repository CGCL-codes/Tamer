        public void run() {
            try {
                HttpURLConnection theUrlConnection = (HttpURLConnection) url.openConnection();
                theUrlConnection.setConnectTimeout(1000000);
                theUrlConnection.setReadTimeout(1000000);
                theUrlConnection.setDoOutput(true);
                theUrlConnection.setDoInput(true);
                theUrlConnection.setUseCaches(false);
                theUrlConnection.setChunkedStreamingMode(1024);
                byte[] encodedPassword = (username + ":" + password).getBytes();
                BASE64Encoder encoder = new BASE64Encoder();
                theUrlConnection.setRequestProperty("Authorization", "Basic " + encoder.encode(encodedPassword));
                theUrlConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + Boundary);
                DataOutputStream httpOut = new DataOutputStream(theUrlConnection.getOutputStream());
                for (Map.Entry<String, File> entry : files.entrySet()) {
                    File f = entry.getValue();
                    String str = "--" + Boundary + "\r\n" + "Content-Disposition: form-data;name=\"" + entry.getKey() + "\"; filename=\"" + f.getName() + "\"\r\n" + "Content-Type:" + MimeTypeGuesser.getInstance().guessMimeType(f) + "\r\n" + "\r\n";
                    httpOut.write(str.getBytes());
                    FileInputStream uploadFileReader = new FileInputStream(f);
                    int numBytesToRead = 1024;
                    int availableBytesToRead;
                    while ((availableBytesToRead = uploadFileReader.available()) > 0) {
                        byte[] bufferBytesRead;
                        bufferBytesRead = availableBytesToRead >= numBytesToRead ? new byte[numBytesToRead] : new byte[availableBytesToRead];
                        uploadFileReader.read(bufferBytesRead);
                        lastIteraction = System.currentTimeMillis();
                        httpOut.write(bufferBytesRead);
                        httpOut.flush();
                    }
                    uploadFileReader.close();
                }
                httpOut.write(("\r\n--" + Boundary + "--\r\n").getBytes());
                httpOut.flush();
                httpOut.close();
                InputStream is = theUrlConnection.getInputStream();
                StringBuilder response = new StringBuilder();
                byte[] respBuffer = new byte[4096];
                while (is.read(respBuffer) >= 0) {
                    response.append(new String(respBuffer).trim());
                }
                is.close();
                theUrlConnection.disconnect();
                result = response.toString();
                finish = true;
            } catch (Exception e) {
                this.e = e;
            }
        }
