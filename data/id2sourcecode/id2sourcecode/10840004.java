    public void addNativeLibrary(String name, InputStream input) throws Exception {
        sendMsg("Adding native library : " + name + " ...");
        outJar.putNextEntry(new ZipEntry("native/" + name));
        copyStream(input, outJar);
        outJar.closeEntry();
        input.close();
    }
