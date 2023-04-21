    public void manageCppAdditionalFiles(PogoDeviceClass new_model) throws Exception {
        ArrayList<String> generated = new ArrayList<String>();
        for (String filename : cppFilenames) generated.add(Utils.strReplace(filename, "ClassName", old_model.class_name));
        String new_path = new_model.getDescription().getSourcePath();
        String old_path = old_model.projectFiles.getPath();
        ArrayList<String> files = Utils.getInstance().getFileList(old_path);
        ArrayList<String> addFiles = new ArrayList<String>();
        for (String filename : files) {
            boolean found = false;
            for (String cppFile : generated) if (filename.equals(cppFile)) found = true;
            if (!found) addFiles.add(filename);
        }
        for (String filename : addFiles) {
            System.out.println("Copiing " + filename);
            ParserTool.writeFile(new_path + "/" + filename, ParserTool.readFile(old_path + "/" + filename));
        }
        String oldCFfile = old_path + "/ClassFactory.cpp";
        if (new File(oldCFfile).exists()) {
            String newCFfile = new_model.getDescription().getSourcePath() + "/ClassFactory.cpp";
            ParserTool.writeFile(newCFfile, "/*----- PROTECTED REGION ID(ClassFactory.cpp) ENABLED START -----*/\n" + ParserTool.readFile(oldCFfile) + "/*----- PROTECTED REGION END -----*/\n");
            System.out.println("ClassFactory.cpp  updated");
        }
        File makefile = new File(old_path + "/Makefile");
        if (makefile.exists()) {
            String new_makefile_name = new_path + "/Makefile";
            String code = "#PROTECTED REGION ID(" + new_model.getName() + "Makefile) ENABLED START#\n" + ParserTool.readFile(makefile.toString()) + "#PROTECTED REGION END#\n";
            ParserTool.writeFile(new_makefile_name, code);
        } else {
            StringBuilder objFiles = new StringBuilder();
            for (String filename : addFiles) if (filename.endsWith(".cpp")) {
                objFiles.append("	\\\n		$(OBJS_DIR)/");
                objFiles.append(filename.substring(0, filename.length() - 3));
                objFiles.append("o");
            }
            if (objFiles.length() > 0) {
                try {
                    PogoParser new_parser = new PogoParser(new_path + "/Makefile");
                    new_parser.addObjFiles(objFiles.toString());
                } catch (Exception e) {
                    System.err.println(e);
                }
            }
        }
    }
