    private static String copyFileToReporter(Reporter reporter, File f) throws IOException {
        if (!f.exists()) {
            throw new FileNotFoundException("File not found: " + f.getAbsolutePath());
        }
        File destination = new File(reporter.getCurrentTestFolder(), f.getName());
        FileUtils.copyFile(f, destination);
        return destination.getAbsolutePath();
    }
