    public String decodePatch() {
        File fpatch = new File(patch);
        tempDir = PatchManager.getFreeFolderName(fpatch.getParent(), "patch");
        String res = "<html>";
        writeOnLog("Num files to process : " + numFilesToProcess);
        boolean keepGoing = true;
        File newDir = new File(fpatch.getParent() + File.separator + tempDir);
        newDir.mkdir();
        if (myWindow != null) {
            myWindow.progress.setValue(0);
            myWindow.progress.setMaximum(numFilesToProcess);
            myWindow.info.setText("Unzipping patch");
        }
        ZipExtractor z = new ZipExtractor(this, patch, newDir.getPath());
        boolean extractOk = z.extract();
        DirectoryManager p = new DirectoryManager(newDir.getPath());
        if (!extractOk) {
            res += "Error while unzipping</html>";
        } else {
            DirectoryManager d = new DirectoryManager(dirDest);
            d.addMeFile(new File(p.getDirectory() + "md5.check"));
            if (myWindow != null) {
                myWindow.progress.setValue(0);
                myWindow.info.setText("Checking version compatibility");
            }
            if (!canIApplyPatch()) {
                res += "Error while applying patch : incompatible versions</html>";
            } else {
                if (myWindow != null) {
                    myWindow.progress.setValue(0);
                    myWindow.progress.setMaximum(numFilesToProcess);
                    myWindow.info.setText("Applying patch");
                }
                try {
                    skimThroughPatch(p, d);
                    while (threadRunning > 0) {
                        writeOnLog("Waiting for " + threadRunning + " threads to terminate...");
                        Thread.sleep(500);
                    }
                } catch (Exception e) {
                    writeOnLog("error applying patch : " + e);
                    res += "Error while applying patch : " + e + "</html>";
                    keepGoing = false;
                }
                if (keepGoing) {
                    if (myWindow != null) {
                        myWindow.info.setText("Deleting temp patch folder");
                        myWindow.repaint();
                    }
                    String lig = "";
                    try {
                        File todel = new File(d.getDirectory() + "todelete.patch");
                        if (myWindow != null) {
                            myWindow.repaint();
                            myWindow.info.setText("Deleting obsolete files");
                        }
                        FileReader fc = new FileReader(todel);
                        BufferedReader fct = new BufferedReader(fc);
                        do {
                            lig = fct.readLine();
                            File fToDelete = new File(d.getDirectory() + lig);
                            if (fToDelete.exists()) {
                                if (fToDelete.isDirectory()) {
                                    DirectoryManager dirToDelete = new DirectoryManager(fToDelete.getPath());
                                    dirToDelete.deleteMe();
                                } else if (fToDelete.isFile()) {
                                    fToDelete.delete();
                                    writeOnLog("deleted : " + d.getDirectory() + lig);
                                }
                            }
                        } while (lig != null);
                        fc.close();
                        todel.delete();
                    } catch (Exception e) {
                        writeOnLog("error while deleting obsolete file " + d.getDirectory() + lig);
                        res += "Error while deleting obsolete file : " + d.getDirectory() + lig + "<br>";
                        keepGoing = false;
                    }
                    if (keepGoing) res += "Patch successfully applied!</html>";
                }
            }
            new File(d.getDirectory() + "md5.check").delete();
            writeOnLog("deleted md5 file " + d.getDirectory() + "md5.check");
        }
        log.close();
        p.deleteMe();
        return res;
    }
