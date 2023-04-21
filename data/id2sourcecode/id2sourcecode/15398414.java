    public void execute() throws Exception {
        if (sourceOnHDFS) testFileExistOnHDFSOrDie(source, "File not found " + source + " on HDFS ", FileNotFoundException.class, LOG); else testFileExistOrDie(source, "File not found " + source + " on localFs ", FileNotFoundException.class, LOG);
        if (targetOnHDFS) testFileExistOnHDFSOrDie(target, "File not found " + target + " on HDFS ", FileNotFoundException.class, LOG); else testFileExistOrDie(target, "File not found " + target + " on localFs ", FileNotFoundException.class, LOG);
        if (alignOnHDFS) testFileExistOnHDFSOrDie(align, "File not found " + align + " on HDFS ", FileNotFoundException.class, LOG); else testFileExistOrDie(align, "File not found " + align + " on localFs ", FileNotFoundException.class, LOG);
        if (outputOnHDFS) {
            if (!overwrite) testFileNotExistOnHDFSOrDie(output, "File already exists " + output + " on HDFS ", IOException.class, LOG); else if (!testFileNotExistOnHDFS(output, "File already exists " + output + " on HDFS, will be overwritten ", LOG)) {
                deleteIfExists(output);
            }
        } else {
            if (!overwrite) testFileNotExistOrDie(output, "File already exists " + output + " on local FS ", IOException.class, LOG);
        }
        InputStream src = openFileForRead(source, sourceOnHDFS);
        InputStream tgt = openFileForRead(target, targetOnHDFS);
        InputStream aln = openFileForRead(align, alignOnHDFS);
        OutputStream oup = CommonFileOperations.openFileForWrite(output);
        mergeAlignment(src, tgt, aln, oup);
    }
