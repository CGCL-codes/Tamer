    private void putNextEntry(ZipEntry newEntry) throws IOException, IllegalStateException {
        if (outputJar == null) {
            throw new IllegalStateException();
        }
        outputJar.putNextEntry(newEntry);
        entryNames.add(newEntry.getName());
        if (manifestBuilder != null) {
            manifestBuilder.addEntry(newEntry);
        }
    }
