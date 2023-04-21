    private void addZipEntry(String path, String fileName, long time, ZipOutputStream zos) throws IOException {
        int collisionCounter = 0;
        do {
            try {
                ZipEntry entry = new ZipEntry(getZipEntryName(path, fileName, collisionCounter));
                entry.setTime(time);
                zos.putNextEntry(entry);
                break;
            } catch (ZipException ze) {
                ++collisionCounter;
            }
        } while (true);
    }
