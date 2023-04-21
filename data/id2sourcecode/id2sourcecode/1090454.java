    public void StopExecution() throws Exception {
        stopped = true;
        HashMap<String, LinkedList<String>> vect = currentScenario.getPcList();
        IOFileManager.writePcListFile(vect, Config.getString("pclistalllinux"));
        Process process;
        String cmd = "sh " + Config.getString("path.scripts") + "clean_all.sh " + Config.getString("pclistalllinux") + " " + Config.getString("pathResRemoteLinux");
        System.out.println(cmd);
        process = Runtime.getRuntime().exec(cmd);
        process.waitFor();
        detectEnd.interrupt();
        Socket soc = new Socket(Config.getString("nameManager"), Config.getInt("portManager"));
        OutputStream os = soc.getOutputStream();
        ObjectOutputStream objs = new ObjectOutputStream(os);
        try {
            objs.writeObject("FINISHED");
            objs.flush();
            objs.close();
        } catch (SocketException e) {
        }
        os.close();
        soc.close();
    }
