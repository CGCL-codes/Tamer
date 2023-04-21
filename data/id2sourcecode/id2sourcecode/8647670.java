    public static byte[] unzip(byte[] data) {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        try {
            try {
                GZIPInputStream zin = new GZIPInputStream(in);
                try {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    try {
                        int read;
                        byte[] buffer = new byte[1024];
                        while ((read = zin.read(buffer)) > 0) {
                            out.write(buffer, 0, read);
                        }
                    } finally {
                        out.close();
                    }
                    return out.toByteArray();
                } finally {
                    zin.close();
                }
            } finally {
                in.close();
            }
        } catch (IOException ex) {
            throw new LocalizableException(StringCodes.STRING_ERROR_CANNOT_UNZIP, ex);
        }
    }
