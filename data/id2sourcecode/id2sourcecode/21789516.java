    private static boolean loadLibrary() throws Throwable {
        String[] libs = new String[] { "rxtxSerial.dll", "librxtxSerial.so", "librxtxSerial.jnilib" };
        for (String lib : libs) {
            try {
                String[] ll = lib.split("\\.");
                String prefix = ll[0];
                String suffix = ll[1];
                File outFile = new File(lib);
                if (!outFile.exists()) {
                    System.out.println("Copying " + lib + " to the working directory...");
                    InputStream inputStream = SerialStream.class.getResource("/" + lib).openStream();
                    File rxtxDll = new File(lib);
                    FileOutputStream outputStream = new FileOutputStream(outFile);
                    byte[] array = new byte[8192];
                    int read = 0;
                    while ((read = inputStream.read(array)) >= 0) outputStream.write(array, 0, read);
                    outputStream.close();
                }
                System.load(outFile.getAbsolutePath());
            } catch (Throwable t) {
                System.err.println("SerialStream.loadLibrary: " + t.getMessage());
            }
        }
        return true;
    }
