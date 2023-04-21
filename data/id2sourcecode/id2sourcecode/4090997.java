    public synchronized boolean restore(String project, String snapshotDirName, String snapshotId) {
        String errorMsg = "Problems encountered while trying to restore local snapshot.";
        boolean rc = false;
        if ((project != null) && (snapshotDirName != null) && (snapshotId != null)) {
            try {
                boolean projectWasClosed = false;
                IProject proj = null;
                ArrayList<String> projectResources = null;
                boolean autoBuildChanged = spy.setAutoBuild(false);
                if (spy.exists(project)) {
                    proj = root.getProject(project);
                    if (!proj.isOpen()) {
                        proj.open(null);
                        projectWasClosed = true;
                    }
                    if (!(proj.isSynchronized(IResource.DEPTH_INFINITE))) {
                        proj.refreshLocal(IResource.DEPTH_INFINITE, null);
                    }
                    projectResources = spy.listProjectMemberFiles(proj);
                } else {
                    if (XPLog.isDebugEnabled()) {
                        XPLog.printDebug(LogConstants.LOG_PREFIX_LOCALSNAPSHOT + "project \"" + project + "\" does not exist in the workspace \"" + root.getLocation() + "\"! Creating it.");
                    }
                    proj = root.getProject(project);
                    proj.create(null);
                    proj.open(null);
                }
                FileInputStream fis = new FileInputStream(getSnapshotPath(project, snapshotDirName, snapshotId).toOSString());
                CheckedInputStream checksum = new CheckedInputStream(fis, new Adler32());
                ZipInputStream zis = new ZipInputStream(new BufferedInputStream(checksum));
                ZipEntry entry;
                try {
                    while ((entry = zis.getNextEntry()) != null) {
                        if (XPLog.isDebugEnabled()) {
                            XPLog.printDebug(LogConstants.LOG_PREFIX_LOCALSNAPSHOT + "extracting: " + entry);
                        }
                        if (entry.isDirectory()) {
                            writeFolder(proj, entry.getName());
                        } else {
                            int count;
                            byte data[] = new byte[ILocalSnapshotConstants.BUFFER];
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            BufferedOutputStream dest = new BufferedOutputStream(bos, ILocalSnapshotConstants.BUFFER);
                            try {
                                while ((count = zis.read(data, 0, ILocalSnapshotConstants.BUFFER)) != -1) {
                                    dest.write(data, 0, count);
                                }
                                dest.flush();
                                bos.flush();
                            } finally {
                                dest.close();
                                bos.close();
                            }
                            writeFile(proj, entry.getName(), bos.toByteArray());
                        }
                        if (projectResources != null) {
                            IResource r = proj.findMember(entry.getName());
                            if (r != null) {
                                if (XPLog.isDebugEnabled()) {
                                    XPLog.printDebug(LogConstants.LOG_PREFIX_LOCALSNAPSHOT + "keeping: " + r.getProjectRelativePath().toOSString());
                                }
                                projectResources.remove(r.getProjectRelativePath().toOSString());
                            }
                        }
                    }
                } finally {
                    zis.close();
                }
                rc = true;
                if (XPLog.isDebugEnabled()) {
                    XPLog.printDebug(LogConstants.LOG_PREFIX_LOCALSNAPSHOT + "checksum: " + checksum.getChecksum().getValue());
                }
                if (projectResources != null) {
                    for (String s : projectResources) {
                        if (XPLog.isDebugEnabled()) {
                            XPLog.printDebug(LogConstants.LOG_PREFIX_LOCALSNAPSHOT + "deleting: " + s);
                        }
                        IResource r = proj.findMember(s);
                        if (r != null) {
                            r.delete(true, null);
                        }
                    }
                }
                if (!(root.isSynchronized(IResource.DEPTH_INFINITE))) {
                    root.refreshLocal(IResource.DEPTH_INFINITE, null);
                }
                proj.build(IncrementalProjectBuilder.CLEAN_BUILD, null);
                if (projectWasClosed) {
                    proj.close(null);
                }
                if (autoBuildChanged) {
                    spy.setAutoBuild(true);
                }
            } catch (ZipException e) {
                logException(0, errorMsg, e);
            } catch (FileNotFoundException e) {
                logException(0, errorMsg, e);
            } catch (IOException e) {
                logException(0, errorMsg, e);
            } catch (CoreException e) {
                logException(0, errorMsg, e);
            } catch (Exception e) {
                logException(0, errorMsg, e);
            }
        }
        return rc;
    }
