    public void run() {
        if (serverSocket == null) return;
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                BufferedOutputStream output = new BufferedOutputStream(socket.getOutputStream());
                File fileWorker = new File("/tmp/worker.xml");
                BufferedInputStream fileStream = new BufferedInputStream(new FileInputStream(fileWorker));
                int read = 0;
                while (read != -1) {
                    read = fileStream.read(buffer, 0, BUFFER_SIZE);
                    if (read != -1) {
                        output.write(buffer, 0, read);
                        output.flush();
                    }
                }
                output.close();
                fileStream.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
