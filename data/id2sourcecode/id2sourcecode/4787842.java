    public static URL[] prepareJars(URL[] files, String destinationDir) throws IOException {
        for (int i = 0; i < files.length; i++) {
            if (files[i].getProtocol().toLowerCase().equals("jar")) {
                files[i] = FileUtils.convertFromJarURL(files[i]);
            }
            String filename = FileUtils.getFilename(files[i].getFile());
            if (destinationDir != null) {
                File outputfile = File.createTempFile(filename, null, new File(destinationDir));
                outputfile.deleteOnExit();
                log.debug("JAR file " + files[i].toString() + " has temporary copy at " + outputfile.getPath());
                files[i] = FileUtils.copyFile(files[i], outputfile);
            }
        }
        return files;
    }
