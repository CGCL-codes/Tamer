    @Override
    protected void processFile(SourceFile src) throws Exception {
        if (getLog().isDebugEnabled()) {
            getLog().debug("compress file :" + src.toFile() + " to " + src.toDestFile(suffix));
        }
        File inFile = src.toFile();
        File outFile = src.toDestFile(suffix);
        File copyToFile = null;
        getLog().debug("only compress if input file is younger than existing output file");
        if (!force && outFile.exists() && (outFile.lastModified() > inFile.lastModified())) {
            if (getLog().isInfoEnabled()) {
                getLog().info("nothing to do, " + outFile + " is younger than original, use 'force' option or clean your target");
            }
            return;
        }
        if (!"".equals(sourcesuffix) && !nosource) {
            getLog().info("compress source :[" + sourcesuffix + "]");
            if (!(".css".equalsIgnoreCase(src.getExtension()) || src.toFile().getName().endsWith(".css.dsp"))) {
                copyToFile = src.toDestFile(sourcesuffix);
                if (copyToFile.exists() && copyToFile.lastModified() > inFile.lastModified()) {
                    if (getLog().isInfoEnabled()) {
                        getLog().info("nothing to do, " + copyToFile + " is younger than original, clean your target instead.");
                    }
                    return;
                }
                if (getLog().isDebugEnabled()) {
                    getLog().debug("copyFile inFile from: " + inFile.getAbsolutePath() + " to: " + copyToFile.getAbsolutePath());
                }
                if (removeSourceComment) {
                    getLog().info("remove js comment: " + copyToFile.getName());
                    String fileContent = FileUtils.fileRead(inFile, encoding);
                    try {
                        fileContent = Comments.removeComment(fileContent);
                    } catch (IllegalStateException ex) {
                        getLog().error("clear comment failed:" + copyToFile.getName() + ":" + ex.getMessage() + ":skip clear comment step");
                    }
                    FileUtils.fileWrite(copyToFile.getAbsolutePath(), encoding, fileContent);
                } else FileUtils.copyFile(inFile, copyToFile);
            }
        }
        InputStreamReader in = null;
        OutputStreamWriter out = null;
        File outFileTmp = new File(outFile.getAbsolutePath() + ".tmp");
        FileUtils.forceDelete(outFileTmp);
        try {
            in = new UnicodeReader(new FileInputStream(inFile), encoding);
            if (!outFile.getParentFile().exists() && !outFile.getParentFile().mkdirs()) {
                throw new MojoExecutionException("Cannot create resource output directory: " + outFile.getParentFile());
            }
            getLog().debug("use a temporary outputfile (in case in == out)");
            getLog().debug("start compression");
            out = new OutputStreamWriter(new FileOutputStream(outFileTmp), encoding);
            if (".js".equalsIgnoreCase(src.getExtension())) {
                JavaScriptCompressor compressor = new JavaScriptCompressor(in, jsErrorReporter_);
                compressor.compress(out, linebreakpos, !nomunge, jswarn, preserveAllSemiColons, disableOptimizations);
            } else if (".css".equalsIgnoreCase(src.getExtension()) || src.toFile().getName().endsWith(".css.dsp")) {
                CssCompressor compressor = new CssCompressor(in);
                compressor.compress(out, linebreakpos);
            }
            getLog().debug("end compression");
        } finally {
            IOUtil.close(in);
            IOUtil.close(out);
        }
        FileUtils.forceDelete(outFile);
        if (getLog().isDebugEnabled()) {
            getLog().debug("rename outFile from: " + outFileTmp.getAbsolutePath() + " to: " + outFile.getAbsolutePath());
        }
        FileUtils.rename(outFileTmp, outFile);
        if (copyToFile != null) copyToFile.setLastModified(outFile.lastModified() + 500);
        File gzipped = gzipIfRequested(outFile);
        if (statistics) {
            inSizeTotal_ += inFile.length();
            outSizeTotal_ += outFile.length();
            getLog().info(String.format("%s (%db) -> %s (%db)[%d%%]", inFile.getName(), inFile.length(), outFile.getName(), outFile.length(), ratioOfSize(inFile, outFile)));
            if (gzipped != null) {
                getLog().info(String.format("%s (%db) -> %s (%db)[%d%%]", inFile.getName(), inFile.length(), gzipped.getName(), gzipped.length(), ratioOfSize(inFile, gzipped)));
            }
        }
    }
