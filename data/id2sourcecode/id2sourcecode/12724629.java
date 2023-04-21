    public OBOSession write(OBOSession history) throws DataAdapterException {
        try {
            ZipOutputStream zipstream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(path)));
            ZipEntry entry = new ZipEntry("main");
            zipstream.putNextEntry(entry);
            zipstream.setLevel(5);
            ObjectOutputStream stream = new ObjectOutputStream(zipstream);
            stream.writeObject(history);
            stream.close();
            return history;
        } catch (Exception e) {
            throw new DataAdapterException(e, "Write error");
        }
    }
