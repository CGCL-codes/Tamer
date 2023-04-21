    public static void setUp() throws Exception {
        String sn = System.getProperty("persistence.test.nodes");
        if (sn == null || "".equals(sn)) num_nodes = 0; else num_nodes = (new Integer(sn)).intValue();
        String sw = System.getProperty("persistence.test.wait");
        if (sw == null || "".equals(sw)) wait = 500; else wait = (new Integer(sw)).intValue();
        String currentDir = System.getProperty("user.dir");
        if (currentDir.endsWith("/")) currentDir = currentDir.substring(0, currentDir.length());
        String mode = "";
        if (!(new File(currentDir + "/controllable-init.xargs")).exists()) {
            byte[] buf = new byte[read_chunk];
            int numread = 0;
            InputStream is = me.getClass().getResourceAsStream("/controllable-init.xargs");
            FileOutputStream os = new FileOutputStream(currentDir + "/controllable-init.xargs");
            while (true) {
                numread = is.read(buf, 0, buf.length);
                if (numread < 0) break;
                os.write(buf, 0, numread);
            }
            mode = " -init -xargs controllable-init.xargs";
        }
        processes = new Process[num_nodes];
        for (int i = 0; i < num_nodes; i++) {
            String[] cmd = { "sh", "-c", "java -XX:+UseParallelGC -Xdebug -Xnoagent " + "-Xrunjdwp:transport=dt_socket,address=" + (8001 + i) + ",server=y,suspend=n -Djava.compiler=NONE -Dpersistence.control.port=" + (19001 + i) + " -Dorg.osgi.service.http.port=" + (8081 + num_nodes + i) + " -Dorg.osgi.framework.dir=fw_persistence_node" + i + " -jar framework.jar" + mode + " >> /tmp/persistence.demo.log" };
            processes[i] = Runtime.getRuntime().exec(cmd);
        }
        Thread.sleep(num_nodes * wait);
    }
