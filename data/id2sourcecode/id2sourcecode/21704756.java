    public static boolean upgrayedd(String url) {
        int size = GetUrl.getFilesize(url);
        if (size <= 0) return false;
        String save = Main.PROG_DIR + "bump3_new.jar";
        BufferedInputStream in;
        URLConnection uc;
        try {
            uc = new URL(url).openConnection();
            uc.setConnectTimeout(Main.CONNECT_TIMEOUT);
            uc.setReadTimeout(Main.READ_TIMEOUT);
            uc.setRequestProperty("User-Agent", Main.USER_AGENT);
            in = new BufferedInputStream(uc.getInputStream());
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(save);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            return false;
        }
        int BYTE_SIZE = 2048;
        BufferedOutputStream bout = new BufferedOutputStream(fos, BYTE_SIZE);
        byte data[] = new byte[BYTE_SIZE];
        int count, current = 0, per = 0, perd = 0;
        Methods.status("upgrayedding...");
        Methods.p("\n" + Main.GR + "[+]" + Main.G + " Upgrayedding...");
        try {
            while ((count = in.read(data, 0, BYTE_SIZE)) != -1) {
                bout.write(data, 0, count);
                current += count;
                per = (100 * current) / size;
                Methods.progbar(per);
            }
            bout.close();
            in.close();
            Methods.p("\n" + Main.GR + "[+]" + Main.G + " Upgrayedd download complete");
        } catch (SocketTimeoutException ste) {
            return false;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        } finally {
            try {
                bout.close();
            } catch (IOException ioe) {
            }
            try {
                in.close();
            } catch (IOException ioe) {
            }
        }
        String newline = System.getProperty("line.separator");
        if (Main.OS.startsWith("Windows")) {
            try {
                FileWriter fw = new FileWriter(Main.PROG_DIR + "upgrayedd.vbs");
                fw.write("CreateObject(\"Wscript.Shell\").Run \"\"\"\" & WScript.Arguments(0) & \"\"\"\", 0, False");
                fw.close();
                fw = new FileWriter(Main.PROG_DIR + "upgrayedd.bat");
                fw.write("@echo off" + newline + "del \"" + Main.PROG_DIR + Main.FILE_NAME + "\"" + newline + "move \"" + Main.PROG_DIR + "bump3_new.jar\" \"" + Main.PROG_DIR + Main.FILE_NAME + "\"" + newline + "java -jar \"" + Main.PROG_DIR + Main.FILE_NAME + "\"" + newline + "del \"" + Main.PROG_DIR + "upgrayedd.vbs" + newline + "del \"" + Main.PROG_DIR + "upgrayedd.bat" + newline);
                fw.close();
                if (Main.theGUI != null) {
                    Main.theGUI.setVisible(false);
                    saveSettings("msg=BuMP3 has been \"upgrayedd\"-ed to the latest revision.\n\nEnjoy!");
                }
                Runtime.getRuntime().exec(new String[] { "wscript.exe", "upgrayedd.vbs", "upgrayedd.bat" });
                System.exit(0);
            } catch (IOException ioe) {
                ioe.printStackTrace();
                return false;
            }
        } else {
            try {
                FileWriter fw = new FileWriter(Main.PROG_DIR + "upgrayedd.sh");
                fw.write("#/bin/sh" + newline + "rm \"" + Main.PROG_DIR + Main.FILE_NAME + "\"" + newline + "mv \"" + Main.PROG_DIR + "bump3_new.jar\" \"" + Main.PROG_DIR + Main.FILE_NAME + "\"" + newline + "java -jar \"" + Main.PROG_DIR + Main.FILE_NAME + "\"" + newline + "rm \"" + Main.PROG_DIR + "upgrayedd.sh\"" + newline);
                fw.close();
                if (Main.theGUI != null) {
                    Main.theGUI.setVisible(false);
                    saveSettings("msg=BuMP3 has been \"upgrayedd\"-ed to the latest revision.\n\nEnjoy!");
                }
                if (!System.getProperty("user.name").equals("root")) {
                    JOptionPane.showMessageDialog(null, "the newest version of BuMP3 has been downloaded as \n" + "'bump3_new.jar' in the same folder as this program.\n\n" + "you are not running as root, so the shell script that\n" + "replaces the old .jar with the _new.jar may not run properly.\n\n" + "if BuMP3 does not reload, simply move bump3_new.jar in place of " + Main.FILE_NAME + "!", "BuMP3 - Linux is iffy", JOptionPane.WARNING_MESSAGE);
                }
                Runtime.getRuntime().exec(new String[] { "chmod", "+x", Main.PROG_DIR + "upgrayedd.sh" });
                Runtime.getRuntime().exec(new String[] { "sh", "upgrayedd.sh" });
                System.exit(0);
            } catch (IOException ioe) {
                ioe.printStackTrace();
                return false;
            }
        }
        return true;
    }
