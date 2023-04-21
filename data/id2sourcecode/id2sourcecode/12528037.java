    private static void writeStream(File outFile, InputStream inStream) throws FileOpException {
        try {
            FileOutputStream fileOutStream = new FileOutputStream(outFile);
            BufferedOutputStream bufferedOutStream = new BufferedOutputStream(fileOutStream);
            BufferedInputStream bufferedInStream = new BufferedInputStream(inStream);
            byte buffer[] = new byte[1024];
            int read = -1;
            while ((read = bufferedInStream.read(buffer)) != -1) bufferedOutStream.write(buffer, 0, read);
            bufferedOutStream.flush();
            bufferedOutStream.close();
            bufferedInStream.close();
        } catch (IOException ex) {
            throw new FileOpException("Could not write file: " + outFile.getAbsolutePath(), ex);
        }
    }
