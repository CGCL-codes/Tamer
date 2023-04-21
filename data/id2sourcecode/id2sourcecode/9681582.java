    public void start(final String name) throws IOException {
        openSerialLine(name, "COM18");
        bb.SendMessage("CrossTigerPrimitiv:");
        bb.SendMessage("CrossTigerDriver:");
        bb.SendMessage("CamObjects2D:");
        bb.onMessage("CrossTigerDriver", new Action() {

            @Override
            public void action(String... args) {
                if (args.length > 1) {
                    if (args[1].equals("connect")) {
                        if (serialLine1 == null) {
                            try {
                                openSerialLine(name, "COM18");
                                bb.SendMessage("CrossTigerPrimitiv: " + autostart);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (args[1].equals("disconnect")) {
                        try {
                            closeSerialLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        bb.onMessage("CrossTigerPrimitiv", new Action() {

            @Override
            public void action(String... args) {
                for (int i = 1; i < args.length; i++) {
                    if (args[i].indexOf('=') > 0) {
                        String[] list = args[i].split("=");
                        set(list[0], Integer.parseInt(list[1]));
                    } else {
                        cmd(args[i]);
                    }
                }
            }
        });
        bb.onMessage("CamObjects2D", new Action() {

            long t1;

            @Override
            public void action(String... args) {
                if (args.length < 6) return;
                int n = Integer.parseInt(args[1]);
                if (n > 0) {
                    int arg = 2;
                    double mx = 0;
                    double my = 0;
                    int otype = Integer.parseInt(args[arg++]);
                    double confi = Double.parseDouble(args[arg++]);
                    double x = Double.parseDouble(args[arg++]);
                    double y = Double.parseDouble(args[arg++]);
                    int id = Integer.parseInt(args[9]);
                    if (id == 1 && (otype == 1 || otype == 2)) {
                        mx = x;
                        my = max(0, y - 200);
                    }
                    if (my > 0) {
                        double a = f2 * (atan2(my, mx) - PI / 2);
                        set("L", otype);
                        set("M", a);
                        t1 = System.currentTimeMillis();
                        my = 0;
                    }
                } else {
                    set("L", -1);
                    set("M", 3);
                }
            }
        });
        bb.onMessage("terror", new Action() {

            long t1;

            @Override
            public void action(String... args) {
                int seq = Integer.parseInt(args[1]);
                long mytime = 0xffffffffL & Long.parseLong(args[2]);
                int lw = 0xffff & (int) mytime;
                int hw = 0xffff & (int) (mytime >> 16);
                bb.SendMessage("stream: 90 " + seq + " " + mytime);
            }
        });
        Thread serialReadChannel1 = new Thread("readFromCrossTigerChannel1") {

            public void run() {
                try {
                    while (true) {
                        String line;
                        try {
                            line = inputStream1.readLine();
                            if (debugflag) System.out.println(line);
                            bb.SendMessage("stream: " + line);
                            if (line.startsWith("61\t")) {
                                setGateInfo(line);
                            }
                        } catch (IOException e1) {
                            sleep(1000);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        serialReadChannel1.setDaemon(true);
        serialReadChannel1.start();
        Thread serialWriteChannel2 = new Thread("writeToCrossTigerChannel2") {

            public void run() {
                try {
                    while (true) {
                        try {
                            if (outputStream2 == null) {
                            } else {
                                String line = getGateInfo();
                                if (line != null) {
                                    outputStream2.write(line.getBytes("ASCII"));
                                    outputStream2.flush();
                                } else {
                                    sleep(100);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        serialWriteChannel2.setDaemon(true);
        serialWriteChannel2.start();
        Thread serialReadChannel2 = new Thread("readFromCrossTigerChannel2") {

            public void run() {
                try {
                    while (true) {
                        String line;
                        try {
                            if (serialLine2 != null) {
                                line = inputStream2.readLine();
                                if (debugflag) System.out.println(line);
                                bb.SendMessage("stream: " + line);
                            }
                        } catch (IOException e1) {
                            sleep(1000);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        serialReadChannel2.setDaemon(true);
        serialReadChannel2.start();
        bb.SendMessage("CrossTigerPrimitiv: " + autostart);
        bb.startMessageLoop();
    }
