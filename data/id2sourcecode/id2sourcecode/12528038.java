    public static String getFileAsString(File inFile) throws FileOpException {
        try {
            FileInputStream fileInStream = new FileInputStream(inFile);
            BufferedInputStream bufferedInStream = new BufferedInputStream(fileInStream);
            ByteArrayOutputStream byteArrayOutStream = new ByteArrayOutputStream();
            byte buffer[] = new byte[1024];
            int read;
            while ((read = bufferedInStream.read(buffer)) != -1) {
                byteArrayOutStream.write(buffer, 0, read);
            }
            byteArrayOutStream.flush();
            String data = new String(byteArrayOutStream.toByteArray());
            byteArrayOutStream.close();
            bufferedInStream.close();
            fileInStream.close();
            return data;
        } catch (IOException ex) {
            throw new FileOpException("Could not read file: " + inFile.getAbsolutePath(), ex);
        }
    }
