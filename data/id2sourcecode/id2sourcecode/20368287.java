    public byte[] Final() {
        if (md5 != null) {
            return md5.Final();
        }
        switch(algo) {
            case ADLER32:
            case CRC32:
                return Long.toOctalString(checksum.getValue()).getBytes();
            case MD5:
            case MD2:
            case SHA1:
            case SHA256:
            case SHA384:
            case SHA512:
                return digest.digest();
        }
        return null;
    }
