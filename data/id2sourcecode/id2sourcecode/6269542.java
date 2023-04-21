    private byte[] getDecodedStreamBytes() {
        InputStream input = getInputStreamForDecodedStreamBytes();
        if (input == null) return null;
        try {
            int outLength = Math.max(1024, (int) streamInput.getLength());
            ConservativeSizingByteArrayOutputStream out = new ConservativeSizingByteArrayOutputStream(outLength, library.memoryManager);
            byte[] buffer = new byte[(outLength > 1024) ? 4096 : 1024];
            while (true) {
                int read = input.read(buffer);
                if (read <= 0) break;
                out.write(buffer, 0, read);
            }
            out.flush();
            out.close();
            input.close();
            byte[] ret = out.toByteArray();
            return ret;
        } catch (IOException e) {
            logger.log(Level.FINE, "Problem decoding stream bytes: ", e);
        }
        return null;
    }
