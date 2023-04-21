    public void generate(String pageclassname) throws CodeGeneratorException {
        this.pageClassName = pageclassname;
        this.actorClassName = pageclassname + "Actor";
        this.pageTestClassName = pageclassname + "Test";
        getBrowserInfo();
        try {
            actorClassFile = SourceFile.createNewSourceFile(actorPackageName, actorClassName);
            String actorclassfilename = actorClassFile.getFilename(sourceDirectory);
            boolean createactorclassfile = true;
            if (new File(actorclassfilename).exists()) {
                if (!confirmManager.confirm(actorClassName + " already exists. Do you want to overwrite it?")) {
                    createactorclassfile = false;
                }
            }
            if (createactorclassfile) {
                System.out.print("Generating actor class...");
                generateActorClass();
                System.out.println();
                System.out.println("Generated " + actorMethodUids.size() + " actor methods");
                System.out.println("Writing properties to " + actorPropertiesFile.getFilename());
                actorPropertiesFile.replacePropertiesForClass(actorProperties, actorClassDefinition.getName());
                System.out.println("Writing actor class to " + actorclassfilename);
                actorClassFile.save(sourceDirectory);
            }
            pageClassFile = SourceFile.createNewSourceFile(pagePackageName, pageClassName);
            String pageclassfilename = pageClassFile.getFilename(sourceDirectory);
            if (!new File(pageclassfilename).exists()) {
                System.out.println("Writing page class to " + pageclassfilename);
                generatePageClass();
                pageClassFile.save(sourceDirectory);
            }
            pageTestClassFile = SourceFile.createNewSourceFile(testPackageName, pageTestClassName);
            String pagetestclassfilename = pageTestClassFile.getFilename(sourceDirectory);
            if (!new File(pagetestclassfilename).exists()) {
                System.out.println("Writing test class to " + pagetestclassfilename);
                generatePageTestClass();
                pageTestClassFile.save(sourceDirectory);
            }
        } catch (IOException e) {
            throw new CodeGeneratorException(e);
        }
    }
