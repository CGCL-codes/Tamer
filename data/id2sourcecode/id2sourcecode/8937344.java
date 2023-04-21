    public void download(String streamName, OutputStream output, int maxRetry) throws S3Exception, RemoteStreamException {
        RemoteStream stream;
        RemoteStreamInfo info;
        stream = new RemoteStream(_connection, _bucket, streamName);
        try {
            S3RetryHandler retry = new S3RetryHandler(maxRetry);
            S3Exception retryError = null;
            do {
                if (retryError != null) {
                    System.err.println("S3 error occured creating stream info record, retrying: " + retryError.getMessage());
                }
                try {
                    info = stream.getStreamInfo();
                    if (info == null) {
                        throw new RemoteStreamException.NoSuchStreamException("Stream \"" + streamName + "\" does not exist.");
                    }
                } catch (S3Exception s3e) {
                    retryError = s3e;
                    continue;
                }
                break;
            } while (retry.shouldRetry(retryError));
        } catch (S3Exception s3e) {
            throw new RemoteStreamException("S3 failure fetching stream info record for '" + streamName + "': " + s3e.getMessage(), s3e);
        }
        byte[] buffer = new byte[BUFFER_SIZE];
        for (long blockId = 0; ; blockId++) {
            try {
                S3Object block;
                MessageDigest blockDigest;
                InputStream input;
                boolean eof;
                int nread;
                block = fetchRemoteBlock(stream, blockId, maxRetry);
                if (block == null) {
                    break;
                }
                try {
                    blockDigest = MessageDigest.getInstance("md5");
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException("Missing MD5 algorithm!", e);
                }
                input = block.getInputStream();
                eof = false;
                while (!eof) {
                    nread = 0;
                    while (buffer.length - nread > (BUFFER_SIZE / 4)) {
                        int len;
                        len = input.read(buffer, nread, buffer.length - nread);
                        if (len < 0) {
                            eof = true;
                            break;
                        }
                        nread += len;
                    }
                    blockDigest.update(buffer, 0, nread);
                    output.write(buffer, 0, nread);
                }
                if (!Arrays.equals(blockDigest.digest(), block.getMD5())) {
                    throw new RemoteStreamException("S3 block " + Long.toString(blockId) + " checksum invalid.");
                }
            } catch (S3Exception e) {
                throw new RemoteStreamException("S3 failure fetching stream block " + Long.toString(blockId) + ": " + e.getMessage(), e);
            } catch (IOException e) {
                throw new RemoteStreamException("Fatal IO error handling stream block " + Long.toString(blockId) + ": " + e.getMessage(), e);
            }
        }
        try {
            output.flush();
        } catch (IOException e) {
            throw new RemoteStreamException("Error flushing output stream: " + e.getMessage(), e);
        }
    }
