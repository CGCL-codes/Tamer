        public void run() {
            try {
                Random rnd = new Random();
                int id = -1;
                boolean complete = false;
                int sendTimeout = 10;
                int counter = 0;
                int[] generationHolder = new int[1];
                boolean receiveFinished = false;
                while (true) {
                    counter++;
                    int sendOffset = complete ? ph.getSendOffset() : ph.getAndResetToSendAckedCount(generationHolder);
                    byte[] sendBytes = ph.getSendBytes(sendTimeout, 512, false, generationHolder[0]);
                    if (sendBytes == null && receiveFinished) break; else if (sendBytes == null) sendBytes = new byte[0];
                    ByteArrayOutputStream baos_ = new ByteArrayOutputStream();
                    DataOutputStream rawData = new DataOutputStream(baos_);
                    rawData.writeInt(id);
                    if (id == -1) rawData.writeUTF(createParam);
                    rawData.writeInt(sendOffset);
                    rawData.writeInt(ph.getReceiveOffset());
                    rawData.writeInt(sendBytes.length == 0 ? timeoutValue : 10);
                    rawData.write(sendBytes);
                    String data = new EnglishWordCoder(baos_.toByteArray()).getPlainText();
                    String url = null;
                    StringBuffer payload = new StringBuffer();
                    int lastpos = 0, pos;
                    while ((pos = template.indexOf('#', lastpos)) != -1) {
                        if (pos != lastpos) {
                            payload.append(template.substring(lastpos, pos));
                        }
                        int pos2 = template.indexOf('#', pos + 1);
                        if (pos2 == -1) throw new RuntimeException("Missing #: " + template.substring(pos));
                        String name = template.substring(pos + 1, pos2);
                        if (name.equals("DATA")) {
                            payload.append(data);
                        } else if (name.equals("C")) {
                            payload.append(counter);
                        } else if (name.equals("POST")) {
                            if (url != null) throw new RuntimeException("More than one POST marker!");
                            url = payload.toString();
                            payload.setLength(0);
                        } else if (name.startsWith("R")) {
                            payload.append(rnd.nextInt(Integer.parseInt(name.substring(1))) + 1);
                        } else if (name.startsWith("D")) {
                            payload.append(generateRandom(rnd, Integer.parseInt(name.substring(1)), "0123456789"));
                        } else if (name.startsWith("H")) {
                            payload.append(generateRandom(rnd, Integer.parseInt(name.substring(1)), "0123456789abcdef"));
                        } else {
                            throw new RuntimeException("Invalid template variable: " + name);
                        }
                        lastpos = pos2 + 1;
                    }
                    if (lastpos != template.length()) {
                        payload.append(template.substring(lastpos));
                    }
                    if (url == null) {
                        url = payload.toString();
                        payload = null;
                    }
                    complete = false;
                    HttpURLConnection sender = (HttpURLConnection) new URL(url).openConnection();
                    if (payload != null) {
                        sender.setDoOutput(true);
                        OutputStream sndOut = sender.getOutputStream();
                        sndOut.write(payload.toString().getBytes("ISO-8859-1"));
                        sndOut.flush();
                    }
                    InputStream in = sender.getInputStream();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    final byte[] buf = new byte[4096];
                    int length;
                    while ((length = in.read(buf)) != -1) {
                        baos.write(buf, 0, length);
                    }
                    in.close();
                    String responseString = new String(baos.toByteArray(), "ISO-8859-1").replaceAll("\r\n", "\n");
                    if (!responseString.startsWith("<pre>\n") || !responseString.endsWith("\n</pre>\n")) throw new RuntimeException("Unexpected response string: " + responseString);
                    byte[] response = EnglishWordCoder.decode(responseString.substring(6, responseString.length() - 8).trim());
                    DataInputStream dis = new DataInputStream(new ByteArrayInputStream(response));
                    int flag = dis.readByte();
                    while (flag != 1 && flag != 2) {
                        outputStream.print((char) flag);
                        flag = dis.readByte();
                    }
                    if (id == -1) {
                        if (flag != 1) throw new RuntimeException("No ID given: " + flag);
                        id = dis.readInt();
                        outputStream.println("Connection established, ID=" + id);
                        flag = dis.readByte();
                    }
                    if (flag != 2) throw new IOException("Invalid flag: " + flag);
                    int roundtripBytes = dis.readInt();
                    flag = dis.readByte();
                    byte[] received = new byte[0];
                    if (flag == -1) {
                        receiveFinished = true;
                    } else if (flag == 0) {
                        int len = dis.readInt();
                        received = new byte[len];
                        dis.readFully(received);
                    } else {
                        throw new IOException("Invalid second flag: " + flag);
                    }
                    if (dis.readByte() != 42 || dis.read() != -1) {
                        throw new IOException("Invalid end marker");
                    }
                    if (received.length > 0) {
                        ph.receiveBytes(received, 0, received.length);
                    }
                    complete = (roundtripBytes == sendBytes.length);
                    if (!complete) errorStream.println("Packet not complete, " + roundtripBytes + " != " + sendBytes.length);
                    sendTimeout = received.length == 0 ? timeoutValue : 10;
                }
            } catch (Exception ex) {
                ex.printStackTrace(errorStream);
            }
        }
