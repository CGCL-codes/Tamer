    public boolean readURL(String urlString, PrintWriter writer) throws MalformedURLException {
        System.out.println("!!!!!!!!!!Session: readAndQueueURL " + urlString + "\n\n");
        URL url = new URL(urlString);
        try {
            HTTPConnection connection = new HTTPConnection();
            HTTPRequest request = new HTTPRequest(HTTP.GET, url);
            request.addHeader("User-Agent", "Cidero/1.0");
            request.addHeader("Accept", "*/*");
            request.addHeader("Icy-Metadata", "1");
            System.out.println("Added header");
            System.out.println("Request is:\n" + request.toString());
            HTTPResponse response = connection.sendRequest(request, false);
            writer.println("  HTTP Response:");
            writer.println("   " + response.getFirstLine());
            System.out.println("Response first line: " + response.getFirstLine());
            System.out.println("Response code: " + response.getStatusCode());
            for (int n = 0; n < response.getNumHeaders(); n++) {
                System.out.println(response.getHeader(n).toString());
                writer.println("   " + response.getHeader(n).toString());
            }
            int icy_metaint = -1;
            int readChunkSize = 8192;
            String icy_metaint_value = response.getHeaderValue("icy-metaint");
            if (icy_metaint_value != null) {
                icy_metaint = Integer.parseInt(icy_metaint_value);
                readChunkSize = icy_metaint;
            }
            int icy_br = -1;
            String icy_br_value = response.getHeaderValue("icy-br");
            if (icy_br_value != null) icy_br = Integer.parseInt(icy_br_value);
            long shoutcastBufSize = -1;
            long totalBytes = 0;
            long last = 0;
            if (response.getStatusCode() != HTTPStatus.OK) {
                response.releaseConnection();
                return false;
            }
            int bytes;
            byte[] buf = new byte[readChunkSize];
            byte[] metadataBuf = new byte[16384];
            BufferedInputStream inStream = (BufferedInputStream) response.getInputStream();
            long startTimeMillis = System.currentTimeMillis();
            long lastTimeMillis = startTimeMillis;
            long lastChunkTime = startTimeMillis;
            String metadataString = null;
            int consecNonZeroMetadata = 0;
            long loopCount = 0;
            while ((totalBytes < maxDataBytes) || (maxDataBytes < 0)) {
                int bytesRemaining = readChunkSize;
                while ((bytes = inStream.read(buf, readChunkSize - bytesRemaining, bytesRemaining)) > 0) {
                    bytesRemaining -= bytes;
                    if (bytesRemaining == 0) break;
                }
                long currTimeMillis = System.currentTimeMillis();
                long elapsedTime = currTimeMillis - startTimeMillis;
                long deltaT = currTimeMillis - lastTimeMillis;
                if ((loopCount % 8) == 0) {
                    System.out.println("read " + readChunkSize + " bytes - time:" + elapsedTime + "  deltaT: " + deltaT + " TotalBytes: " + totalBytes);
                    System.out.flush();
                }
                loopCount++;
                lastTimeMillis = currTimeMillis;
                if (bytes < 0) {
                    System.out.println("read returned -1 - done");
                    break;
                }
                if (icy_metaint > 0) {
                    int metadataBytes = inStream.read() * 16;
                    System.out.println("--- Metadata bytes = " + metadataBytes);
                    System.out.flush();
                    if (metadataBytes > 0) {
                        consecNonZeroMetadata++;
                        if (consecNonZeroMetadata > 3) {
                            System.out.println("More than 3 consec non-zero metadata blocks!");
                            System.out.println("TotalBytes = " + totalBytes);
                            System.out.flush();
                            System.exit(-1);
                        }
                        bytesRemaining = metadataBytes;
                        while (bytesRemaining > 0) {
                            bytes = inStream.read(metadataBuf, metadataBytes - bytesRemaining, bytesRemaining);
                            if (bytes < 0) {
                                System.out.println("--- Error reading metadata ");
                                return false;
                            }
                            bytesRemaining -= bytes;
                        }
                        String tmpMetadataString = new String(metadataBuf, 0, metadataBytes);
                        System.out.println("Metadata: [" + tmpMetadataString + "]");
                        System.out.flush();
                        if (metadataString == null) {
                            metadataString = tmpMetadataString;
                            writer.println("  Metadata: [" + metadataString + "]");
                        }
                    } else {
                        consecNonZeroMetadata = 0;
                    }
                }
                totalBytes += readChunkSize;
                if ((totalBytes - last) > 32768) {
                    long rate = (totalBytes - last) * 1000 / (currTimeMillis - lastChunkTime);
                    last = totalBytes;
                    lastChunkTime = currTimeMillis;
                }
                if ((totalBytes > 64 * 1024) && (deltaT > 300)) {
                    if (shoutcastBufSize < 0) shoutcastBufSize = totalBytes;
                }
            }
            writer.println("  TotalBytes snooped: " + totalBytes);
            writer.print("  Apparent shoutcast bufSize: " + shoutcastBufSize);
            if (icy_br > 0) {
                writer.print(" ( " + (shoutcastBufSize * 8) / icy_br + " msec @" + icy_br + "k )");
            }
            writer.println("");
            response.releaseConnection();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Session: readURL: Exception" + e);
            return false;
        }
        return true;
    }
