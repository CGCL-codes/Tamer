    public static void runToFile(String command, String dir, String outputfile) throws IOException {
        byte[] puffer = new byte[10000];
        File fdir = null;
        if (dir != null) fdir = new File(dir);
        Process prozess = Runtime.getRuntime().exec(command, getEnvStrArr(), fdir);
        BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(outputfile));
        int anzahl = -1;
        BufferedInputStream input = new BufferedInputStream(prozess.getInputStream());
        BufferedInputStream error = new BufferedInputStream(prozess.getErrorStream());
        while (true) {
            while (input.available() > 0) {
                output.write(input.read());
            }
            while (error.available() > 0) {
                output.write(error.read());
            }
            try {
                int wert = prozess.exitValue();
                break;
            } catch (IllegalThreadStateException ex) {
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
            }
        }
        output.flush();
        output.close();
        input.close();
        error.close();
        prozess.getOutputStream().close();
    }
