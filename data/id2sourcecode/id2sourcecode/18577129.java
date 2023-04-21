    public void writeArchiveFromTo(File inputDir, File outputFile) throws LTSException, IOException {
        DirectoryScanner scan = new DirectoryScanner();
        scan.setBasedir(inputDir);
        String[] includes = { "**" };
        scan.setIncludes(includes);
        scan.scan();
        String[] files = scan.getIncludedFiles();
        FileOutputStream fos = new FileOutputStream(outputFile);
        ZipOutputStream zout = new ZipOutputStream(fos);
        for (int i = 0; i < files.length; i++) {
            File inputFile = new File(inputDir, files[i]);
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(inputFile);
            } catch (FileNotFoundException e) {
                throw new LTSException("Error trying to open file, " + inputFile + ", in preparation to write to archive, " + getArchiveFile(), e);
            }
            String s = convertOneString(files[i]);
            ZipEntry zentry = new ZipEntry(s);
            zout.putNextEntry(zentry);
            copyFromTo(fis, zout);
            fis.close();
        }
        zout.close();
    }
