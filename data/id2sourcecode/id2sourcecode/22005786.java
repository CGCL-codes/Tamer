    private int runcommand(String[] command) throws IOException, Exception {
        System.out.println();
        System.out.println();
        for (int i = 0; i < command.length - 1; i++) {
            System.out.print(command[i] + " ");
        }
        System.out.println(command[command.length - 1]);
        System.out.println();
        Process p = Runtime.getRuntime().exec(command);
        InputStream is = p.getInputStream();
        try {
            int b;
            while ((b = is.read()) != -1) System.out.write(b);
        } finally {
            is.close();
        }
        int exitCode = p.waitFor();
        debug("" + exitCode + ": PasteWorker exit code");
        return exitCode;
    }
