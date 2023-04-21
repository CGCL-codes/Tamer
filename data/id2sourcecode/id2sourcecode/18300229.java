    public static void dump() throws IOException {
        System.err.println("TimeLine Profiler: generating output");
        String fileName = null;
        File f = new File(Controller._fileName);
        Date now = new Date();
        if (f.isDirectory()) {
            StringBuffer b = new StringBuffer(f.getAbsolutePath());
            b.append(File.separator);
            b.append(new SimpleDateFormat("yyyyMMdd-HHmmss").format(now));
            b.append(".txt");
            fileName = b.toString();
        } else {
            if (Controller._fileName.endsWith(".txt")) {
                fileName = Controller._fileName;
            } else {
                StringBuffer b = new StringBuffer(Controller._fileName);
                b.append(".txt");
                fileName = b.toString();
            }
        }
        FileWriter out = new FileWriter(fileName);
        BufferedWriter bufferedWriter = new BufferedWriter(out);
        PrintWriter writer = new PrintWriter(bufferedWriter);
        for (TimeRecord rec : TimeLineProfiler._timeLine) {
            writer.print("Time: ");
            writer.print(rec._pointInTime - TimeLineProfiler._startTime);
            if (Controller._timeResoltion == Controller.TimeResolution.ns) {
                writer.println(" ns.");
            } else {
                writer.println(" ms.");
            }
            for (ActionRecord act : rec._actionRecordList) {
                writer.print('\t');
                if (act.getAction() == Action.START) {
                    writer.print("START");
                } else if (act.getAction() == Action.STOP) {
                    writer.print("END");
                } else if (act.getAction() == Action.ALLOC) {
                    writer.print("ALLOC");
                } else if (act.getAction() == Action.BEGIN_WAIT) {
                    writer.print("W:START");
                } else if (act.getAction() == Action.END_WAIT) {
                    writer.print("W:END");
                } else if (act.getAction() == Action.EXCEPTION) {
                    writer.print("THROWS");
                } else {
                    writer.print("???");
                }
                writer.print('\t');
                writer.print('[');
                writer.print(act.getThreadId());
                writer.print("]\t");
                String className = act.getClassName().replace('/', '.');
                int index = className.lastIndexOf('.');
                String shortName = null;
                String packageName = "";
                if (index > -1) {
                    shortName = className.substring(index + 1);
                    packageName = className.substring(0, index);
                } else {
                    shortName = className;
                }
                StringBuffer b = new StringBuffer();
                b.append(shortName);
                if (act.getMethodName().length() > 0) {
                    b.append(':');
                    b.append(act.getMethodName());
                }
                b.append("\t(");
                b.append(packageName);
                b.append(")");
                writer.println(b.toString());
            }
        }
        writer.flush();
        writer.close();
    }
