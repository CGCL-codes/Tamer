    public void run() {
        try {
            File putFile = websterInstance.parseFileName(fileName);
            String header;
            if (putFile.exists()) {
                header = "HTTP/1.0 200 OK\n" + "Allow: PUT\n" + "MIME-Version: 1.0\n" + "Server : Webster: a Java HTTP Server \n" + "\n\n <H1>200 File updated</H1>\n";
            } else {
                header = "HTTP/1.0 201 Created\n" + "Allow: PUT\n" + "MIME-Version: 1.0\n" + "Server : Webster: a Java HTTP Server \n" + "\n\n <H1>201 File Created</H1>\n";
            }
            FileOutputStream requestedFile = new FileOutputStream(putFile);
            InputStream in = client.getInputStream();
            int length = Integer.parseInt(ignoreCaseProperty(rheader, "Content-Length"));
            try {
                for (int i = 0; i < length; i++) {
                    requestedFile.write(in.read());
                }
            } catch (IOException e) {
                header = "HTTP/1.0 500 Internal Server Error\n" + "Allow: PUT\n" + "MIME-Version: 1.0\n" + "Server : Webster: a Java HTTP Server \n" + "\n\n <H1>500 Internal Server Error</H1>\n" + e;
            }
            DataOutputStream clientStream = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
            clientStream.writeBytes(header);
            clientStream.flush();
            requestedFile.close();
            clientStream.close();
        } catch (Exception e) {
            logger.log(Level.WARNING, "Closing Socket", e);
        } finally {
            try {
                client.close();
            } catch (IOException e2) {
                logger.log(Level.WARNING, "Closing incoming socket", e2);
            }
        }
    }
