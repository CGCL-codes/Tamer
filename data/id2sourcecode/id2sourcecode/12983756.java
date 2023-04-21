    public synchronized void checkPermission(java.security.Permission perm) {
        if (!enabled) {
            return;
        }
        if (getSecurityContext().equals(safeSecurityContext)) {
            return;
        }
        Thread c = Thread.currentThread();
        try {
            super.checkPermission(perm);
            return;
        } catch (SecurityException e) {
        }
        if (perm instanceof FilePermission) {
            FilePermission fp = (FilePermission) perm;
            if (fp.getActions().equals("read")) {
                if (System.getProperty("OVERRIDEFILEREADSECURITY", "false").equals("true")) {
                    return;
                }
            }
        }
        if (perm instanceof PropertyPermission) {
            if (perm.getActions().equals("read")) {
                return;
            }
        }
        if (perm instanceof RuntimePermission) {
            if (perm.getName() != null && perm.getName().length() >= 24) {
                if (perm.getName().substring(0, 24).equals("accessClassInPackage.sun")) {
                    return;
                }
            }
        }
        RobotPeer r = threadManager.getRobotPeer(c);
        if (r == null) {
            r = threadManager.getLoadingRobotPeer(c);
            if (r == null) {
                if (perm instanceof RobocodePermission) {
                    if (perm.getName().equals("System.out") || perm.getName().equals("System.err") || perm.getName().equals("System.in")) {
                        return;
                    }
                }
                syserr.println("Preventing unknown thread " + Thread.currentThread().getName() + " from access: " + perm);
                syserr.flush();
                if (perm instanceof java.awt.AWTPermission) {
                    if (perm.getName().equals("showWindowWithoutWarningBanner")) {
                        throw new ThreadDeath();
                    }
                }
                throw new java.security.AccessControlException("Preventing unknown thread " + Thread.currentThread().getName() + " from access: " + perm);
            }
        }
        if (perm instanceof FilePermission) {
            FilePermission fp = (FilePermission) perm;
            if (fp.getActions().equals("read")) {
                RobotFileSystemManager fileSystemManager = r.getRobotFileSystemManager();
                if (fileSystemManager.getReadableDirectory() == null) {
                    r.setEnergy(0.0);
                    throw new java.security.AccessControlException("Preventing " + r.getName() + " from access: " + perm + ": Robots that are not in a package may not read any files.");
                }
                if (fileSystemManager.isReadable(fp.getName())) {
                    return;
                } else {
                    r.setEnergy(0.0);
                    throw new java.security.AccessControlException("Preventing " + r.getName() + " from access: " + perm + ": You may only read files in your own root package directory. ");
                }
            } else if (fp.getActions().equals("write")) {
                RobocodeFileOutputStream o = getRobocodeOutputStream();
                if (o == null) {
                    r.setEnergy(0.0);
                    throw new java.security.AccessControlException("Preventing " + r.getName() + " from access: " + perm + ": You must use a RobocodeOutputStream.");
                }
                removeRobocodeOutputStream();
                RobotFileSystemManager fileSystemManager = r.getRobotFileSystemManager();
                if (fileSystemManager.getWritableDirectory() == null) {
                    r.setEnergy(0.0);
                    throw new java.security.AccessControlException("Preventing " + r.getName() + " from access: " + perm + ": Robots that are not in a package may not write any files.");
                }
                if (fileSystemManager.isWritable(fp.getName())) {
                    return;
                } else {
                    if (fileSystemManager.getWritableDirectory().toString().equals(fp.getName())) {
                        return;
                    } else {
                        r.setEnergy(0.0);
                        threadOut("Preventing " + r.getName() + " from access: " + perm + ": You may only write files in your own data directory. ");
                        throw new java.security.AccessControlException("Preventing " + r.getName() + " from access: " + perm + ": You may only write files in your own data directory. ");
                    }
                }
            } else if (fp.getActions().equals("delete")) {
                RobotFileSystemManager fileSystemManager = r.getRobotFileSystemManager();
                if (fileSystemManager.getWritableDirectory() == null) {
                    r.setEnergy(0.0);
                    throw new java.security.AccessControlException("Preventing " + r.getName() + " from access: " + perm + ": Robots that are not in a package may not delete any files.");
                }
                if (fileSystemManager.isWritable(fp.getName())) {
                    return;
                } else {
                    if (fileSystemManager.getWritableDirectory().toString().equals(fp.getName())) {
                        return;
                    } else {
                        r.setEnergy(0.0);
                        throw new java.security.AccessControlException("Preventing " + r.getName() + " from access: " + perm + ": You may only delete files in your own data directory. ");
                    }
                }
            }
        }
        if (perm instanceof RobocodePermission) {
            if (perm.getName().equals("System.out") || perm.getName().equals("System.err")) {
                r.out.println("SYSTEM:  You cannot write to System.out or System.err.");
                r.out.println("SYSTEM:  Please use out.println instead of System.out.println");
                throw new java.security.AccessControlException("Preventing " + r.getName() + " from access: " + perm);
            } else if (perm.getName().equals("System.in")) {
                r.out.println("SYSTEM:  You cannot read from System.in.");
                throw new java.security.AccessControlException("Preventing " + r.getName() + " from access: " + perm);
            }
        }
        if (status == null || status.equals("")) {
            syserr.println("Preventing " + r.getName() + " from access: " + perm);
        } else {
            syserr.println("Preventing " + r.getName() + " from access: " + perm + " (" + status + ")");
        }
        r.setEnergy(0.0);
        if (perm instanceof java.awt.AWTPermission) {
            if (perm.getName().equals("showWindowWithoutWarningBanner")) {
                throw new ThreadDeath();
            }
        }
        throw new java.security.AccessControlException("Preventing " + Thread.currentThread().getName() + " from access: " + perm);
    }
