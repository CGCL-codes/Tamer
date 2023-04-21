    @SuppressWarnings("unused")
    static void rmipc(String[] ipcsCommand, String[] ipcrmCommand) throws Exception {
        StringWriter writer = new StringWriter();
        ProcessBuilder pb = new ProcessBuilder(ipcsCommand);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        ReaperThread thread = new ReaperThread(writer, process.getInputStream());
        thread.setDaemon(true);
        thread.start();
        int exitV = process.waitFor();
        if (exitV != 0) {
            System.out.println("run ipcs failed ,errorcode =" + exitV);
            System.exit(exitV);
        }
        Thread.sleep(500);
        String ipcsmOutput = writer.toString();
        List<String> ipcids = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new StringReader(ipcsmOutput));
        String line = null;
        while ((line = reader.readLine()) != null) {
            String ipcid = parseLine(line);
            if (ipcid != null) {
                ipcids.add(ipcid);
            }
        }
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < ipcsCommand.length; i++) {
            buffer.append(ipcsCommand[i]);
            buffer.append(" ");
        }
        buffer.append("returns ");
        buffer.append(ipcids);
        System.out.println(buffer);
        for (String ipcid : ipcids) {
            List<String> crmCommands = new ArrayList<String>();
            crmCommands.addAll(Arrays.asList(ipcrmCommand));
            crmCommands.add(ipcid);
            ProcessBuilder crmpb = new ProcessBuilder(crmCommands);
            Process crmp = crmpb.start();
            crmp.waitFor();
            Thread.sleep(50);
        }
    }
