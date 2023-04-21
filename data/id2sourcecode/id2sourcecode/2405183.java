    private void createEntry(String id) {
        try {
            if (entry != null) {
                if (id.equals(entry.getName())) return; else zip.closeEntry();
            }
            Statistics.eventCount++;
            entry = new ZipEntry(id);
            zip.putNextEntry(entry);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
