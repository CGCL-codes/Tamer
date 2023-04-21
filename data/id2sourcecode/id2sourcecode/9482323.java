    public long getFilesFromStreamOnlyRootFiles(InputStream in, String FilesDirStr, Hashtable newNamesTable) throws Exception {
        long plussQuotaSize = 0;
        if (!FilesDirStr.endsWith(sep)) {
            FilesDirStr += sep;
        }
        if (null == newNamesTable) {
            newNamesTable = new Hashtable();
        }
        FileUtils.getInstance().createDirectory(FilesDirStr);
        ZipInputStream zin = null;
        zin = new ZipInputStream(new BufferedInputStream(in));
        BufferedOutputStream out = null;
        ZipEntry zipEntry = null;
        while ((zipEntry = zin.getNextEntry()) != null) {
            if (!zipEntry.isDirectory()) {
                String zipEntryName = zipEntry.getName();
                String oldName = zipEntryName;
                String newName = oldName;
                String newFileName = FilesDirStr + newName;
                if (newNamesTable.containsKey(oldName)) {
                    newName = (String) newNamesTable.get(oldName);
                    if ((newName != null) && (!"".equals(newName))) {
                        newFileName = FilesDirStr + newName;
                    } else {
                        newName = oldName;
                    }
                }
                boolean rootFile = true;
                if (new File(zipEntryName).getParentFile() != null) {
                    rootFile = false;
                }
                File newFile = new File(newFileName);
                if (rootFile) {
                    FileUtils.getInstance().createDirectory(newFile.getParent());
                    if (newFile.exists()) {
                        plussQuotaSize -= newFile.length();
                        newFile.delete();
                    }
                    out = new BufferedOutputStream(new FileOutputStream(newFile), bufferSize);
                    int readLen;
                    byte dataBuff[] = new byte[bufferSize];
                    while ((readLen = zin.read(dataBuff)) > 0) {
                        out.write(dataBuff, 0, readLen);
                        plussQuotaSize += readLen;
                    }
                    out.flush();
                    out.close();
                } else {
                    byte dataBuff[] = new byte[bufferSize];
                    while ((zin.read(dataBuff)) > 0) {
                    }
                }
            }
        }
        zin.close();
        return plussQuotaSize;
    }
