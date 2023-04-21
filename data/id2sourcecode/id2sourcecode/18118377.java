    public File toDirectory(String name, File root) throws WSImportException {
        File exists = new File(name);
        if (exists.isDirectory()) {
            return exists;
        }
        if (exists.isFile()) {
            ZipFormat zipFormat = ZipFormat.parseString(name);
            if (zipFormat.isDirectory()) {
                try {
                    InputStream fis = new FileInputStream(exists);
                    File parent = createTempDirectory(root);
                    toDirectory(fis, zipFormat, parent);
                    return parent;
                } catch (FileNotFoundException ex) {
                    throw new WSImportException("Error finding " + exists.getAbsolutePath(), ex, logger);
                } catch (IOException ex) {
                    throw new WSImportException("Error extracting " + exists.getAbsolutePath(), ex, logger);
                }
            }
            throw new WSImportException("File " + name + " is a file not a directory or known zip format.", logger);
        }
        if (name.toLowerCase().startsWith("ftp")) {
            return fromFtpDirectory(name, root);
        }
        try {
            ZipFormat zipFormat = ZipFormat.parseString(name);
            if (zipFormat.isDirectory()) {
                URL url = new URL(name);
                URLConnection connection = url.openConnection();
                long date = connection.getDate();
                InputStream uis = url.openStream();
                File parent = createTempDirectory(root);
                toDirectory(uis, zipFormat, parent);
                return parent;
            }
            throw new IOException("Url " + name + " does not appear to point to known directory zip format.");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new WSImportException("The arguement " + name + " of type String, could not be converted " + "to either a URL or an existing file.", logger);
        }
    }
