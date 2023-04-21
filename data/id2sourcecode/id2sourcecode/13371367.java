    public ReflectorMessage getMessage(MachineID recipientMachine, ReflectorMessageListener listener) throws ReflectorException {
        if (listener == null) {
            listener = new ReflectorMessageAdapter();
        }
        PostMethod post = getPostMethod(MSGTYPE_GETMESSAGE, recipientMachine);
        try {
            listener.executing();
            if (!post.execute()) {
                post.flush();
                listener.failed("Invalid response from server");
                logger.log(Level.WARNING, "Invalid response: HTTP Status of " + post.execute());
                throw new ReflectorException("Could not get message - " + post.getStatusCode());
            }
            String filename = post.getResponseHeader("cclearly-file");
            if (filename == null) {
                listener.noMessage();
                return null;
            }
            String sizeHeader = post.getResponseHeader("cclearly-size");
            int size = 0;
            if (sizeHeader != null) {
                size = Integer.valueOf(sizeHeader);
            }
            String responseMd5 = post.getResponseHeader("message-md5");
            if (responseMd5 == null) {
                throw new ReflectorException("No md5 checksum header found from reflector.");
            }
            if ((filename == null) || (filename.length() == 0)) {
                return null;
            }
            listener.startReceiving(filename, size);
            ReflectorMessage result = new ReflectorMessage();
            InputStream responseBodyAsStream = post.getResponseBodyAsStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int readSize = 0;
            int position = 0;
            byte[] data = new byte[4096];
            while ((readSize = responseBodyAsStream.read(data)) != -1) {
                out.write(data, 0, readSize);
                position += readSize;
                listener.progress(filename, position, size);
            }
            listener.finishedReceiving(filename);
            String md5 = MD5.md5(out.toByteArray());
            if (md5 == null) {
                throw new ReflectorException("Could not md5 message");
            }
            if (!md5.equals(responseMd5)) {
                throw new ReflectorException("Checksums from reflector does not match: Download - " + md5 + ", Header - " + responseMd5);
            }
            result.setByteMessage(new DataInputStream(new ByteArrayInputStream(out.toByteArray())));
            PostMethod reply = getPostMethod(MSGTYPE_MESSAGERECEIVED, recipientMachine);
            reply.setParameter("filename", filename);
            listener.startRemoving(filename);
            executePostAndProcess(reply);
            listener.finishedRemoving(filename);
            return result;
        } catch (IOException e) {
            listener.failed("IO Problem: " + e.getMessage());
            throw new ReflectorException(e);
        } catch (IllegalStateException e) {
            listener.failed("Illegal State: " + e.getMessage());
            throw new ReflectorException(e);
        } finally {
            post.releaseConnection();
        }
    }
