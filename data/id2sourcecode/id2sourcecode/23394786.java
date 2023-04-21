    public int sendFile(File file) {
        byte[] buffer = new byte[2048];
        try {
            DataInputStream fis = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
            while (true) {
                int read = 0;
                if (fis != null) {
                    read = fis.read(buffer);
                }
                if (read == -1) {
                    break;
                }
                dataOutput.write(buffer, 0, read);
            }
            dataOutput.close();
            fis.close();
            return 1;
        } catch (Exception e) {
            ExceptionHandler.handleExcptin(e);
            return 0;
        }
    }
