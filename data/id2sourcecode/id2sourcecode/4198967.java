    public static byte[] getDtd(String dtdName) {
        try {
            URL dtd = new URL("resource:/openejb/dtds/" + dtdName);
            InputStream in = dtd.openStream();
            if (in == null) return null;
            byte[] buf = new byte[512];
            in = new BufferedInputStream(in);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int count;
            while ((count = in.read(buf)) > -1) out.write(buf, 0, count);
            in.close();
            out.close();
            return out.toByteArray();
        } catch (Throwable e) {
            return null;
        }
    }
