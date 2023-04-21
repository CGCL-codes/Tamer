    private void executeCopySecondaryVersionItem() {
        for (Metadatacollection.Metadata mds : secondaryDataList) for (Metadatacollection.Metadata mdp : primaryDataList) {
            if (casc.compare(mds, mdp) == 0) {
                String aRelativePath = createStructure ? (version == 'p' ? mdp.getRelativepath() : mds.getRelativepath()) : "";
                File srcFile = getFile(svPath, mds.getRelativepath(), mds.getName());
                File destFile = getFile(path, aRelativePath, mds.getName());
                try {
                    FileUtils.copyFile(srcFile, destFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
