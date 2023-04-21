        @Override
        public void run() {
            try {
                LOG.info("restart:{}", ftpClient.getRestart());
                FileInputStream fis = new FileInputStream(new File(fileName));
                if (ftpClient.getRestart() != 0) {
                    fis.skip(ftpClient.getRestart());
                }
                OutputStream os = socket.getOutputStream();
                byte[] block = new byte[1024];
                for (int read = fis.read(block); read != -1; read = fis.read(block)) {
                    os.write(block, 0, read);
                }
                List<String> list = new ArrayList<String>();
                String msg = FtpResponse.FileSent.asString() + ClientCommand.SP + "File Sent" + ClientCommand.CRLF;
                list.add(msg);
                ClientResponse response = new ClientResponse(list);
                ftpClient.notifyListeners(response);
            } catch (Exception e) {
                LOG.error("Error sending file to server", e);
            }
        }
