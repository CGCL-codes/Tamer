    public void reciveFile(String ip, int filePort, String fileName) {
        byte[] buffer = new byte[2048];
        try {
            Socket socket = new Socket(ip, filePort);
            DataInputStream dataInput = new DataInputStream(socket.getInputStream());
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
        } catch (Exception e) {
            ExceptionHandler.handleExcptin(e);
        }
    }
