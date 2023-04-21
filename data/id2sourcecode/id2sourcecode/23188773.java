    private void updateMinecraft(File f, File mineCraftJarDir) throws IOException {
        File zipDir = new File(prefs.getTmpArea(), f.getName());
        Util.unzip(f, zipDir);
        File mockMineDir = new File(prefs.getTmpArea(), "mockminecraft");
        FileUtils.forceMkdir(mockMineDir);
        File mockRes = new File(mockMineDir, "resources");
        FileUtils.forceMkdir(mockRes);
        if (hasClassFiles(zipDir)) {
            FileUtils.copyDirectory(zipDir, mineCraftJarDir, new TrackingFileFilter(prefs.getTmpArea(), true), true);
            return;
        }
        File files[] = zipDir.listFiles();
        if (files == null || files.length == 0) {
            setMessage(f.getName() + " contained no files");
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                if (file.getName().startsWith("__")) continue;
                if (file.getName().toLowerCase().contains("resource")) {
                    FileUtils.copyDirectory(file, mockRes, new TrackingFileFilter(prefs.getTmpArea(), true), true);
                    continue;
                }
                File dir = findClassesDir(file);
                if (dir != null) {
                    FileUtils.copyDirectory(dir, mineCraftJarDir, new TrackingFileFilter(prefs.getTmpArea(), true), true);
                    continue;
                }
                setMessage("Unknown Directory " + file + " (ignored)");
            } else {
                if (!file.getName().toLowerCase().contains("readme")) {
                    setMessage("Copied: " + file.getName());
                    FileUtils.copyFileToDirectory(file, mockMineDir);
                    continue;
                }
            }
        }
    }
