    private BinaryMessage dispatch(BinaryMessage query, ComputingInterface server, GSIUtils proxy) throws RemoteException {
        log.debug("Dispathing new job");
        WorkDescription wds = new WorkDescription();
        wds.getInfo().setOwner(proxy.getUserDN());
        wds.getInfo().setPrivacy(JobInfo.OPEN_FOR_ALL);
        String[] ids = query.readString().split("/");
        Long[] jobID = new Long[ids.length];
        for (int i = 0; i < ids.length; i++) jobID[i] = new Long(ids[i]);
        wds.setJobID(jobID);
        wds.getInfo().setJobName(query.readString());
        String serviceURL = query.readString();
        if (serviceURL.length() > 0) wds.setServiceURL(serviceURL); else wds.setServiceURL(DiscoveryService.getInstance().getLocalService());
        FileUtils.createDir(Config.getWorkingDir(jobID[0]));
        try {
            String jarFile = query.readString();
            if (jarFile.length() > 0) {
                URL jarURL = new URL(jarFile);
                URL targetURL = FileUtils.copyFile(jarURL, Config.getWorkingDir(jobID[0]));
                wds.attachFile(new URL("jar:" + targetURL.toString() + "!/"));
            }
        } catch (IOException mue) {
            throw new RemoteException("Cannot load the agent JAR file: " + mue.getMessage());
        }
        wds.getExecutable().setClassName(query.readString());
        wds.getExecutable().setMethodName(query.readString());
        int subCount = Integer.parseInt(query.readString());
        int parameterCount = Integer.parseInt(query.readString());
        int flagPairCount = Integer.parseInt(query.readString());
        if (subCount > 0) {
            for (int i = 0; i < subCount; i++) {
                WorkDescription subWds = new WorkDescription();
                String methodName = query.readString();
                if (methodName.length() == 0) methodName = null;
                subWds.getExecutable().setMethodName(methodName);
                wds.addChildren(subWds);
            }
        }
        String[] jobParameters = new String[parameterCount];
        for (int i = 0; i < parameterCount; i++) {
            jobParameters[i] = query.readString();
        }
        wds.getExecutable().putParameters(jobParameters);
        Properties jobFlags = new Properties();
        for (int i = 0; i < flagPairCount; i++) {
            String key = query.readString();
            String value = query.readString();
            jobFlags.setProperty(key, value);
        }
        wds.flags(jobFlags);
        while (query.hasNext()) {
            String name = Config.getWorkingDir(jobID[0]) + "/" + query.readString();
            byte[] data = query.readBinary();
            FileUtils.writeFile(name, data);
            try {
                wds.attachFile(new URL("file:" + name));
            } catch (IOException ioe) {
                throw new RemoteException(ioe.getMessage());
            }
        }
        log.debug("WDS " + wds.toString());
        server.dispatch(wds);
        log.debug("job dispatched " + wds.getJobID().toString());
        return new BinaryMessage(0);
    }
