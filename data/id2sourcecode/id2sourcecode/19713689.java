    public final void readHttpURL(HttpURLConnection url) throws IOException {
        long before, after;
        url.setAllowUserInteraction(true);
        verbose(commandName + ": Contacting the URL ...");
        url.connect();
        verbose(commandName + ": Connect. Waiting for reply ...");
        before = System.currentTimeMillis();
        DataInputStream in = new DataInputStream(url.getInputStream());
        after = System.currentTimeMillis();
        verbose(commandName + ": The reply takes " + ((int) (after - before) / 1000) + " seconds");
        before = System.currentTimeMillis();
        try {
            if (url.getResponseCode() != HttpURLConnection.HTTP_OK) {
                buffer.append(commandName + ": " + url.getResponseMessage());
            } else {
                printHeader(url);
                while (true) {
                    writeChar((char) in.readUnsignedByte());
                }
            }
        } catch (EOFException e) {
            after = System.currentTimeMillis();
            int milliSeconds = (int) (after - before);
            if (output) {
                verbose("\n");
            }
            verbose(commandName + ": Read " + count + " bytes from " + url.getURL());
            verbose(commandName + ": HTTP/1.0 " + url.getResponseCode() + " " + url.getResponseMessage());
            url.disconnect();
            verbose(commandName + ": It takes " + (milliSeconds / 1000) + " seconds" + " (at " + round(count / (float) milliSeconds) + " K/sec).");
            if (url.usingProxy()) {
                verbose(commandName + ": This URL uses a proxy");
            }
        } catch (IOException e) {
            buffer.append(e + ": " + e.getMessage());
            if (output) {
                verbose("\n");
            }
            verbose(commandName + ": I/O Error : Read " + count + " bytes from " + url.getURL());
            buffer.append(commandName + ": I/O Error " + url.getResponseMessage());
        }
    }
