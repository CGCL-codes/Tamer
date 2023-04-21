    protected void writeInstallerResources() throws IOException {
        sendMsg("Copying " + installerResourceURLMap.size() + " files into installer");
        for (String s : installerResourceURLMap.keySet()) {
            String name = s;
            InputStream in = (installerResourceURLMap.get(name)).openStream();
            org.apache.tools.zip.ZipEntry newEntry = new org.apache.tools.zip.ZipEntry(name);
            long dateTime = FileUtil.getFileDateTime(installerResourceURLMap.get(name));
            if (dateTime != -1) {
                newEntry.setTime(dateTime);
            }
            primaryJarStream.putNextEntry(newEntry);
            copyStream(in, primaryJarStream);
            primaryJarStream.closeEntry();
            in.close();
        }
    }
