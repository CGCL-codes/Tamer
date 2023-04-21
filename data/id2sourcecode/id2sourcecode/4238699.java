    private void copyFlashFiles(String rootDir, boolean publish, boolean isPlanet) throws IOException {
        File destDir = new File(rootDir);
        FileUtils.copyFileToDirectory(new File(flashDir, "expressInstall.swf"), destDir);
        FileUtils.copyFileToDirectory(new File(flashDir, "infact.swf"), destDir);
        FileUtils.copyFileToDirectory(new File(flashDir, "nodes.swf"), destDir);
        FileUtils.copyFileToDirectory(new File(flashDir, "as2bridge.swf"), destDir);
        FileUtils.copyFileToDirectory(new File(flashDir, "as2_tubeloc.swf"), destDir);
        FileUtils.copyFileToDirectory(new File(flashDir, "index.html"), destDir);
        if (!publish) {
            regexReplaceAllInFile(destDir + File.separator + "index.html", "flashvars\\.baseurl\\s*=\\s*\".*?\"", "flashvars.baseurl = \"" + getInfactUrl() + "\"");
        }
        FileUtils.copyFileToDirectory(new File(flashDir, "MinimaSilverPlayBackSeekMute.swf"), destDir);
        FileUtils.copyFileToDirectory(new File(flashDir, "MinimaUnderPlayBackSeekCounterVolMuteFull.swf"), destDir);
        FileUtils.copyDirectoryToDirectory(new File(flashDir, "js"), destDir);
        if (isPlanet) {
            FileUtils.copyFileToDirectory(new File(flashDir, "planeten.swf"), destDir);
            FileUtils.copyDirectoryToDirectory(new File(flashDir, "leaves"), destDir);
            FileUtils.copyDirectory(new File(flashDir, "templates" + File.separator + "KS_video"), new File(destDir, "templates" + File.separator + "KS_video"));
            File iVideoDir = new File(rootDir, "media" + File.separator + "ivideo" + File.separator + "1337");
            FileUtils.forceMkdir(iVideoDir);
            File pFlashDir = new File(rootDir, "media" + File.separator + "flash" + File.separator + "1337");
            FileUtils.forceMkdir(pFlashDir);
            FileUtils.copyFileToDirectory(new File(flashDir, "scrubduck_vr.swf"), pFlashDir);
            File ksDir = new File(rootDir, "ks" + File.separator + "1337");
            FileUtils.forceMkdir(ksDir);
            FileUtils.copyFileToDirectory(new File(flashDir, "signkyoto_vr.swf"), ksDir);
            File movieDir = new File(rootDir, "media" + File.separator + "movie" + File.separator + "1337");
            FileUtils.forceMkdir(movieDir);
        } else {
            FileFilter filter = FileFilterUtils.notFileFilter(FileFilterUtils.orFileFilter(FileFilterUtils.nameFileFilter("planet"), FileFilterUtils.nameFileFilter("planetvideo")));
            FileUtils.copyDirectory(new File(flashDir, "leaves"), new File(rootDir, "leaves"), filter);
        }
    }
