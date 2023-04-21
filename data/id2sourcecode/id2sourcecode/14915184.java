    protected synchronized void add(PrintWriter writer, String name, String aliases, String appBase, boolean manager, boolean autoDeploy, boolean deployOnStartup, boolean deployXML, boolean unpackWARs, boolean xmlNamespaceAware, boolean xmlValidation) {
        if (debug >= 1) {
            log("add: Adding host '" + name + "'");
        }
        if ((name == null) || name.length() == 0) {
            writer.println(sm.getString("hostManagerServlet.invalidHostName", name));
            return;
        }
        if (engine.findChild(name) != null) {
            writer.println(sm.getString("hostManagerServlet.alreadyHost", name));
            return;
        }
        File appBaseFile = null;
        if (appBase == null || appBase.length() == 0) {
            appBase = name;
        }
        File file = new File(appBase);
        if (!file.isAbsolute()) file = new File(System.getProperty("catalina.base"), appBase);
        try {
            appBaseFile = file.getCanonicalFile();
        } catch (IOException e) {
            appBaseFile = file;
        }
        if (!appBaseFile.exists()) {
            appBaseFile.mkdirs();
        }
        File configBaseFile = getConfigBase(name);
        if (manager) {
            InputStream is = null;
            OutputStream os = null;
            try {
                is = getServletContext().getResourceAsStream("/manager.xml");
                os = new FileOutputStream(new File(configBaseFile, "manager.xml"));
                byte buffer[] = new byte[512];
                int len = buffer.length;
                while (true) {
                    len = is.read(buffer);
                    if (len == -1) break;
                    os.write(buffer, 0, len);
                }
            } catch (IOException e) {
                writer.println(sm.getString("hostManagerServlet.managerXml"));
                return;
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                    }
                }
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
        StandardHost host = new StandardHost();
        host.setAppBase(appBase);
        host.setName(name);
        host.addLifecycleListener(new HostConfig());
        if ((aliases != null) && !("".equals(aliases))) {
            StringTokenizer tok = new StringTokenizer(aliases, ", ");
            while (tok.hasMoreTokens()) {
                host.addAlias(tok.nextToken());
            }
        }
        host.setAutoDeploy(autoDeploy);
        host.setDeployOnStartup(deployOnStartup);
        host.setDeployXML(deployXML);
        host.setUnpackWARs(unpackWARs);
        host.setXmlNamespaceAware(xmlNamespaceAware);
        host.setXmlValidation(xmlValidation);
        try {
            engine.addChild(host);
        } catch (Exception e) {
            writer.println(sm.getString("hostManagerServlet.exception", e.toString()));
            return;
        }
        host = (StandardHost) engine.findChild(name);
        if (host != null) {
            writer.println(sm.getString("hostManagerServlet.add", name));
        } else {
            writer.println(sm.getString("hostManagerServlet.addFailed", name));
        }
    }
