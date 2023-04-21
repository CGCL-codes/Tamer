    @Test(expected = CannotCreateAntlrPackageException.class)
    public void couldNotCreatePackage() throws InvalidAntlrPackageException, IOException {
        File mockDir = new File("target/mock/lib");
        mockDir.mkdirs();
        JarOutputStream jarStream = new JarOutputStream(new FileOutputStream(new File(mockDir, "test.jar")));
        ZipEntry testEntry = new ZipEntry("test.txt");
        jarStream.putNextEntry(testEntry);
        jarStream.closeEntry();
        jarStream.close();
        AntlrPackageProvider.SINGLETON.create(Path.fromOSString(mockDir.getAbsolutePath()));
    }
