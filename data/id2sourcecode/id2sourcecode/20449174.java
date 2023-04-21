    public boolean createImg(String fileName, InputStream in) {
        Log.d(D, "create img : " + fullFolder + fileName);
        File file = new File(fullFolder + fileName);
        boolean isSuccess = true;
        if (!file.exists()) {
            FileOutputStream fout = null;
            BufferedInputStream bin = new BufferedInputStream(in);
            int readLength = 0;
            byte[] buffer = new byte[1024];
            try {
                fout = new FileOutputStream(file);
                while ((readLength = bin.read(buffer, 0, buffer.length)) != -1) {
                    fout.write(buffer, 0, readLength);
                }
            } catch (Exception e) {
                e.printStackTrace();
                isSuccess = false;
                Log.d(D, "create file faile!");
            } finally {
                try {
                    fout.close();
                    bin.close();
                    in.close();
                    if (!isSuccess) {
                        if (file.exists()) {
                            file.delete();
                            Log.d(D, "delete file :" + fullFolder + fileName);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return isSuccess;
    }
