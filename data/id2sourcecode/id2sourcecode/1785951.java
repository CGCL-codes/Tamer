        public void setUp() throws Exception {
            super.setUp();
            String sn = System.getProperty("persistence.test.nodes");
            if (sn == null || "".equals(sn)) num_nodes = 0; else num_nodes = (new Integer(sn)).intValue();
            String sw = System.getProperty("persistence.test.wait");
            if (sw == null || "".equals(sw)) wait = 500; else wait = (new Integer(sw)).intValue();
            controllables = new HashMap<ControllableAddress, Set<ControllableAddress>>();
            hazelcast2controllable = new HashMap<ControllableAddress, ControllableAddress>();
            controllable2hazelcast = new HashMap<ControllableAddress, ControllableAddress>();
            controllable2soap = new HashMap<ControllableAddress, ControllableAddress>();
            soap2controllable = new HashMap<ControllableAddress, ControllableAddress>();
            searcher = new SearcherForControllables(controllables, hazelcast2controllable, controllable2hazelcast, controllable2soap, soap2controllable, new IControllablesDisplay() {

                public void addNode(ControllableAddress ca) {
                }

                ;

                public void removeNode(ControllableAddress ca) {
                }

                ;

                public void setMaster(ControllableAddress ca) {
                }

                ;

                public void setExMaster(ControllableAddress ca) {
                }

                ;

                public void setOrdinary(ControllableAddress ca) {
                }

                ;

                public void drawConnection(ControllableAddress ca, ControllableAddress cb) {
                }

                ;
            });
            String currentDir = System.getProperty("user.dir");
            if (currentDir.endsWith("/")) currentDir = currentDir.substring(0, currentDir.length());
            String mode = "";
            if (!(new File(currentDir + "/controllable-init.xargs")).exists()) {
                byte[] buf = new byte[read_chunk];
                int numread = 0;
                InputStream is = getClass().getResourceAsStream("/controllable-init.xargs");
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
                processes[i] = Runtime.getRuntime().exec("java -XX:+UseParallelGC -Xdebug -Xnoagent" + " -Xrunjdwp:transport=dt_socket,address=" + (8001 + i) + ",server=y,suspend=n -Djava.compiler=NONE -Dpersistence.control.port=" + (19001 + i) + " -Dorg.osgi.service.http.port=" + (8081 + num_nodes + i) + " -Dorg.osgi.framework.dir=fw_persistence_node" + i + " -jar framework.jar" + mode + " >> /tmp/persistence.demo.log", null, null);
            }
            Thread.sleep(num_nodes * wait);
        }
