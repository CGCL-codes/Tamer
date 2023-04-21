    public static void main(String[] args) {
        if (args.length != 2) {
            throw new RuntimeException("Two arguments, the first specifying the JES 1.6.1 " + "install dir and the second defining a temporary directory to store the " + "converted items into, are required.");
        }
        File[] smtpMessages = new File(args[0], "smtp").listFiles();
        if (smtpMessages.length != 0) {
            File targetSMTP = new File(args[1], "smtp");
            if (!targetSMTP.mkdirs()) {
                throw new RuntimeException("Unable to create the temporary smtp directory, aborting...");
            }
            for (int i = 0; i < smtpMessages.length; i++) {
                File messageFile;
                StringBuilder sb;
                String uid = null;
                int j;
                do {
                    sb = new StringBuilder(8);
                    for (j = 0; j < 8; j++) {
                        sb.append(characters.charAt(random.nextInt(16)));
                    }
                    uid = sb.toString();
                    messageFile = new File(targetSMTP, "smtp" + uid + ".ser");
                    if (!messageFile.exists() && !new File(targetSMTP, "smtp" + uid + ".ser").exists()) break;
                } while (true);
                BufferedReader reader = null;
                FileWriter writer = null;
                try {
                    reader = new BufferedReader(new FileReader(smtpMessages[i]));
                    writer = new FileWriter(messageFile);
                    String line;
                    line = reader.readLine();
                    writer.write("X-JES-File-Version: " + Utils.FILE_VERSION);
                    writer.write(EOL);
                    writer.write("X-JES-UID: " + uid);
                    writer.write(EOL);
                    line = reader.readLine();
                    writer.write("X-JES-MAIL-FROM: " + line);
                    writer.write(EOL);
                    line = reader.readLine();
                    writer.write("X-JES-RCPT-TO: " + line);
                    writer.write(EOL);
                    line = reader.readLine();
                    writer.write("X-JES-Date: " + line);
                    writer.write(EOL);
                    writer.write("X-JES-8bitMIME: false");
                    writer.write(EOL);
                    line = reader.readLine();
                    writer.write("X-JES-Delivery-Date: " + line);
                    writer.write(EOL);
                    line = reader.readLine();
                    writer.write("X-JES-Delivery-Count: " + line);
                    writer.write(EOL);
                    line = reader.readLine();
                    while (line != null) {
                        writer.write(line);
                        writer.write(EOL);
                        line = reader.readLine();
                    }
                } catch (IOException ioe) {
                    System.out.println("Unable to convert message " + smtpMessages[i]);
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException ioe) {
                        }
                    }
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (IOException ioe) {
                        }
                    }
                }
            }
        }
        File users = new File(args[0], "conf" + File.separator + "user.conf");
        Properties properties = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(users);
            properties.load(fis);
        } catch (IOException ioe) {
            throw new RuntimeException("Unable to load users.conf");
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ioe) {
                }
            }
        }
        Properties converted = new Properties();
        Enumeration keys = properties.keys();
        String user, pwd;
        while (keys.hasMoreElements()) {
            user = (String) keys.nextElement();
            pwd = properties.getProperty(user);
            converted.put(user, (pwd.length() == 60 ? "{SHA}" : "") + properties.getProperty(user));
        }
        File confDirectory = new File(args[1], "conf");
        if (!confDirectory.mkdirs()) {
            throw new RuntimeException("Unable to create the temporary conf directory, aborting...");
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(confDirectory, "user.conf"));
            converted.store(fos, ConfigurationManager.USER_PROPERTIES_HEADER);
        } catch (IOException ioe) {
            System.out.println("Unable to load user.conf");
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ioe) {
                }
            }
        }
        File[] userMBXs = new File(args[0], "users").listFiles();
        if (userMBXs.length != 0) {
            File targetLocal = new File(args[1], "users");
            if (!targetLocal.mkdirs()) {
                throw new RuntimeException("Unable to create the temporary smtp directory, aborting...");
            }
            FilenameFilter ff = new FilenameFilter() {

                public boolean accept(File directory, String filename) {
                    if (filename.endsWith(".jmsg")) return true;
                    return false;
                }
            };
            for (int i = 0; i < userMBXs.length; i++) {
                if (userMBXs[i].isDirectory()) {
                    File[] userMessages = userMBXs[i].listFiles(ff);
                    if (userMessages.length > 0) {
                        File targetUser = new File(targetLocal, userMBXs[i].getName());
                        if (!targetUser.mkdirs()) {
                            throw new RuntimeException("Unable to create temporary user directory, aborting...");
                        }
                        for (int j = 0; j < userMessages.length; j++) {
                            File messageFile;
                            StringBuilder sb;
                            String uid = null;
                            int k;
                            do {
                                sb = new StringBuilder(8);
                                for (k = 0; k < 8; k++) {
                                    sb.append(characters.charAt(random.nextInt(16)));
                                }
                                uid = sb.toString();
                                messageFile = new File(targetUser, uid + ".loc");
                                if (!messageFile.exists() && !new File(targetUser, uid + ".loc").exists()) break;
                            } while (true);
                            try {
                                FileUtils.copyFile(userMessages[j], messageFile);
                            } catch (IOException ioe) {
                                throw new RuntimeException("Unable to create temporary user message, aborting...");
                            }
                        }
                    }
                }
            }
        }
        System.out.println("All convertion operations completed successfully");
    }
