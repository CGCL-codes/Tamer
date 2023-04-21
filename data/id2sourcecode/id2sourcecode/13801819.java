    private void transfer(InputStream is, String path, boolean append, OutputStream outs) throws IOException {
        if (((options & GassServer.CLIENT_SHUTDOWN_ENABLE) != 0) && path.indexOf(GassServer.SHUTDOWN_STR) != -1) {
            server.shutdown();
            return;
        }
        OutputStream out = null;
        String line;
        long length = 0;
        boolean chunked = false;
        boolean javaclient = false;
        do {
            line = readLine(is);
            if (DEBUG_ON) debug("header (put/post): " + line);
            if (line.startsWith(CONTENT_LENGTH)) {
                length = Long.parseLong(line.substring(line.indexOf(':') + 1).trim());
            } else if (line.startsWith(TRANSFER_ENCODING)) {
                chunked = true;
            } else if (line.startsWith(JAVA_CLIENT)) {
                javaclient = true;
            }
        } while ((line.length() != 0) && (line.charAt(0) != '\r') && (line.charAt(0) != '\n'));
        out = pickOutputStream(path, "/dev/stdout", System.out);
        if (out != null) {
            if ((options & GassServer.STDOUT_ENABLE) == 0) {
                throw new IOException("Bad Request");
            }
        } else {
            out = pickOutputStream(path, "/dev/stderr", System.err);
            if (out != null) {
                if ((options & GassServer.STDERR_ENABLE) == 0) {
                    throw new IOException("Bad Request");
                }
            } else {
                if ((options & GassServer.WRITE_ENABLE) == 0) {
                    throw new IOException("Bad Request");
                }
                path = decodeUrlPath(path);
                out = new FileOutputStream(path, append);
            }
        }
        if (javaclient) {
            writeln(outs, HTTP_CONTINUE);
        }
        byte[] buffer = new byte[BUFFER_SIZE];
        int read;
        if (!chunked) {
            while (length != 0) {
                read = is.read(buffer);
                if (read == -1) break;
                out.write(buffer, 0, read);
                length -= read;
            }
        } else {
            long chunkLength;
            int bytes;
            do {
                line = readLine(is);
                length = fromHex(line);
                if (DEBUG_ON) debug("chunk: '" + line + "' size:" + length);
                chunkLength = length;
                while (chunkLength != 0) {
                    if (chunkLength > buffer.length) {
                        bytes = buffer.length;
                    } else {
                        bytes = (int) chunkLength;
                    }
                    read = is.read(buffer, 0, bytes);
                    if (read == -1) break;
                    out.write(buffer, 0, read);
                    chunkLength -= read;
                }
                is.read();
                is.read();
            } while (length > 0);
            if (DEBUG_ON) debug("finished chunking");
        }
        out.flush();
        if (out == System.out || out == System.err) return;
        out.close();
    }
