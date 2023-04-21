    public XMLDataInputStream(InputStream is) throws OdaException {
        BufferedInputStream bis = new BufferedInputStream(is);
        try {
            File file = createFileWithUniqueName();
            FileOutputStream fos = new FileOutputStream(file);
            int abyte;
            while ((abyte = bis.read()) != -1) {
                fos.write(abyte);
            }
            fos.close();
            url = file.toURL();
            inputStream = new BufferedInputStream(url.openStream());
        } catch (IOException e) {
            throw new OdaException(e.getLocalizedMessage());
        }
    }
