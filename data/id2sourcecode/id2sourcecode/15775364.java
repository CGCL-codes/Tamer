    public static byte[] gunzip(byte[] input) {
        try {
            ByteArrayInputStream bla = new ByteArrayInputStream(input);
            GZIPInputStream gzis = new GZIPInputStream(bla);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            int read;
            while ((read = gzis.read(buf)) > 0) out.write(buf, 0, read);
            out.flush();
            byte[] output = out.toByteArray();
            gzis.close();
            bla.close();
            out.close();
            return output;
        } catch (Exception e) {
            log.warn("", e);
        }
        return null;
    }
