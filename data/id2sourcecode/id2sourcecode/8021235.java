        public void run() {
            try {
                String id;
                synchronized (globalID) {
                    id = globalID[0];
                }
                if (!id.startsWith("id=")) throw new RuntimeException(id);
                id = id.substring(3);
                int counter = 0;
                boolean complete = false;
                int[] generationHolder = new int[1];
                while (true) {
                    try {
                        counter++;
                        int off = complete ? ph.getSendOffset() : ph.getAndResetToSendAckedCount(generationHolder);
                        String url = baseURL + "counter=" + counter;
                        complete = false;
                        HttpURLConnection sender = (HttpURLConnection) new URL(url).openConnection();
                        sender.setDoOutput(true);
                        boolean doStreaming = timeout.length() == 0;
                        if (doStreaming) {
                            try {
                                sender.getClass().getMethod("setFixedLengthStreamingMode", new Class[] { int.class }).invoke(sender, new Object[] { new Integer(104857600) });
                            } catch (Exception ex) {
                                doStreaming = false;
                            }
                        }
                        OutputStream sndOut = sender.getOutputStream();
                        sndOut.write((id + "," + off + ";").getBytes("ISO-8859-1"));
                        int sentBytes = 0;
                        try {
                            byte[] buf;
                            while ((buf = ph.getSendBytes(15000, -1, true, generationHolder[0])) != null) {
                                sndOut.write(buf, 0, buf.length);
                                sentBytes += buf.length;
                                sndOut.flush();
                                if (!doStreaming) break;
                            }
                            if (buf == null) break;
                        } catch (IOException ex) {
                            ex.printStackTrace(errorStream);
                        } finally {
                            try {
                                sndOut.close();
                            } catch (IOException ex) {
                                if (!ex.getMessage().equals("insufficient data written")) ex.printStackTrace(errorStream);
                            }
                        }
                        InputStream in = sender.getInputStream();
                        StringBuffer sb = new StringBuffer();
                        int b;
                        while ((b = in.read()) != -1) {
                            sb.append((char) b);
                        }
                        in.close();
                        int value = Integer.parseInt(sb.toString());
                        complete = (value == sentBytes);
                        if (!complete) errorStream.println("Packet not complete, " + value + " != " + sentBytes);
                    } catch (Exception ex) {
                        ex.printStackTrace(errorStream);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace(errorStream);
            }
        }
