    public static void main(String[] args) throws IOException, InterruptedException {
        SimpleDateFormat sdf_log = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS");
        if (args.length < 2) {
            System.out.println("Error. Worng number of args.");
            return;
        }
        String id = Long.toHexString(startDate.getTimeInMillis());
        StringBuilder sb = new StringBuilder();
        FileOutputStream _out = new FileOutputStream("./log/runner." + id + ".log");
        out = _out.getChannel();
        File workDir = new File(args[0]);
        List command = new ArrayList();
        for (int i = 1; i < args.length; i++) {
            command.add(args[i]);
            sb.append(args[i] + " ");
        }
        prn("Work dir: " + workDir.getPath());
        prn("Command line: " + sb.toString());
        prn("Start date/time: " + sdf_log.format(startDate.getTime()));
        final ProcessBuilder builder = new ProcessBuilder(command);
        builder.redirectErrorStream(true);
        builder.directory(workDir);
        final Process p = builder.start();
        Thread waiter = new Thread() {

            public void run() {
                try {
                    p.waitFor();
                } catch (InterruptedException ex) {
                    return;
                }
                done = true;
            }
        };
        waiter.start();
        BufferedReader is = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = null;
        prn("======================================");
        while (!done && ((line = is.readLine()) != null)) {
            prn(line);
        }
        prn("======================================");
        prn("Program terminated!");
    }
