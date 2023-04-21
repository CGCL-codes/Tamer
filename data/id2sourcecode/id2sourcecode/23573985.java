    public static byte[] readBytes(String sourceFileName) {
        ByteArrayOutputStream outBuffer = null;
        FileInputStream inFile = null;
        BufferedInputStream bufInputStream = null;
        try {
            outBuffer = new ByteArrayOutputStream();
            inFile = new FileInputStream(sourceFileName);
            bufInputStream = new BufferedInputStream(inFile);
            byte[] tmpBuffer = new byte[8 * 1024];
            int n = 0;
            while ((n = bufInputStream.read(tmpBuffer)) >= 0) outBuffer.write(tmpBuffer, 0, n);
        } catch (FileNotFoundException fnf) {
            ClientLogger.getInstance().error("[readBytes] File not found :" + fnf.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inFile != null) {
                try {
                    inFile.close();
                } catch (IOException ioex) {
                }
            }
            if (outBuffer != null) {
                try {
                    outBuffer.close();
                } catch (IOException ioex) {
                }
            }
            if (bufInputStream != null) {
                try {
                    bufInputStream.close();
                } catch (IOException ioex) {
                }
            }
        }
        return outBuffer.toByteArray();
    }
