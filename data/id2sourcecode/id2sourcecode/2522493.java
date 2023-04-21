     */
    public static void repackStream(String prefix, ZipInputStream in, ZipOutputStream out) throws IOException {
        ZipEntry entry = in.getNextEntry();
        while (entry != null) {
            ZipEntry outEntry = new ZipEntry(entry.toString());
            outEntry.setTime(entry.getTime());
            ZipEntryAccessor.setMethod(outEntry, -1);
            ZipEntryAccessor.setName(outEntry, prefix + outEntry.toString());
            out.putNextEntry(outEntry);
            copyStream(in, out);
            out.closeEntry();
            dumpEntry(outEntry);
            entry = in.getNextEntry();
        }
    }

    /**

     * Copies a content from source file(s) to archived destination stream. 

     * @param inf the source file to be compressed

     * @param out the archived destination stream 

     * @param bRecursive the recursive execution property for folder operation

     * @throws java.io.IOException

     */
    public static void repackFile(File inf, ZipOutputStream out, boolean bRecursive) throws IOException {
        String stEN = null;
        if (inf.isAbsolute()) {
            stEN = inf.getName();
        } else {
            stEN = inf.getPath();
        }
        if (inf.isDirectory() && !stEN.endsWith(File.separator)) {
