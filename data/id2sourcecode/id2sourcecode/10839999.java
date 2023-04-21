    public void addPanelClass(String classFilename, InputStream input) throws Exception {
        sendMsg("Adding the (sub)classes for " + classFilename + " ...");
        outJar.putNextEntry(new ZipEntry("com/izforge/izpack/panels/" + classFilename));
        copyStream(input, outJar);
        outJar.closeEntry();
    }
