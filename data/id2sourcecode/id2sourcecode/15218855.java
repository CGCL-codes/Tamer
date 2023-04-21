    public static byte[] getByteArray(InputStream inStream) throws IOOperationException {
        try {
            BufferedInputStream bufferedInStream = new BufferedInputStream(inStream);
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            BufferedOutputStream bufferedOutStream = new BufferedOutputStream(outStream);
            byte buffer[] = new byte[1024];
            int read = 0;
            while ((read = bufferedInStream.read(buffer)) != -1) {
                bufferedOutStream.write(buffer, 0, read);
            }
            bufferedOutStream.flush();
            byte retData[] = outStream.toByteArray();
            bufferedOutStream.close();
            bufferedInStream.close();
            outStream.close();
            return retData;
        } catch (IOException ex) {
            throw new IOOperationException("Could not get data from InputStream", ex);
        }
    }
