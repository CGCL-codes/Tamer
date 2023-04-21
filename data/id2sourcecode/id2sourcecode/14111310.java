    protected boolean writeInstallFile(final File f) {
        boolean b = false;
        if (f.canRead()) {
            byte[] ba = new byte[(int) f.length()];
            if (ba != null) {
                try {
                    FileInputStream fisJar = new FileInputStream(f);
                    fisJar.read(ba);
                    fisJar.close();
                    ByteArrayEntry bae = new ByteArrayEntry(FileType.JAR_TYPE, GroupManager.DEFAULT, App.ADMIN_NAME, Integer.valueOf(0), App.WORKER_JAR_FILE, ba);
                    CommManager.getInstance().writeEntry(bae, Lease.FOREVER);
                    b = true;
                } catch (Exception exc) {
                    App.getInstance().showMessageDialog("InstallManager::writeInstallation(): f=" + f.getAbsolutePath() + ": write entry failed.");
                }
            }
        } else {
            App.getInstance().showMessageDialog("InstallManager::writeInstallation(): f=" + f.getAbsolutePath() + ": can't read.");
        }
        return b;
    }
