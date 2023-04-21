    public void test_11() {
        System.out.println("==test_11===");
        String cmnd1 = null;
        String cmnd2 = null;
        if (RuntimeAdditionalTest0.os.equals("Win")) {
            cmnd1 = RuntimeAdditionalTest0.cm + " /C cat \"" + RuntimeAdditionalTest0.textFile + "\"";
            String ttt = System.getProperty("vm.boot.class.path") + (System.getProperty("java.class.path").length() > 0 ? File.pathSeparator + System.getProperty("java.class.path") : "");
            cmnd2 = RuntimeAdditionalTest0.javaStarter + " -Xbootclasspath/a:" + ttt + " -cp " + ttt + " java.lang.RuntimeAdditionalSupport2";
        } else if (RuntimeAdditionalTest0.os.equals("Lin")) {
            cmnd1 = "/bin/cat -v \"" + RuntimeAdditionalTest0.libFile + "\"";
            String ttt = System.getProperty("vm.boot.class.path") + (System.getProperty("java.class.path").length() > 0 ? File.pathSeparator + System.getProperty("java.class.path") : "");
            cmnd2 = RuntimeAdditionalTest0.javaStarter + " -Xbootclasspath/a:" + ttt + " -cp " + ttt + " java.lang.RuntimeAdditionalSupport2";
            return;
        } else {
            fail("WARNING (test_1): unknown operating system.");
        }
        Process pi3 = null;
        Process pi4 = null;
        try {
            pi3 = Runtime.getRuntime().exec(cmnd1);
            pi4 = Runtime.getRuntime().exec(cmnd2);
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
            }
            pi3.getOutputStream();
            pi3.getErrorStream();
            java.io.InputStream is = pi3.getInputStream();
            java.io.OutputStream os4 = pi4.getOutputStream();
            pi4.getErrorStream();
            java.io.InputStream is4 = pi4.getInputStream();
            while (is4.available() == 0) {
            }
            Thread.sleep(1000);
            byte[] bbb5 = new byte[is4.available()];
            is4.read(bbb5);
            int c1 = 0;
            int c2 = 0;
            while (true) {
                try {
                    while ((is.available()) != 0) {
                        os4.write(is.read());
                        c1++;
                        os4.write(10);
                        c1++;
                        try {
                            os4.flush();
                        } catch (java.io.IOException e) {
                        }
                        is4.read();
                        is4.read();
                        c2 += 2;
                    }
                    pi3.exitValue();
                    break;
                } catch (IllegalThreadStateException e) {
                    continue;
                }
            }
            while (true) {
                try {
                    while ((is4.available()) != 0) {
                        c2++;
                        is4.read();
                    }
                    if (c2 == c1) {
                        if ((is4.available()) == 0) {
                        } else {
                            fail("test_11 FAILED.");
                        }
                        try {
                            pi4.exitValue();
                        } catch (IllegalThreadStateException e) {
                            pi4.destroy();
                            return;
                        }
                    } else if (is4.available() == 0) {
                        int i = 0;
                        for (; i < 500; i++) {
                            if (is4.available() != 0) {
                                break;
                            }
                            try {
                                Thread.sleep(10);
                            } catch (Exception e) {
                            }
                        }
                        if (i == 500 && is4.available() == 0) {
                            try {
                                pi4.exitValue();
                            } catch (IllegalThreadStateException e) {
                                pi4.destroy();
                            }
                            fail("test_11 FAILED.");
                        }
                    }
                } catch (IllegalThreadStateException e) {
                    continue;
                }
            }
        } catch (Exception eeee) {
            eeee.printStackTrace();
            fail("ERROR (test_11): unexpected exception.");
        }
        try {
            pi3.exitValue();
        } catch (IllegalThreadStateException e) {
            pi3.destroy();
        }
        try {
            pi4.exitValue();
        } catch (IllegalThreadStateException e) {
            pi4.destroy();
        }
    }
