    private void spotCheck(String subfName, List<File> invalidThumbs, int numberToCheck) {
        setShortLogDesc("SpotCheck");
        if (!tools.valid(SPOT_CHECK_DIR)) {
            Config.log(INFO, "Skipping spot check because no directory was specified....");
            return;
        }
        File spotCheckDir = new File(SPOT_CHECK_DIR);
        if (!spotCheckDir.exists()) {
            log(INFO, "Creating directory at: " + spotCheckDir);
            boolean created = spotCheckDir.mkdir();
            if (!created) log(ERROR, "Failed to create spot check directory, skipping spot check.");
            return;
        }
        File subdir = new File(spotCheckDir.getPath() + SEP + subfName);
        if (!subdir.exists()) subdir.mkdir(); else {
            Config.log(INFO, "Deleting spot check directory contents in preperation for new images at: " + subdir);
            try {
                for (Iterator<File> i = FileUtils.iterateFiles(subdir, THUMB_EXTS, false); i.hasNext(); ) {
                    File f = i.next();
                    if (f.exists()) f.delete();
                }
            } catch (Exception x) {
                Config.log(WARNING, "Failed to delete spot check directory contents: " + x, x);
            }
        }
        Random r = new Random();
        int numberCopied = 0;
        int attempts = 0;
        int maxAttempts = Math.abs((int) Math.round(numberToCheck * 3.5));
        if (invalidThumbs.isEmpty()) {
            Config.log(INFO, "No thumbs/fanart found to copy to spot-check directory. Skipping.");
        } else while (true) {
            attempts++;
            if (attempts > maxAttempts) break;
            File f = invalidThumbs.get(r.nextInt(invalidThumbs.size()));
            if (!f.getName().endsWith(".dds")) {
                int dotIndx = f.getName().lastIndexOf(".");
                String newName = f.getName().substring(0, dotIndx == -1 ? f.getName().length() : dotIndx);
                if (f.getPath().toLowerCase().contains("fanart")) newName += "-fanart";
                String ext;
                if (dotIndx == -1) ext = ".jpg"; else ext = f.getName().substring(dotIndx, f.getName().length()).toLowerCase();
                if (ext.equals(".tbn")) ext = ".jpg";
                newName += ext;
                File dest = new File(subdir + "\\" + newName);
                if (!dest.exists()) {
                    try {
                        FileUtils.copyFile(f, dest);
                        numberCopied++;
                    } catch (Exception x) {
                        Config.log(WARNING, "Failed to copy: " + x);
                    }
                }
            }
            if (numberCopied >= numberToCheck) break;
        }
        Config.log(INFO, "Copied " + numberCopied + " images to spot check directory at: " + subdir);
    }
