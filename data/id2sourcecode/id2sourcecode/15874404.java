    private static Header getHeader(InputStream inputStream, File f) throws IOException {
        InputStream in64 = null;
        try {
            int firstByte = inputStream.read();
            boolean base64;
            if (firstByte == BASE64_ENCODING) {
                in64 = new Base64InputStream(inputStream);
                inputStream = in64;
                base64 = true;
            } else if (firstByte == BINARY_ENCODING) {
                base64 = false;
            } else {
                return null;
            }
            byte[] fileHash = new byte[FILEHASH_SIZE];
            for (int i = 0; i < FILEHASH_SIZE; i++) fileHash[i] = (byte) inputStream.read();
            int fileSize = 0;
            for (int i = 0; i < FILESIZE_SIZE; i++) fileSize = (fileSize << 8) + (0xFF & inputStream.read());
            int dataOffset = 0;
            for (int i = 0; i < DATAOFFSET_SIZE; i++) dataOffset = (dataOffset << 8) + (0xFF & inputStream.read());
            int dataChunkSize = 0;
            for (int i = 0; i < DATASIZE_SIZE; i++) dataChunkSize = (dataChunkSize << 8) + (0xFF & inputStream.read());
            byte[] dataHash = new byte[DATAHASH_SIZE];
            for (int i = 0; i < DATAHASH_SIZE; i++) dataHash[i] = (byte) inputStream.read();
            if (base64) {
                for (int i = 0; i < HEADER64_B64PAD_CNT; i++) inputStream.read();
            }
            MessageDigest md;
            try {
                md = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            }
            int nRead;
            int remaining = dataChunkSize;
            final int dataBytesBufferSize = 4096;
            byte[] dataBytes = new byte[dataBytesBufferSize];
            while ((remaining > 0) && (nRead = inputStream.read(dataBytes)) != -1) {
                if (remaining < nRead) {
                    nRead = remaining;
                }
                md.update(dataBytes, 0, nRead);
                remaining -= nRead;
            }
            byte[] actualDataHash = md.digest();
            if (!Arrays.equals(dataHash, actualDataHash)) return null;
            Header header = new Header(base64, fileHash, fileSize, dataOffset, dataChunkSize, dataHash, f);
            return header;
        } finally {
            if (null != in64) in64.close();
            inputStream.close();
        }
    }
