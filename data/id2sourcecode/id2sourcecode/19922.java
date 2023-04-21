    public String createPatch() {
        DirectoryManager s = new DirectoryManager(dirSource);
        DirectoryManager m = new DirectoryManager(dirModif);
        DirectoryManager d = new DirectoryManager(dirDest);
        String res = "";
        try {
            if (myWindow != null) {
                myWindow.progress.setValue(0);
                myWindow.progress.setMaximum(s.calcNumFiles(true, false));
                myWindow.info.setText("Patch creation : processing source->modifications");
            }
            skimThroughSource(s, m, d);
            while (threadRunning > 0) {
                writeOnLog("Waiting for " + threadRunning + " threads to terminate...");
                Thread.sleep(500);
            }
            if (myWindow != null) {
                myWindow.progress.setValue(0);
                myWindow.progress.setMaximum(m.calcNumFiles(true, false));
                myWindow.info.setText("Patch creation : processing modifications->source");
            }
            skimThroughMod(s, m, d);
        } catch (Exception e) {
            res = "error creating patch : " + e;
            writeOnLog(res);
            return res;
        }
        try {
            while (threadRunning > 0) {
                writeOnLog("Waiting for " + threadRunning + " threads to terminate...");
                Thread.sleep(500);
            }
        } catch (Exception e) {
            writeOnLog("Was waiting for " + threadRunning + " threads to terminate... and error ! " + e);
        }
        log.close();
        filesToDelete.close();
        md5File.close();
        if (myWindow != null) {
            myWindow.progress.setValue(0);
            myWindow.progress.setMaximum(d.calcNumFiles(true, false));
            myWindow.info.setText("Patch creation : creating final zip file");
        }
        ZipCreator z = new ZipCreator(this, dirDest.replace(tempDir + File.separator, "") + zipName, dirDest);
        boolean zipCreated = z.create();
        if (myWindow != null) {
            myWindow.info.setText("Deleting temporary files");
            myWindow.repaint();
        }
        d.deleteMe();
        if (zipCreated) res = "<html>Patch successfully created!<br>" + dirDest.replace(tempDir + File.separator, "") + zipName + "</html>"; else res = "error while creating patch...";
        return res;
    }
