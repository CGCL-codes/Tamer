    public void startJade() {
        String os = System.getProperty("os.name");
        String java = "";
        String javaVMArgs = "";
        String classPath = "";
        String jade = "";
        String jadeArgs = "";
        java = "java";
        if (jvmMemAllocUseDefaults == false) {
            javaVMArgs = "-Xms" + jvmMemAllocInitial + " -Xmx" + jvmMemAllocMaximum;
        }
        classPath = getClassPath(jadeServices);
        if (os.toLowerCase().contains("windows") == true) {
        } else if (os.toLowerCase().contains("linux") == true) {
            classPath = classPath.replaceAll(";", ":");
        }
        jade += "agentgui.core.application.Application -jade" + " ";
        if (jadeServices != null) {
            jadeArgs += "-services  " + jadeServices + " ";
        }
        if (jadeIsRemoteContainer) {
            jadeArgs += "-container ";
            jadeArgs += "-container-name " + jadeContainerName + " ";
        }
        jadeArgs += "-host " + jadeHost + " ";
        jadeArgs += "-port " + jadePort + " ";
        if (jadeShowGUI) {
            jadeArgs += jadeShowGUIAgentName + jadeContainerName + ":jade.tools.rma.rma ";
        }
        jadeArgs = jadeArgs.trim();
        String execute = java + " " + javaVMArgs + " " + classPath + " " + jade + " " + jadeArgs;
        execute = execute.replace("  ", " ");
        System.out.println("Execute: " + execute);
        try {
            String[] arg = execute.split(" ");
            ProcessBuilder proBui = new ProcessBuilder(arg);
            proBui.redirectErrorStream(true);
            proBui.directory(new File(pathBaseDir));
            Process p = proBui.start();
            Scanner in = new Scanner(p.getInputStream()).useDelimiter("\\Z");
            Scanner err = new Scanner(p.getErrorStream()).useDelimiter("\\Z");
            while (in.hasNextLine() || err.hasNextLine()) {
                if (in.hasNextLine()) {
                    System.out.println("[" + jadeContainerName + "]: " + in.nextLine());
                }
                if (err.hasNextLine()) {
                    System.err.println("[" + jadeContainerName + "]: " + err.nextLine());
                }
            }
            System.out.println("Killed Container [" + jadeContainerName + "]");
            if (extJarFolder != null) {
                if (extJarFolder.exists() == true) {
                    deleteFolder(extJarFolder);
                    extJarFolder.delete();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
