    public void deploy(List<Deployable> deployables, AbstractDeployMojo mojo) throws MojoExecutionException {
        try {
            if (mojo.useDFCApplicationRouter && (mojo.dfcApplicationRouterConfig != null) && (mojo.dfcApplicationRouterConfig.length() > 0)) {
                File approuter = new File(mojo.dfcApplicationRouterConfig);
                if ((mojo.wsadminApprouterConfigPath != null) && (mojo.wsadminApprouterConfigPath.length() > 0)) {
                    File dest = new File(mojo.wsadminApprouterConfigPath);
                    if (dest.isDirectory()) {
                        FileUtils.copyFileToDirectory(approuter, dest);
                    } else {
                        File parent = dest.getParentFile();
                        if (!parent.exists()) {
                            throw new MojoExecutionException("Directory for wsadminApprouterConfigPath (" + mojo.wsadminApprouterConfigPath + ") does not exist.");
                        }
                        FileUtils.copyFile(approuter, dest);
                    }
                }
            }
            for (Deployable d : mojo.deployables) {
                boolean ignoreFailureToStart = false;
                try {
                    executeWsadminCommand(mojo.getLog(), mojo.wsadminPath, WebSphereCommands.getInstallCommand(d.path, d.context, d.context));
                } catch (ExecException exe) {
                    String onAppExists = mojo.onApplicationExists;
                    if (onAppExists == null) {
                        onAppExists = "redeploy";
                    }
                    if (onAppExists.equalsIgnoreCase("warn")) {
                        ignoreFailureToStart = true;
                        mojo.getLog().warn("Attempt to deploy " + d + " failed because the app is already " + " installed. Ignoring (onApplicationExists==" + "warn)");
                    } else if (onAppExists.equalsIgnoreCase("fail")) {
                        throw exe;
                    } else {
                        mojo.getLog().warn("Attempt to deploy " + d + " failed with exit code " + exe.getExitCode() + ". Attempting to uninstall and redeploy. " + "Set attemptRedeploy=false to disable this.");
                        ArrayList<Deployable> l = new ArrayList<Deployable>();
                        l.add(d);
                        undeploy(l, mojo);
                        executeWsadminCommand(mojo.getLog(), mojo.wsadminPath, WebSphereCommands.getInstallCommand(d.path, d.context, d.context));
                    }
                }
                try {
                    executeWsadminCommand(mojo.getLog(), mojo.wsadminPath, WebSphereCommands.getStartCommand(d.context));
                } catch (ExecException exe) {
                    if (ignoreFailureToStart) {
                        mojo.getLog().warn("Ingoring failure to start " + d + " (app was already installed)");
                    } else {
                        throw exe;
                    }
                }
            }
        } catch (IOException jse) {
            throw new MojoExecutionException("Error deploying to local WebSphere instance", jse);
        }
    }
