    public static void threadDump() {
        SimpleDateFormat fmt = new SimpleDateFormat("MM-dd_HH-mm-ss");
        OutputStreamWriter out = null;
        try {
            File file = new File("ThreadDump-" + fmt.format(new Date()) + ".log");
            out = new FileWriter(file);
            Map traces = Thread.getAllStackTraces();
            for (Iterator iterator = traces.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry entry = (Map.Entry) iterator.next();
                Thread thread = (Thread) entry.getKey();
                out.write("Thread= " + thread.getName() + " " + (thread.isDaemon() ? "daemon" : "") + " prio=" + thread.getPriority() + "id=" + thread.getId() + " " + thread.getState());
                out.write("\n");
                StackTraceElement[] ste = (StackTraceElement[]) entry.getValue();
                for (int i = 0; i < ste.length; i++) {
                    out.write("\t");
                    out.write(ste[i].toString());
                    out.write("\n");
                }
                out.write("---------------------------------\n");
            }
            out.close();
            out = null;
            Logger.info("Full ThreadDump created " + file.getAbsolutePath());
        } catch (Throwable ignore) {
        } finally {
            IOUtils.closeQuietly(out);
        }
    }
