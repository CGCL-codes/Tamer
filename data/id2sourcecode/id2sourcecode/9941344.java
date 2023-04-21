    public void save(File targetDir) throws IOException {
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.addDefaultExcludes();
        scanner.setBasedir(getBaseDir());
        scanner.setIncludes(getIncludes());
        scanner.setExcludes(getExcludes());
        scanner.scan();
        System.out.println("Results using scanner");
        System.out.println("From: " + new File(getBaseDir()).getAbsolutePath());
        System.out.println("To: " + targetDir.getPath());
        String[] included = scanner.getIncludedFiles();
        for (int i = 0; i < included.length; i++) {
            String includedFile = included[i];
            System.out.println("\tFile: " + includedFile);
            FileUtils.copyFile(new File(getBaseDir(), includedFile), new File(targetDir, includedFile));
        }
    }
