    public Installation getInstallation(String name, String version) throws InstallerException {
        File inst = getInstDir(name, version);
        if (inst == null) {
            throw new InstallerNotFoundException(name + " " + version + " not found");
        }
        FileInputStream fis;
        try {
            fis = new FileInputStream(inst + File.separator + DATA_FILE);
        } catch (FileNotFoundException fnfe) {
            throw new InstallerException(name + " " + version + " not found");
        }
        FileChannel channel = fis.getChannel();
        FileLock lock;
        if ((name == null) || (version == null)) {
            throw new NullPointerException("name or version is null");
        }
        try {
            lock = channel.lock(0, inst.length(), true);
        } catch (Exception e) {
            try {
                fis.close();
            } catch (Exception e2) {
            }
            throw new InstallerException(inst.getPath() + " is locked");
        }
        Properties p = new Properties();
        File propFile = new File(inst, DATA_FILE);
        if (propFile.exists() == false) {
            try {
                lock.release();
                fis.close();
            } catch (Exception e) {
            }
            throw new InstallerException(DATA_FILE + "does not exist in " + inst.getPath() + " - try re-installing");
        }
        try {
            p.load(new FileInputStream(propFile));
        } catch (Exception e) {
            try {
                lock.release();
                fis.close();
            } catch (Exception e2) {
            }
            throw new InstallerException("unable to load " + propFile.getPath(), e);
        }
        String id = p.getProperty("providerId");
        if (id == null) {
            throw new InstallerException("no providerId found");
        }
        try {
            lock.release();
            fis.close();
        } catch (Exception e) {
        }
        return (new Installation(name, version, id, inst.getPath(), p));
    }
