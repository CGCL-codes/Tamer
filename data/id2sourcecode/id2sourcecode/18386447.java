    public void execute() throws MojoExecutionException {
        if (getLog().isDebugEnabled()) {
            getLog().debug("fork: " + fork);
            getLog().debug("failOnError: " + failOnError);
            getLog().debug("debug: " + debug);
            getLog().debug("verbose: " + verbose);
            getLog().debug("showDep: " + showDeprecation);
            getLog().debug("opt: " + optimize);
            getLog().debug("showWarn: " + showWarnings);
            getLog().debug("out: " + outputDirectory);
            getLog().debug("build: " + buildDirectory);
            getLog().debug("web: " + webappDirectory);
            getLog().debug("cleanUp: " + cleanUp);
            getLog().debug("jvmArgs: " + jvmArgs);
            getLog().debug("gwtLog: " + gwtLog);
            getLog().debug("gwtGen: " + gwtGen);
            getLog().debug("gwtTreeLogger: " + gwtTreeLogger);
            getLog().debug("gwtOutputStyle: " + gwtOutputStyle);
            getLog().debug("gwtAssert: " + gwtAssert);
            getLog().debug("gwtValidateOnly: " + gwtValidateOnly);
            StringBuffer srcRoots = new StringBuffer("src: ");
            for (Object root : compileSourceRoots) {
                srcRoots.append(" ").append(root);
            }
            getLog().debug(srcRoots.toString());
            StringBuffer classpath = new StringBuffer("cp: ");
            for (Object path : classpathElements) {
                classpath.append(" ").append(path);
            }
            getLog().debug(classpath.toString());
            StringBuffer moduleInfo = new StringBuffer("mods: ");
            for (Object module : modules) {
                moduleInfo.append(" ").append(module);
            }
            getLog().debug(moduleInfo.toString());
        }
        File gwtcDir = new File(buildDirectory, "gwtc");
        if (!gwtcDir.exists()) {
            gwtcDir.mkdirs();
            getLog().info("Created directory: " + gwtcDir.getAbsolutePath());
        }
        File gwtc = new File(gwtcDir, JAR_NAME);
        Exception compilerException = null;
        byte buffer[] = new byte[1024];
        if (!gwtc.exists()) {
            FileOutputStream out = null;
            InputStream in = null;
            try {
                out = new FileOutputStream(gwtc);
                in = this.getClass().getClassLoader().getResourceAsStream(JAR_NAME);
                for (int read = in.read(buffer); read != -1; read = in.read(buffer)) {
                    out.write(buffer, 0, read);
                }
                getLog().info("Created working gwtc: " + gwtc.getAbsolutePath());
            } catch (Exception e) {
                compilerException = e;
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    compilerException = e;
                }
            }
        }
        if (compilerException != null) {
            throw new MojoExecutionException("Trouble creating working compiler", compilerException);
        }
        if (!webappDirectory.exists()) {
            webappDirectory.mkdirs();
            getLog().info("Created directory: " + webappDirectory.getAbsolutePath());
        }
        String activeModule = null;
        String classpath;
        StringBuffer cp = new StringBuffer();
        cp.append(gwtc.getAbsolutePath());
        for (Object path : compileSourceRoots) {
            cp.append(File.pathSeparator).append(path);
        }
        for (Object path : classpathElements) {
            cp.append(File.pathSeparator).append(path);
        }
        classpath = cp.toString();
        getLog().debug("CP: " + classpath);
        ArrayList<String> args = new ArrayList<String>();
        args.add("java");
        if (jvmArgs != null) {
            for (String arg : jvmArgs.split("\\s")) {
                arg = arg.trim();
                if (arg.length() > 0) {
                    args.add(arg);
                }
            }
        }
        args.add("-classpath");
        args.add(classpath);
        args.add("com.google.gwt.dev.Compiler");
        args.add("-war");
        args.add(webappDirectory.getAbsolutePath());
        if (gwtLog != null) {
            gwtLog = gwtLog.trim().toUpperCase();
            if ("ERROR".equals(gwtLog) || "WARN".equals(gwtLog) || "INFO".equals(gwtLog) || "TRACE".equals(gwtLog) || "DEBUG".equals(gwtLog) || "SPAM".equals(gwtLog) || "ALL".equals(gwtLog)) {
                getLog().debug("logging: " + gwtLog);
            } else {
                gwtLog = null;
            }
            if (gwtLog != null) {
                args.add("-logLevel");
                args.add(gwtLog);
            }
        }
        if (gwtGen != null) {
            args.add("-gen");
            args.add(gwtGen.getPath());
        }
        if (gwtTreeLogger) {
            args.add("-treeLogger");
        }
        if (gwtOutputStyle != null) {
            gwtOutputStyle = gwtOutputStyle.trim().toUpperCase();
            if ("OBF".equals(gwtOutputStyle) || "OBFUSCATED".equals(gwtOutputStyle) || "PRETTY".equals(gwtOutputStyle) || "DETAILED".equals(gwtOutputStyle)) {
                getLog().debug("style: " + gwtOutputStyle);
            } else {
                gwtOutputStyle = null;
            }
            if (gwtOutputStyle != null) {
                args.add("-style");
                args.add(gwtOutputStyle);
            }
        }
        if (gwtAssert) {
            args.add("-ea");
        }
        if (gwtValidateOnly) {
            args.add("-validateOnly");
        }
        if (gwtWorkDir != null) {
            args.add("-workDir");
            args.add(gwtWorkDir);
        }
        if (gwtLocalWorkers != null) {
            args.add("-localWorkers");
            args.add(gwtLocalWorkers);
        }
        if (gwtExtra != null) {
            args.add("-extra");
            args.add(gwtExtra);
        }
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(gwtcDir);
        try {
            for (Object module : modules) {
                activeModule = module.toString();
                List<String> commands = processBuilder.command();
                commands.clear();
                commands.addAll(args);
                commands.add(activeModule);
                Process process = processBuilder.start();
                InputStream std = process.getInputStream();
                for (int read = std.read(buffer); read != -1; read = std.read(buffer)) {
                    getLog().info(new String(buffer, 0, read).trim());
                }
                std = process.getErrorStream();
                for (int read = std.read(buffer); read != -1; read = std.read(buffer)) {
                    getLog().error(new String(buffer, 0, read).trim());
                }
                if (process.waitFor() != 0) {
                    throw new MojoExecutionException("Error compiling module " + activeModule);
                }
                attemptCleanUp(activeModule + "-aux");
            }
            attemptCleanUp(".gwt-tmp");
        } catch (Exception e) {
            throw new MojoExecutionException("Error compiling module " + activeModule, e);
        }
    }
