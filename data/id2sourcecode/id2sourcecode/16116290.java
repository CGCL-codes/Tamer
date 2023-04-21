    public static void encode(InputStream in, OutputStream out, Collection<Base64EncodeOptions> options) throws IOException {
        final Base64EncodeOutputStream bos = new Base64EncodeOutputStream(out, options, true);
        for (int v = in.read(); v != (-1); v = in.read()) bos.write(v);
        FileUtil.closeAll(bos);
        out.flush();
    }
