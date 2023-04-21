    public static void logEvent(Object source, String info) {
        if (debugging) debugln("LOG: " + source + " / " + info);
        if (logging) {
            if (logfile == null) try {
                String path = System.getenv("user.home");
                logfile = new File(path, "collabed-" + new Date().getTime() + ".log");
                writer = new PrintWriter(new FileWriter(logfile));
            } catch (Exception e) {
                e.printStackTrace();
            }
            writer.println("SYSID: Thread[" + Thread.currentThread().getName() + "]" + "Class[" + source.getClass() + "]");
            writer.println(source + "[" + info + "]");
            writer.flush();
        }
    }
