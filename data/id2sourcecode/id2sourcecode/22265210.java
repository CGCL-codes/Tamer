    public File toDirectory(String name, File root) throws IOException, FTPException, ParseException {
        File exists = new File(name);
        if (exists.isDirectory()) {
            return exists;
        }
        if (exists.isFile()) {
            if ((name.toLowerCase().endsWith(".zip")) || (name.toLowerCase().endsWith(".tar")) || (name.toLowerCase().endsWith(".tar.gz"))) {
                InputStream fis = new FileInputStream(exists);
                File parent = createTempDirectory(root);
                toDirectory(fis, name, parent);
                return parent;
            }
            throw new IOException("File " + name + " is a file not a directory or known zip format.");
        }
        if (name.toLowerCase().startsWith("ftp")) {
            return fromFtpDirectory(name, root);
        }
        try {
            URL url = new URL(name);
            URLConnection connection = url.openConnection();
            long date = connection.getDate();
            InputStream uis = url.openStream();
            if ((name.toLowerCase().endsWith(".zip")) || (name.toLowerCase().endsWith(".tar")) || (name.toLowerCase().endsWith(".tar.gz"))) {
                File parent = createTempDirectory(root);
                toDirectory(uis, name, parent);
                return parent;
            }
            throw new IOException("Url " + name + " does not appear to point to known zip format.");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new IOException("The arguement " + name + " of type String, could not be converted " + "to either a URL or an existing file.");
        }
    }
