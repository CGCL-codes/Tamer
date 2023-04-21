    private void loadServiceNames() {
        try {
            java.io.InputStream inStr = null;
            ClassLoader cl = null;
            try {
                cl = ClassLoader.getSystemClassLoader();
            } catch (java.lang.NoClassDefFoundError e) {
            } catch (java.lang.NoSuchMethodError e) {
            }
            if (cl != null) {
                inStr = cl.getResourceAsStream("nmap-services");
            }
            if (inStr == null) {
                String classpath = System.getProperty("java.class.path");
                if (classpath == null) {
                    classpath = new String("");
                }
                java.util.StringTokenizer st = new java.util.StringTokenizer(classpath, ":");
                String path;
                java.io.File file;
                while (st.hasMoreTokens()) {
                    path = st.nextToken();
                    file = new java.io.File(path, "/nmap-services");
                    if (file.exists()) {
                        inStr = new java.io.FileInputStream(file);
                        break;
                    }
                }
                if (inStr == null) {
                    file = new java.io.File(".", "/nmap-services");
                    if (file.exists()) {
                        inStr = new java.io.FileInputStream(file);
                    }
                }
            }
            if (inStr != null) {
                java.io.InputStreamReader inStrReader = new java.io.InputStreamReader(inStr);
                java.io.BufferedReader bRead = new java.io.BufferedReader(inStrReader);
                String line;
                while ((line = bRead.readLine()) != null) {
                    int idx;
                    line = line.trim();
                    if ((line.length() == 0) || (line.charAt(0) == '#')) {
                        continue;
                    }
                    String[] toks = line.split("\\s+");
                    if (toks.length < 2) {
                        continue;
                    }
                    String serviceName = toks[0];
                    Integer port = new Integer(0);
                    String protocol = "";
                    String[] portAndProtocol = toks[1].split("/");
                    if (portAndProtocol.length != 2) {
                        continue;
                    }
                    try {
                        port = Integer.decode(portAndProtocol[0]);
                    } catch (NumberFormatException nfe) {
                        System.err.println("Unparsable port: " + portAndProtocol[0]);
                        continue;
                    }
                    protocol = portAndProtocol[1].trim();
                    if (protocol.equalsIgnoreCase("tcp")) {
                        tcpServices.put(port, serviceName);
                    } else if (protocol.equalsIgnoreCase("udp")) {
                        udpServices.put(port, serviceName);
                    } else {
                        System.err.println("Unrecognized protocol in line: \"" + line + "\"");
                    }
                }
                if (inStr instanceof java.io.FileInputStream) {
                    inStr.close();
                }
            }
        } catch (java.lang.Exception e) {
        } catch (java.lang.Error e) {
        }
    }
