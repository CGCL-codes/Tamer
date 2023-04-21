    public static boolean writeFile(String filePath, String content, int off, int len) {
        File file = new File(filePath);
        if (file.exists()) {
            FileOutputStream outputStream = null;
            OutputStreamWriter outputWriter = null;
            BufferedWriter bufWriter = null;
            try {
                outputStream = new FileOutputStream(filePath);
                outputWriter = new OutputStreamWriter(outputStream, JavaCenterHome.JCH_CHARSET);
                bufWriter = new BufferedWriter(outputWriter);
                FileLock fl = outputStream.getChannel().tryLock();
                if (fl.isValid()) {
                    bufWriter.write(content, off, len);
                    fl.release();
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    bufWriter.close();
                    outputWriter.close();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
