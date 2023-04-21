    public boolean storeFile(InputStream is, String fileName) {
        if (null == is) {
            return false;
        }
        FileOutputStream fos = null;
        File outPutFile = new File(fileName);
        try {
            fos = new FileOutputStream(outPutFile);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            outPutFile.delete();
            return false;
        }
        byte[] buffer = new byte[1024];
        int readBytes = 0;
        try {
            while ((readBytes = is.read(buffer)) >= 0) {
                fos.write(buffer, 0, readBytes);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            outPutFile.delete();
            return false;
        } finally {
            try {
                if (fos != null) {
                    fos.flush();
                    fos.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
