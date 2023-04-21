    protected synchronized void deploy(PrintWriter writer, String path, String tag, boolean update, HttpServletRequest request) {
        if (debug >= 1) {
            log("deploy: Deploying web application at '" + path + "'");
        }
        if ((path == null) || path.length() == 0 || !path.startsWith("/")) {
            writer.println(sm.getString("managerServlet.invalidPath", path));
            return;
        }
        String displayPath = path;
        if (path.equals("/")) path = "";
        String basename = getDocBase(path);
        Context context = (Context) host.findChild(path);
        if (update) {
            if (context != null) {
                undeploy(writer, displayPath);
            }
            context = (Context) host.findChild(path);
        }
        if (context != null) {
            writer.println(sm.getString("managerServlet.alreadyContext", displayPath));
            return;
        }
        File deployedPath = deployed;
        if (tag != null) {
            deployedPath = new File(versioned, tag);
            deployedPath.mkdirs();
        }
        File localWar = new File(deployedPath, basename + ".war");
        if (debug >= 2) {
            log("Uploading WAR file to " + localWar);
        }
        try {
            if (!isServiced(path)) {
                addServiced(path);
                try {
                    uploadWar(request, localWar);
                    if (tag != null) {
                        deployedPath = deployed;
                        File localWarCopy = new File(deployedPath, basename + ".war");
                        copy(localWar, localWarCopy);
                        localWar = localWarCopy;
                        copy(localWar, new File(getAppBase(), basename + ".war"));
                    }
                    check(path);
                } finally {
                    removeServiced(path);
                }
            }
        } catch (Exception e) {
            log("managerServlet.check[" + displayPath + "]", e);
            writer.println(sm.getString("managerServlet.exception", e.toString()));
            return;
        }
        context = (Context) host.findChild(path);
        if (context != null && context.getConfigured()) {
            writer.println(sm.getString("managerServlet.deployed", displayPath));
        } else {
            writer.println(sm.getString("managerServlet.deployFailed", displayPath));
        }
    }
