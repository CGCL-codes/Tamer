    public Question getQuestionFromXML(String filename, String testLocation) {
        Question question = new Question();
        try {
            question.setQtiAssItem(getAssessmentItemFromDisk(filename));
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
        Document qDoc = getDocForAssItem(question.getQtiAssItem());
        question.setSourceAsDoc(qDoc);
        long size = ((new File(filename)).length());
        question.setSize(size);
        String id = "id-" + MiscUtils.miniUUID();
        String newQdir = testLocation + sep + id;
        new File(newQdir).mkdir();
        try {
            FileUtils.copyFileToDirectory(new File(filename), new File(newQdir));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String testBaseDir = new File(testLocation).getAbsolutePath();
        String replacementString;
        if (System.getProperty("os.name").contains("Linux")) {
            replacementString = tempdir + "/spectemp";
        } else {
            replacementString = tempdir + "spectemp";
        }
        String relativeTestDir = testBaseDir.replace(replacementString, "");
        if (sep.equals("\\")) {
            testLocation = testLocation.replaceAll("\\\\", "/");
            testBaseDir = testBaseDir.replaceAll("\\\\", "/");
            relativeTestDir = relativeTestDir.replaceAll("\\\\", "/");
            filename = filename.replaceAll("\\\\", "/");
        }
        if (!relativeTestDir.equals("")) {
            if (relativeTestDir.charAt(0) == '/') relativeTestDir = relativeTestDir.substring(1, relativeTestDir.length());
            if (relativeTestDir.charAt(relativeTestDir.length() - 1) != '/') relativeTestDir = relativeTestDir + "/";
        }
        String[] x;
        x = filename.split("/");
        String justTheFilename = x[x.length - 1];
        question.setMyid(id);
        question.setRefId(id);
        question.setHref(id + "/" + justTheFilename);
        question.setBaseHref(relativeTestDir + question.getHref());
        FileType ft = new FileType();
        ft.setHref(question.getBaseHref());
        question.getFiles().add(ft);
        MetadataType metadata = new MetadataType();
        LomType lom = new LomType();
        GeneralType general = new GeneralType();
        DescriptionType desc = new DescriptionType();
        LangstringType langStr = new LangstringType();
        langStr.setLang("en");
        langStr.setValue("This question is an XML file imported into Spectatus.");
        desc.getLangstring().add(langStr);
        general.getContent().add(desc);
        lom.setGeneral(general);
        try {
            metadata.getAny().add(lomToDoc(lom).getDocumentElement());
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        question.setMetadata(metadata);
        return question;
    }
