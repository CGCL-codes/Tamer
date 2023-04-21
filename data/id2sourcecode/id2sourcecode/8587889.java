    public int receiveFile(String fileName) {
        byte[] buffer = new byte[2048];
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            DataOutputStream fio = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            while (true) {
                int read = 0;
                if (dataInput != null) {
                    read = dataInput.read(buffer);
                }
                if (read == -1) {
                    break;
                }
                fio.write(buffer, 0, read);
            }
            fio.close();
            dataInput.close();
            return 1;
        } catch (Exception e) {
            ExceptionHandler.handleExcptin(e);
            return 0;
        }
    }
