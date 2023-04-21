    private void loadFiles(File root, int maxlevel) throws IOException {
        if ((root.isDirectory()) && (maxlevel != 0)) {
            File[] files = root.listFiles(ROMFile.getFileFilter());
            for (int x = 0; x < files.length; ++x) {
                loadFiles(files[x], maxlevel - 1);
            }
        } else {
            myPM.setNote(root.getName());
            myPM.setProgress(++currentProgress);
            ROMFile rf = new ROMFile(root.toString());
            MetaData md = myROMInfo.getEntry(rf.getProperty("rom-CRC").toUpperCase());
            if (md != null) {
                rf.addProperties(md);
                if ((!rf.getName().equals("rom-name")) && (Boolean.getBoolean("net.sourceforge.rombrowser.rename"))) {
                    ROMFile newrf = rf.renameAppropriately(Boolean.getBoolean("net.sourceforge.rombrowser.delete-while-renaming"));
                    if (newrf != rf) {
                        rf = newrf;
                        rf.addProperties(md);
                    }
                }
            }
            myKnownFiles.addEntry(rf);
        }
    }
