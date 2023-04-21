    public void compress(String packageName) {
        FileOutputStream oDestination;
        ZipOutputStream oOutput;
        try {
            File oDestFile = new File(String.format(this.globalData.getData(IGlobalData.PROJECT_OUTPUT_PATH) + "%s.mnm", packageName));
            if (!oDestFile.exists()) oDestFile.createNewFile();
            oDestination = new FileOutputStream(oDestFile);
            oOutput = new ZipOutputStream(new BufferedOutputStream(oDestination));
            oOutput.setLevel(9);
            for (Module module : modules) {
                String sRelativePath = module.getResource().getProjectRelativePath().toString();
                ZipEntry oEntry = new ZipEntry(sRelativePath);
                oOutput.putNextEntry(oEntry);
                if (module.getDocument() == null) {
                    writeBinary(module.getResource().getLocation().toString(), oOutput);
                } else {
                    writeXml(module.getDocument(), oOutput);
                }
            }
            oOutput.close();
        } catch (Exception e) {
            this.problems.add(new InternalError(null, e.getMessage()));
        }
    }
