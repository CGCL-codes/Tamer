    public static void readFileByRandomAccess(String fileName) {
        RandomAccessFile randomFile = null;
        try {
            System.out.println("����ȡһ���ļ����ݣ�");
            randomFile = new RandomAccessFile(fileName, "r");
            long fileLength = randomFile.length();
            int beginIndex = (fileLength > 4) ? 4 : 0;
            randomFile.seek(beginIndex);
            byte[] bytes = new byte[10];
            int byteread = 0;
            while ((byteread = randomFile.read(bytes)) != -1) {
                System.out.write(bytes, 0, byteread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (randomFile != null) {
                try {
                    randomFile.close();
                } catch (IOException e1) {
                }
            }
        }
    }
