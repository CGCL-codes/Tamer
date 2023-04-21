    protected boolean doExport(Node data, Map exportOut) {
        String exportName = XmlUtils.getStringFromXPath(data, "name");
        File baseDir = new File(getRepositoryPath());
        if (baseDir.exists() == false) {
            if (baseDir.mkdirs() == false) {
                lastError = "error.cannotcreateoutputdirectory";
                return false;
            }
        }
        File zipFile = new File(baseDir, exportName + ".zip");
        if (zipFile.exists()) {
            lastError = "error.alreadyexist";
            return false;
        }
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(zipFile);
        } catch (FileNotFoundException e) {
            lastError = "error.filenotfound";
            return false;
        }
        ZipOutputStream zos = new ZipOutputStream(fos);
        Node projectNode = action.getNodeFromXPath("/project");
        String projectXml = "<!DOCTYPE project PUBLIC \"" + VictorAction.VICTOR_PUBLIC_ID + "\" \"http://dtd.indigen.com/victor.dtd\">\n" + XmlUtils.serialize(projectNode);
        ByteArrayInputStream bais;
        try {
            bais = new ByteArrayInputStream(projectXml.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            return false;
        }
        try {
            zos.putNextEntry(new ZipEntry("project.xml"));
            XmlUtils.copy(bais, zos);
            bais.close();
            zos.closeEntry();
            File[] dataFiles = action.getBaseDirectory().listFiles();
            for (int i = 0; i < dataFiles.length; i++) {
                zos.putNextEntry(new ZipEntry(dataFiles[i].getName()));
                FileInputStream fis = new FileInputStream(new File(dataFiles[i].getAbsolutePath()));
                XmlUtils.copy(fis, zos);
                fis.close();
                zos.closeEntry();
            }
            zos.close();
        } catch (IOException e) {
            action.getActionLogger().error("doExport failed on " + zipFile.getAbsolutePath() + ": " + e);
            lastError = "error.ioerror";
            return false;
        }
        return true;
    }
