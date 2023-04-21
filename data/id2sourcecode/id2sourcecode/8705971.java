        public void actionPerformed(ActionEvent evt) {
            int index = table.getSelectedRow();
            if (index != -1) {
                WorkerServiceInterface proxy = model.getProxyAt(index);
                String ipProxy = model.getProxyIpAt(index);
                proxy.getLastLog();
                try {
                    Socket socket = new Socket(ipProxy, 2000);
                    BufferedInputStream input = new BufferedInputStream(socket.getInputStream());
                    File tempFile = new File("/tmp/worker-temp.xml");
                    FileOutputStream fileStream = new FileOutputStream(tempFile);
                    int read = 0;
                    while (read != -1) {
                        read = input.read(buffer, 0, BUFFER_SIZE);
                        if (read != -1) {
                            fileStream.write(buffer, 0, read);
                            fileStream.flush();
                        }
                    }
                    if (read != 0) {
                        fileStream.write(new String("</log>\n").getBytes());
                    }
                    input.close();
                    fileStream.close();
                    socket.close();
                    parseXml(tempFile, "worker " + ipProxy);
                    tempFile.delete();
                } catch (SocketException ex) {
                    System.out.println("Unable to contact this worker! " + "Is this a version recent enough? Network Problem?");
                } catch (IOException ex) {
                    System.out.println("Socket Error with this worker!");
                }
            }
        }
