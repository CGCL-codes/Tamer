    public static OutputStream wrapOutputStream(OutputStream out, String compressionAlgorithm, String entryName) throws SQLException {
        try {
            if ("GZIP".equals(compressionAlgorithm)) {
                out = new GZIPOutputStream(out);
            } else if ("ZIP".equals(compressionAlgorithm)) {
                ZipOutputStream z = new ZipOutputStream(out);
                z.putNextEntry(new ZipEntry(entryName));
                out = z;
            } else if ("DEFLATE".equals(compressionAlgorithm)) {
                out = new DeflaterOutputStream(out);
            } else if ("LZF".equals(compressionAlgorithm)) {
                out = new LZFOutputStream(out);
            } else if (compressionAlgorithm != null) {
                throw Message.getSQLException(ErrorCode.UNSUPPORTED_COMPRESSION_ALGORITHM_1, compressionAlgorithm);
            }
            return out;
        } catch (IOException e) {
            throw Message.convertIOException(e, null);
        }
    }
