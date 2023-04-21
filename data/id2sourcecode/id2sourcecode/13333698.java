    public void run() {
        while (true) {
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("[ERROR] Problem accepting connection: " + e);
                continue;
            }
            String filename = null;
            try {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                InputStream in = clientSocket.getInputStream();
                byte[] baUploadId = new byte[10];
                in.read(baUploadId);
                String uploadId = new String(baUploadId, 0, 10);
                UploadSession usSession = UploadSession.get(uploadId);
                System.out.println("[DEBUG] Got uploadId: >" + new String(baUploadId) + "< of size " + baUploadId.length + " > session = " + usSession);
                int namelength = in.read();
                byte[] name = new byte[namelength];
                in.read(name);
                filename = new String(name, 0, namelength);
                int lenlength = in.read();
                byte[] leng = new byte[lenlength];
                in.read(leng);
                long size = Long.valueOf(new String(leng)).longValue();
                byte[] buffer = new byte[BUFFER_SIZE];
                int len = 0;
                long count = 0;
                if ((usSession != null) && "marc21.xml".equals(filename)) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream((int) size);
                    System.out.print("[DEBUG] Reading MARC-XML file with size " + size);
                    if (size > 0) {
                        while ((len = in.read(buffer, 0, BUFFER_SIZE)) > 0) {
                            baos.write(buffer, 0, len);
                            count += len;
                            if (count >= size) {
                                break;
                            }
                        }
                    }
                    System.out.println(" done");
                    out.println("ACK");
                    baos.flush();
                    String xmlmarc = new String(baos.toString("UTF-8"));
                    usSession.setXmlMarc21(xmlmarc);
                    baos.close();
                } else if ((usSession != null) && "structure.xml".equals(filename)) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream((int) size);
                    System.out.print("[DEBUG] Reading structure XML file with size " + size);
                    if (size > 0) {
                        while ((len = in.read(buffer, 0, BUFFER_SIZE)) > 0) {
                            baos.write(buffer, 0, len);
                            count += len;
                            if (count >= size) {
                                break;
                            }
                        }
                    }
                    System.out.println(" done");
                    out.println("ACK");
                    baos.flush();
                    String xmlresource = new String(baos.toString("UTF-8"));
                    usSession.setXmlResource(xmlresource);
                    baos.close();
                } else if (usSession != null) {
                    String prefix = (filename.lastIndexOf('.') < 3) ? ((filename.length() < 3) ? "tmp" : filename) : filename.substring(0, filename.lastIndexOf('.') + 1);
                    String postfix = ((filename.lastIndexOf('.') < 0) || (filename.lastIndexOf('.') >= (filename.length() - 3))) ? null : filename.substring(filename.lastIndexOf('.'));
                    File file = File.createTempFile(prefix, postfix, RMI_DIR);
                    FileOutputStream fos = new FileOutputStream(file);
                    FileChannel fch = fos.getChannel();
                    ReadableByteChannel rbc = Channels.newChannel(in);
                    FileLock flock = fch.lock();
                    long pos = 0;
                    long cnt = 0;
                    System.out.print("[DEBUG] Reading file " + filename + " to file " + file.getName() + " with size " + size);
                    while ((cnt = fch.transferFrom(rbc, pos, size)) > 0) {
                        pos += cnt;
                        if (pos >= size) {
                            break;
                        }
                    }
                    flock.release();
                    System.out.println(" done");
                    out.println(file.getName());
                    fos.flush();
                    fos.close();
                    usSession.addFile(file);
                } else {
                    while ((len = in.read(buffer, 0, 1024)) > 0) {
                        count += len;
                        if (count >= size) {
                            break;
                        }
                    }
                    out.println("ERROR");
                }
                out.close();
                in.close();
                clientSocket.close();
            } catch (IOException ex) {
                System.out.println(RMI_DIR.getAbsolutePath());
                ex.printStackTrace();
            }
        }
    }
