    public boolean saveQuestionToZip(Question question, String zipfilename, AssessmentContentPackage cp) {
        ManifestType cpMani = createRootManifest(cp, FileOps.PURE);
        ResourceType qRes = null;
        for (ResourceType rt : cpMani.getResources().getResource()) {
            if (rt.getHref().contains(question.getHref())) qRes = rt;
            if (rt.getIdentifier().equals(question.getMyid())) qRes = rt;
        }
        if (qRes == null) {
            return false;
        }
        String replacementString;
        String originalString;
        if (System.getProperty("os.name").contains("Linux")) {
            originalString = tempdir + "/spectemp";
            replacementString = tempdir + "/tempqexport";
        } else {
            originalString = tempdir + "spectemp";
            replacementString = tempdir + "tempqexport";
        }
        deleteDirectory(new File(replacementString));
        for (FileType f : qRes.getFile()) {
            String originalFilename = originalString + "/" + f.getHref();
            String newFilename = replacementString + "/" + f.getHref();
            try {
                FileUtils.copyFile(new File(originalFilename), new File(newFilename));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ManifestType qMani = new ManifestType();
        qMani.setMetadata(question.getMetadata());
        qMani.setResources(new ResourcesType());
        qMani.getResources().getResource().add(qRes);
        try {
            saveManifestToDisk(qMani, replacementString + "/imsmanifest.xml");
            zipDirectory(new File(replacementString), new File(zipfilename));
            deleteDirectory(new File(replacementString));
        } catch (Exception e) {
            e.printStackTrace();
            deleteDirectory(new File(replacementString));
            return false;
        }
        return true;
    }
