    public static final Collection<Map.Entry<File, File>> dirSync(final PrintStream out, final Collection<Map.Entry<File, File>> org, final File srcFolder, final File dstFolder) throws IOException {
        if ((null == srcFolder) || (null == dstFolder)) return org;
        if ((!srcFolder.exists()) || (!srcFolder.isDirectory())) return org;
        final String[] srcFiles = srcFolder.list();
        if ((null == srcFiles) || (srcFiles.length <= 0)) return org;
        if (!dstFolder.exists()) {
            if (!dstFolder.mkdirs()) throw new IOException("Cannot create destination folder=" + dstFolder);
            out.println("Created " + dstFolder);
        } else if (!dstFolder.isDirectory()) throw new IOException("Destination is not a folder: " + dstFolder);
        Collection<Map.Entry<File, File>> ret = org;
        for (final String sName : srcFiles) {
            final File sFile = new File(srcFolder, sName), dFile = new File(dstFolder, sName);
            if (sFile.isDirectory()) {
                ret = dirSync(out, ret, sFile, dFile);
                continue;
            }
            if (sFile.length() <= 0L) {
                out.println("Skip empty file " + sFile);
                if (dFile.exists()) {
                    if (dFile.delete()) out.println("\tDeleted " + dFile); else System.err.println("Failed to delete " + dFile);
                }
                continue;
            }
            if (dFile.exists()) {
                final long sMod = sFile.lastModified(), dMod = dFile.lastModified(), mDiff = sMod - dMod;
                if (mDiff <= TimeUnits.MINUTE.getMilisecondValue()) {
                    if (mDiff != 0L) {
                        final String sDate = DTF.format(new Date(sMod)), dDate = DTF.format(new Date(dMod));
                        out.println("\tSkip more recent file " + dFile + " (" + sDate + " vs. " + dDate + ")");
                    }
                    continue;
                }
                final long sLen = sFile.length(), dLen = dFile.length();
                if (sLen == dLen) {
                    out.println("\tSkip equal size file " + dFile);
                    continue;
                }
            } else out.println("\tCopy new file " + dFile);
            out.print("Copy " + sFile + " => " + dFile);
            final long cpyStart = System.currentTimeMillis(), cpyLen = IOCopier.copyFile(sFile, dFile), cpyEnd = System.currentTimeMillis(), cpyDuration = cpyEnd - cpyStart;
            if (cpyLen < 0L) throw new StreamCorruptedException("Error (" + cpyLen + ") after " + cpyDuration + " msrc. while copying " + sFile + " to " + dFile);
            out.println(" - " + cpyLen + " bytes in " + cpyDuration + " msrc.");
            if (null == ret) ret = new LinkedList<Map.Entry<File, File>>();
            ret.add(new MapEntryImpl<File, File>(sFile, dFile));
        }
        final String[] dstFiles = dstFolder.list();
        if ((null == dstFiles) || (dstFiles.length <= 0)) return ret;
        final Collection<String> srcSet = new HashSet<String>(Arrays.asList(srcFiles));
        for (final String d : dstFiles) {
            if (srcSet.contains(d)) continue;
            final File dFile = new File(dstFolder, d);
            if (dFile.delete()) out.println("\tDeleted " + dFile); else System.err.println("Failed to delete " + dFile);
        }
        return ret;
    }
