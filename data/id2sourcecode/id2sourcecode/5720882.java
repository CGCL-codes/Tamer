    public static TraceFile createTraceFile(InputStream is) throws IOException {
        byte[] buf = new byte[4096];
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        for (int read = 0; (read = is.read(buf)) > 0; ) os.write(buf, 0, read);
        is = new ByteArrayInputStream(os.toByteArray());
        if (ABIFile.isABI(is)) return new ABIFile(is); else if (SCFFile.isSCF(is)) return new SCFFile(is); else throw new IOException("Unsupported file format");
    }
