    private static void writeToZip(File f, Serializable o) throws PersistenceException {
        ZipOutputStream zos = null;
        FileOutputStream oos = null;
        try {
            zos = new ZipOutputStream(new FileOutputStream(f));
            ZipEntry entry = new ZipEntry("entry");
            zos.putNextEntry(entry);
            XStream xs = new XStream();
            xs.toXML(o, zos);
            zos.closeEntry();
        } catch (IOException e) {
            throw new PersistenceException("Could not write " + o.getClass().getName() + " to " + f.getAbsolutePath() + ".", e);
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                }
            }
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                }
            }
        }
    }
