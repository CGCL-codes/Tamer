    public void sendDirNameToZipStream(String userbaseDir, String DirStr, ZipOutputStream zipout) throws Exception {
        if (!userbaseDir.endsWith(sep)) {
            userbaseDir += sep;
        }
        if (!DirStr.endsWith(sep)) {
            DirStr += sep;
        }
        String entryPath = userbaseDir + DirStr;
        File dir = new File(entryPath);
        dir.mkdirs();
        if ((dir.exists()) && (dir.isDirectory())) {
            ZipEntry zipEntry = new ZipEntry(DirStr);
            zipEntry.setMethod(ZipEntry.STORED);
            zipEntry.setSize(0);
            zipEntry.setCrc(0);
            zipout.putNextEntry(zipEntry);
            zipout.flush();
            zipout.closeEntry();
        } else {
        }
    }
