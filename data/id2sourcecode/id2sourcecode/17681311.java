    public static void putDifsInJar(File targetFile, File differenceFile, JProgressBar bar) throws ZipException, IOException {
        File temp = new File(targetFile.getAbsolutePath() + ".temp");
        ZipFile sourceZipFile = new ZipFile(targetFile);
        ZipFile diffZipFile = new ZipFile(differenceFile);
        HashMap<String, ZipEntry> map = new HashMap<String, ZipEntry>();
        Enumeration diffEntries = diffZipFile.entries();
        while (diffEntries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) diffEntries.nextElement();
            map.put(entry.getName(), entry);
        }
        Enumeration sourceEntries = sourceZipFile.entries();
        while (sourceEntries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) sourceEntries.nextElement();
            if (map.get(entry.getName()) == null) {
                map.put(entry.getName(), entry);
            }
        }
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(temp));
        Set<Entry<String, ZipEntry>> set = map.entrySet();
        bar.setMaximum(set.size());
        Iterator<Entry<String, ZipEntry>> it = set.iterator();
        while (it.hasNext()) {
            Entry<String, ZipEntry> entry = it.next();
            zos.putNextEntry(entry.getValue());
            InputStream is = null;
            if (diffZipFile.getEntry(entry.getKey()) != null) {
                is = diffZipFile.getInputStream(entry.getValue());
            } else {
                is = sourceZipFile.getInputStream(entry.getValue());
            }
            bar.setValue(bar.getValue() + 1);
            copyInputStream(is, zos, null);
            is.close();
        }
        zos.close();
        zos.flush();
        sourceZipFile.close();
        diffZipFile.close();
        FileOutputStream fos = new FileOutputStream(targetFile);
        FileInputStream fis = new FileInputStream(temp);
        copyInputStream(fis, fos, null);
        fis.close();
        fos.close();
        temp.delete();
        temp.deleteOnExit();
    }
