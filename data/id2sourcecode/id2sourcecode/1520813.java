    public static byte[] getResourceAsBytes(String name) throws IOPException {
        try {
            InputStream ins = getResourceAsStream(name);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int i = 0;
            byte abyte0[] = new byte[1024];
            while ((i = ins.read(abyte0, 0, 1024)) > 0) bos.write(abyte0, 0, i);
            return bos.toByteArray();
        } catch (IOPException e) {
            throw e;
        } catch (Exception e) {
            throw new IOPException(PCCWBaseException.CODE_RUNTIME, "Unable to getResourceAsBytes '" + name + "'", e);
        }
    }
