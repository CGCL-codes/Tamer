    public void save(String basePath) throws CannotWriteFileException, GenericConfigurationTrasformException {
        String fullName = basePath + File.separator + cName;
        File f = new File(fullName);
        if (f.exists()) throw new CannotWriteFileException(cName, "File already exists");
        if (!new File(fullName.substring(0, fullName.lastIndexOf(File.separator))).mkdirs()) throw new CannotWriteFileException(cName, "Cannot create intermediate directories");
        try {
            if (!f.createNewFile() || !f.canWrite()) throw new CannotWriteFileException(cName, "Unable to write to file (probably read-only)");
        } catch (IOException e) {
            throw new CannotWriteFileException(cName, e.getCause() + " - " + e.getLocalizedMessage());
        }
        ConfigurationParser.save(xmlDocument, fullName);
    }
