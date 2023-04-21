    private void copy(File src, File dest) throws IOException {
        dest.mkdirs();
        File srcConfig = new File(src, "config.txt");
        if (srcConfig.exists()) {
            FileUtils.copyFile(srcConfig, new File(dest, "config.txt"));
        }
        File srcSeeds = new File(src, "seeds.txt");
        if (srcSeeds.exists()) {
            FileUtils.copyFile(srcSeeds, new File(dest, "seeds.txt"));
        }
        File srcSheets = new File(src, "sheets");
        if (srcSheets.isDirectory()) {
            FileUtils.copyFiles(srcSheets, new File(dest, "sheets"));
        }
        File srcState = new File(src, "state");
        if (srcState.isDirectory()) {
            FilenameFilter ff = new FilenameFilter() {

                public boolean accept(File parent, String name) {
                    return !name.equals("je.lck");
                }
            };
            FileUtils.copyFiles(srcState, ff, new File(dest, "state"), false, true);
        }
    }
