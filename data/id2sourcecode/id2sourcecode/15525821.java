    private static byte[] readBytes(File file) {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        try {
            FileInputStream fileinputstream = new FileInputStream(file);
            BufferedInputStream bufferedinputstream = new BufferedInputStream(fileinputstream);
            int i = 0;
            byte abyte0[] = new byte[16384];
            while ((i = bufferedinputstream.read(abyte0)) != -1) if (i > 0) bytearrayoutputstream.write(abyte0, 0, i);
            bufferedinputstream.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return bytearrayoutputstream.toByteArray();
    }
