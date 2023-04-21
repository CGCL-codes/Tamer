    protected boolean processFile(final File inFile, final File outFile) throws IOException, BuildException {
        final long pStart = System.currentTimeMillis();
        if (isVerboseMode()) log("Processing " + inFile.getAbsolutePath(), getVerbosity());
        final String inString = readFileData(inFile), outString = replaceProperties(inFile, inString);
        boolean createOutputFile = (!outFile.exists()) || isOverwrite();
        if (!createOutputFile) {
            final String curOut = readFileData(outFile);
            if (!outString.equals(curOut)) createOutputFile = true;
        }
        if (createOutputFile) {
            {
                final File outDir = outFile.getParentFile();
                if ((!outDir.exists()) && (!outDir.mkdirs())) throw new BuildException("Cannot create output folder for file=" + outFile.getAbsolutePath(), getLocation());
            }
            if (outFile.exists() && (!outFile.canWrite())) {
                if (!isOverwriteReadOnly()) throw new BuildException("Not allowed to overwrite read-only file=" + outFile.getAbsolutePath(), getLocation());
                final Attrib at = new Attrib();
                at.setFile(outFile);
                at.setReadonly(false);
                at.setProject(getProject());
                at.execute();
                if (isVerboseMode()) log("Enabled write to read-only file=" + outFile.getAbsolutePath(), getVerbosity());
            }
            FileOutputStream fout = null;
            try {
                fout = new FileOutputStream(outFile);
                fout.write(outString.getBytes(getEncoding()));
            } finally {
                if (fout != null) {
                    try {
                        fout.close();
                    } finally {
                        fout = null;
                    }
                }
            }
            if (isVerboseMode()) {
                if (inFile != outFile) log("Translated " + inFile.getAbsolutePath() + " => " + outFile.getAbsolutePath(), getVerbosity()); else log("Created/Generated " + outFile.getAbsolutePath(), getVerbosity());
            }
        }
        final long pEnd = System.currentTimeMillis(), pDiff = (pEnd - pStart);
        if (isVerboseMode()) log("Processed (in " + pDiff + " msec.) " + inFile.getAbsolutePath(), getVerbosity());
        return createOutputFile;
    }
