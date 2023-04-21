    public void run() {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String command = in.readLine();
            if (command.startsWith("File:")) {
                File file = new File(command.substring(5));
                OutputStream out = null;
                try {
                    out = socket.getOutputStream();
                    if (file.isFile()) {
                        try {
                            out.write('K');
                            FileInputStream fileIn = new FileInputStream(file);
                            try {
                                byte[] buffer = new byte[1024];
                                int bytesread = 0;
                                while ((bytesread = fileIn.read(buffer)) != -1) {
                                    out.write(buffer, 0, bytesread);
                                }
                            } catch (IOException e) {
                                System.err.println("[" + this.getClass().getSimpleName() + "]: Failed to perform file transfer");
                            }
                            try {
                                fileIn.close();
                            } catch (IOException e) {
                                System.err.println("[" + this.getClass().getSimpleName() + "]: Failed to close file input stream");
                            }
                        } catch (IOException e) {
                            System.err.println("[" + this.getClass().getSimpleName() + "]: Failed to write file found status to output stream");
                        }
                    } else {
                        try {
                            out.write('N');
                        } catch (IOException e) {
                            System.err.println("[" + this.getClass().getSimpleName() + "]: Failed to write file not found status to socket output stream");
                        }
                    }
                } catch (IOException e) {
                    System.err.println("[" + this.getClass().getSimpleName() + "]: Failed to open socket output stream");
                } finally {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            System.err.println("[" + this.getClass().getSimpleName() + "]: Failed to close socket output stream");
                        }
                    }
                }
            } else if (command.startsWith("ServiceType?")) {
                PrintWriter out = null;
                try {
                    out = new PrintWriter(socket.getOutputStream());
                    out.println("EFRAFA");
                } catch (IOException e) {
                    System.err.println("[" + this.getClass().getSimpleName() + "]: Failed to respond to request for service type");
                } finally {
                    if (out != null) {
                        out.close();
                    }
                }
            } else {
                System.err.println("[" + this.getClass().getSimpleName() + "]: Unrecognised command received " + command);
            }
        } catch (IOException e) {
            System.err.println("[" + this.getClass().getSimpleName() + "]: Failed to read command from socket input stream");
        }
    }
