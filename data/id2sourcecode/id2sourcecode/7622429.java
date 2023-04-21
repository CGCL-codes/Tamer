    public String exec(String[] cmd, boolean wait) throws Exception {
        StringBuffer tmp = new StringBuffer();
        for (String c : cmd) System.out.print(c + " ");
        System.out.print("\n");
        process = Runtime.getRuntime().exec(cmd);
        BufferedReader k = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader y = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        Thread t = new Thread() {

            public void run() {
                try {
                    process.waitFor();
                    running = false;
                } catch (Exception E) {
                }
            }
        };
        t.start();
        while ((y.ready() || k.ready()) || running) {
            String writeTmp = null;
            if (y.ready()) System.out.println(y.readLine());
            if (k.ready()) {
                writeTmp = k.readLine();
                tmp.append(writeTmp);
                System.out.println(writeTmp);
            }
        }
        t = null;
        return tmp.toString();
    }
