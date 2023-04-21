    public void addResource(String resId, InputStream input) throws Exception {
        sendMsg("Adding resource : " + resId + " ...");
        outJar.putNextEntry(new ZipEntry("res/" + resId));
        copyStream(input, outJar);
        outJar.closeEntry();
    }
