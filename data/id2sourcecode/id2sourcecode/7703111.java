            public void run() {
                try {
                    synchronized (toContinue) {
                        toContinue = true;
                    }
                    server = new ServerSocket(17853);
                    System.out.println("Waiting for connection");
                    while (toContinue) {
                        try {
                            s = server.accept();
                        } catch (SocketException se) {
                            return;
                        }
                        System.out.println("Connected, waiting to receive");
                        BufferedInputStream in = new BufferedInputStream(s.getInputStream());
                        OutputStream out = s.getOutputStream();
                        while (s.isConnected()) {
                            if (!toContinue) break;
                            if (in.available() > 0) {
                                try {
                                    short header = (short) in.read();
                                    if (header == (short) 1) {
                                        int address = in.read();
                                        int special = in.read();
                                        int ichannel = ((special << 1) & 0x00000100) | address;
                                        int value = in.read();
                                        if ((special & 0x00000001) == 1) value = -100;
                                        System.out.println("Updating chan=" + ichannel + " val=" + value);
                                        Channel channel = new Channel((short) ichannel, (short) value);
                                        idc.updateChannel(channel);
                                    } else if (header == (short) 2) {
                                        idc.goToNextCue();
                                    } else if (header == (short) 3) {
                                    } else if (header == (short) 4) {
                                    } else if (header == (short) 5) {
                                        idc.resetAllChannels();
                                    } else if (header == (short) 6) {
                                        Channel[] channels = idc.getChannels();
                                        sendChannels(channels, idc.getChannelSources(channels), (byte) 6, out);
                                    } else if (header == (short) 7) {
                                        Channel[] channels = idc.getInputDeviceChannels();
                                        sendChannels(channels, idc.getChannelSources(channels), (byte) 7, out);
                                    } else if (header == (short) 255) {
                                        out.write((byte) 0xFF);
                                        in.close();
                                        out.close();
                                        s.close();
                                        break;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Thread.yield();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
