    private static boolean calculate(File olderVersionJar, File newerVersionJar, String outputDestination) throws ZipException, IOException {
        ZipFile oldZip = new ZipFile(olderVersionJar);
        ZipFile newZip = new ZipFile(newerVersionJar);
        Enumeration oldEntries = oldZip.entries();
        Enumeration newEntries = newZip.entries();
        HashMap<String, Long> map = new HashMap<String, Long>();
        while (newEntries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) newEntries.nextElement();
            map.put(entry.getName(), entry.getCrc());
        }
        while (oldEntries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) oldEntries.nextElement();
            Long l = map.get(entry.getName());
            if (l != null && l.longValue() == entry.getCrc()) {
                map.remove(entry.getName());
            }
        }
        if (!map.isEmpty()) {
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(outputDestination));
            Set<Entry<String, Long>> set = map.entrySet();
            Iterator<Entry<String, Long>> it = set.iterator();
            while (it.hasNext()) {
                Entry<String, Long> entry = it.next();
                ZipEntry zipEntry = newZip.getEntry(entry.getKey());
                InputStream is = newZip.getInputStream(zipEntry);
                zos.putNextEntry(zipEntry);
                copyInputStream(is, zos);
                zos.closeEntry();
                is.close();
            }
            zos.flush();
            zos.close();
        }
        oldZip.close();
        newZip.close();
        if (map.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
