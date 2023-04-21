    public static void encryptClasses(File directory) {
        for (File file : directory.listFiles()) {
            String filename = file.getPath();
            if (filename.endsWith(".class") && !filename.endsWith(EXTENSION)) {
                FileInputStream in = null;
                FileOutputStream out = null;
                byte[] bytecode = null;
                try {
                    in = new FileInputStream(file);
                    out = new FileOutputStream(filename.replace(".class", EXTENSION));
                    bytecode = cokefolk(in, Cipher.ENCRYPT_MODE);
                    System.out.println("Outputting encrypted " + filename);
                    out.write(bytecode);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(1);
                } finally {
                    if (in != null) try {
                        in.close();
                    } catch (IOException e) {
                    }
                    if (out != null) try {
                        out.close();
                    } catch (IOException e) {
                    }
                }
            }
            if (file.isDirectory()) {
                encryptClasses(file);
            }
        }
    }
