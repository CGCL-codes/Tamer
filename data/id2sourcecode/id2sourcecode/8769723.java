    static void dump() throws IOException {
        _clumpedFrameMap = new HashMap<String, Holder>();
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
        writer.println("+----------------------------------------------------------------------");
        writer.print("|  File: ");
        writer.println(fileName);
        writer.print("|  Date: ");
        writer.println(new SimpleDateFormat("yyyy.MM.dd HH:mm:ss a").format(now));
        writer.println("+----------------------------------------------------------------------");
        writer.println();
        if (!Controller._outputSummaryOnly) {
            dumpThreads(writer);
        }
        dumpFrames(writer);
        dumpClumpedFrames(writer);
        dumpAllocation(writer);
        writer.flush();
        out.close();
    }
