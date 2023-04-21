    public Hashtable getDirFilesOnlyFromZipStream(InputStream in, String FilesDirStr, String EnabledDirName, String NewEnabledDirName, Hashtable newNamesTable, String uploadID) throws Exception {
        Hashtable plussQuotaSizeHash = new Hashtable();
        String otherKey = "others";
        plussQuotaSizeHash.put(otherKey, new Long(0));
        if (!FilesDirStr.endsWith(sep)) {
            FilesDirStr += sep;
        }
        if (null == newNamesTable) {
            newNamesTable = new Hashtable();
        }
        ZipInputStream zin = null;
        zin = new ZipInputStream(new BufferedInputStream(in));
        BufferedOutputStream out = null;
        ZipEntry zipEntry = null;
        while ((zipEntry = zin.getNextEntry()) != null) {
            if (!zipEntry.isDirectory()) {
                String oldEnabledDirPath = new String(FilesDirStr + EnabledDirName + sep);
                String newEnabledDirPath = new String(FilesDirStr + NewEnabledDirName + sep);
                String oldName = zipEntry.getName();
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
                newFileName = replaceFirst(newFileName, oldEnabledDirPath, newEnabledDirPath);
                File newFile = new File(newFileName);
                boolean storeThisFile = true;
                String runtimeID = new String("");
                if (newFile.getParentFile().getParent().endsWith("outputs")) {
                    runtimeID = newFile.getParentFile().getName();
                    String newRuntimeID = createNewRuntimeID(runtimeID, uploadID);
                    newFileName = replaceFirst(newFileName, runtimeID, newRuntimeID);
                    runtimeID = newRuntimeID;
                    newFile = new File(newFileName);
                    if (!plussQuotaSizeHash.containsKey(runtimeID)) {
                        plussQuotaSizeHash.put(runtimeID, new Long(0));
                    }
                }
                if (!newFileName.startsWith(newEnabledDirPath)) {
                    storeThisFile = false;
                }
                if (storeThisFile) {
                    String hashKey = otherKey;
                    if (!"".equals(runtimeID)) {
                        hashKey = runtimeID;
                    }
                    long plussQuotaSize = ((Long) plussQuotaSizeHash.get(hashKey)).longValue();
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
                    plussQuotaSizeHash.put(hashKey, plussQuotaSize);
                } else {
                    int readLen;
                    byte dataBuff[] = new byte[bufferSize];
                    while ((readLen = zin.read(dataBuff)) > 0) {
                    }
                }
            }
        }
        zin.close();
        return plussQuotaSizeHash;
    }
