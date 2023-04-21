    public void makeVersion(String comment) {
        if (vDir != null) {
            Logger.logWarning("RHDatabase.makeVersion", "Cannot make a version of a version");
            return;
        }
        if (!versionDir.exists()) {
            versionDir.mkdir();
        }
        int max = 0;
        int min = Integer.MAX_VALUE;
        int count = 0;
        File s[] = versionDir.listFiles(new FileFilter() {

            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
        int j;
        for (int i = 0; i < s.length; i++) {
            try {
                j = Integer.parseInt(s[i].getName());
            } catch (Throwable t) {
                continue;
            }
            if (j > max) {
                max = j;
            }
            if (j < min) {
                min = j;
            }
            count++;
        }
        max++;
        File newVersionDir = new File(versionDir, "" + max);
        newVersionDir.mkdir();
        String origName = getDBName();
        props.setProperty("rhumba.versioncomment", comment);
        props.setProperty("rhumba.versioning", "0");
        props.setProperty("rhumba.name", "version " + max + " of " + origName);
        saveDefinition();
        try {
            copyDatabase(newVersionDir, false);
        } catch (IOException e) {
        } finally {
            props.setProperty("rhumba.name", origName);
            props.setProperty("rhumba.versioning", Integer.toString(versionMask));
            props.remove("rhumba.versioncomment");
            saveDefinition();
        }
        if (count >= maxVersions) {
            File killme = new File(versionDir, new Integer(min).toString());
            if (!FileUtil.deleteDir(killme)) {
                Logger.logWarning("RHDatabase.makeVersion", "unable to delete old version: " + killme.getAbsolutePath());
            }
        }
    }
