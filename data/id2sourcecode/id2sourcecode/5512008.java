    public OutputStream getOutputStream(DataEntry dataEntry, Finisher finisher) throws IOException {
        OutputStream parentOutputStream = dataEntryWriter.getOutputStream(dataEntry.getParent(), this);
        if (parentOutputStream == null) {
            return null;
        }
        if (currentParentOutputStream == null) {
            currentParentOutputStream = parentOutputStream;
            currentJarOutputStream = manifest != null ? new JarOutputStream(parentOutputStream, manifest) : new ZipOutputStream(parentOutputStream);
            if (comment != null) {
                currentJarOutputStream.setComment(comment);
            }
        }
        String name = dataEntry.getName();
        if (!name.equals(currentEntryName)) {
            closeEntry();
            if (!jarEntryNames.add(name)) {
                throw new IOException("Duplicate zip entry [" + dataEntry + "]");
            }
            currentJarOutputStream.putNextEntry(new ZipEntry(name));
            currentFinisher = finisher;
            currentEntryName = name;
        }
        return currentJarOutputStream;
    }
