    public void deployApplication(WorkflowApplication inApplication, String inContextPath) {
        ConfigManager cm = ConfigManagerFactory.getConfigManager();
        String deployLocation = cm.getMandatoryPropertyAttribute(LOCATIONS_PATH, "deploy");
        File finalDeployDir = new File(deployLocation);
        File pfDir = getPowerFolderDirectory(cm);
        File assembleDir = new File(pfDir, "assemble");
        if (!assembleDir.exists()) {
            assembleDir.mkdirs();
        }
        MiscHelper.deleteFileOrDirectory(assembleDir);
        if (!assembleDir.exists()) {
            assembleDir.mkdirs();
        }
        File webInfDir = new File(assembleDir, "WEB-INF");
        if (!webInfDir.exists()) {
            webInfDir.mkdirs();
        }
        File jspsDir = new File(webInfDir, "jsps");
        if (!jspsDir.exists()) {
            jspsDir.mkdirs();
        }
        File appsJspsDir = new File(jspsDir, "apps");
        if (!appsJspsDir.exists()) {
            appsJspsDir.mkdirs();
        }
        File coreJspsDir = new File(jspsDir, "core");
        if (!coreJspsDir.exists()) {
            coreJspsDir.mkdirs();
        }
        File parentJspDir = new File(assembleDir, "parent.jsp");
        MiscHelper.writeTextFile(parentJspDir, INIT_JSP);
        for (int i = 0; i < inApplication.getWebPageCount(); i++) {
            WorkflowWebPage wwp = inApplication.getWebPage(i);
            String content = wwp.getContent();
            File wwpFile = new File(assembleDir, wwp.getName() + ".jsp");
            MiscHelper.writeTextFile(wwpFile, content);
        }
        for (int i = 0; i < inApplication.getWebFileCount(); i++) {
            WorkflowWebFile wwf = inApplication.getWebFile(i);
            byte content[] = wwf.getContent();
            File wwfFile = new File(assembleDir, wwf.getName());
            MiscHelper.writeBinaryFile(wwfFile, content);
        }
        String descriptor = getDeploymentDescriptor(inApplication);
        File webFile = new File(webInfDir, "web.xml");
        MiscHelper.writeTextFile(webFile, descriptor);
        String weblogicWebDescriptor = getWeblogicDeploymentDescriptor();
        File weblogicWebFile = new File(webInfDir, "weblogic.xml");
        MiscHelper.writeTextFile(weblogicWebFile, weblogicWebDescriptor);
        File libDir = new File(webInfDir, "lib");
        if (!libDir.exists()) {
            libDir.mkdirs();
        }
        File destPowerfolderJarFile = new File(libDir, "powerfolder.jar");
        File sourceLibDir = new File(pfDir, "lib");
        if (!sourceLibDir.exists()) {
            sourceLibDir.mkdirs();
        }
        File sourcePowerfolderJarFile = new File(sourceLibDir, "powerfolder.jar");
        MiscHelper.writeBinaryFile(destPowerfolderJarFile, MiscHelper.readBinaryFile(sourcePowerfolderJarFile));
        File deployDir = new File(pfDir, "deploy");
        if (!deployDir.exists()) {
            deployDir.mkdirs();
        }
        MiscHelper.deleteFileOrDirectory(deployDir);
        if (!deployDir.exists()) {
            deployDir.mkdirs();
        }
        File warFile = new File(deployDir, "deploy.war");
        createWarFile(assembleDir, warFile);
        File metaInfDir = new File(deployDir, "META-INF");
        if (!metaInfDir.exists()) {
            metaInfDir.mkdirs();
        }
        String earDescriptor = getAppDeploymentDescriptor(inContextPath);
        File earFile = new File(metaInfDir, "application.xml");
        MiscHelper.writeTextFile(earFile, earDescriptor);
        File finalEarFile = new File(finalDeployDir, inContextPath + ".ear");
        createWarFile(deployDir, finalEarFile);
        cm.close();
    }
