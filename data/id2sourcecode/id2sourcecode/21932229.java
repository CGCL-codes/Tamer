    public static final byte[] unzipData(byte[] in) {
        ByteArrayInputStream bais = null;
        GZIPInputStream gis = null;
        ByteArrayOutputStream baos = null;
        byte[] buffer = new byte[1024];
        try {
            bais = new ByteArrayInputStream(in);
            gis = new GZIPInputStream(bais);
            baos = new ByteArrayOutputStream();
            int read = 0;
            while ((read = gis.read(buffer)) != -1) baos.write(buffer, 0, read);
        } catch (Exception ioe) {
            buffer = null;
            ioe.printStackTrace();
            baos = null;
        } finally {
            try {
                if (gis != null) gis.close();
                if (bais != null) bais.close();
                if (baos != null) baos.close();
            } catch (Exception e) {
            }
        }
        return baos.toByteArray();
    }
