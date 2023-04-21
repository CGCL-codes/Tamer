    public void generate() throws Exception {
        StaticDef staticDef = myProfile.getMessage();
        myMessageName = staticDef.getMsgStructID();
        SourceGenerator.makeDirectory(myTargetDirectory + "/message");
        SourceGenerator.makeDirectory(myTargetDirectory + "/segment");
        SourceGenerator.makeDirectory(myTargetDirectory + "/group");
        String chapter = "";
        String version = myProfile.getHL7Version();
        GroupDef group = convertToGroupDef(staticDef, version);
        String basePackageName = DefaultModelClassFactory.getVersionPackageName(version);
        String[] datatypePackages;
        switch(myGenerateDataTypes) {
            default:
            case NONE:
                datatypePackages = new String[] { basePackageName + "datatype" };
                break;
            case SINGLE:
                datatypePackages = new String[] { myBasePackage + "datatype" };
                SourceGenerator.makeDirectory(myTargetDirectory + "/datatype");
        }
        boolean haveGroups = myGroupDefs.size() > 0;
        {
            String parent = myTargetDirectory + "message/";
            FileUtils.forceMkdir(new File(parent));
            String fileName = parent + staticDef.getMsgStructID() + "." + myFileExt;
            ourLog.info("Writing Message file: " + fileName);
            MessageGenerator.writeMessage(fileName, group.getStructures(), myMessageName, chapter, version, group, myBasePackage, haveGroups, myTemplatePackage);
        }
        for (GroupDef next : myGroupDefs) {
            String parent = myTargetDirectory + "group/";
            FileUtils.forceMkdir(new File(parent));
            String fileName = parent + next.getName() + "." + myFileExt;
            ourLog.info("Writing Group file: " + fileName);
            GroupGenerator.writeGroup(next.getName(), fileName, next, version, myBasePackage, myTemplatePackage, next.getDescription());
        }
        Set<String> alreadyWrittenDatatypes = new HashSet<String>();
        Set<String> alreadyWrittenSegments = new HashSet<String>();
        for (SegmentDef next : mySegmentDefs) {
            alreadyWrittenSegments.add(next.getName());
            String parent = myTargetDirectory + "segment/";
            FileUtils.forceMkdir(new File(parent));
            String fileName = parent + next.getName() + "." + myFileExt;
            ourLog.info("Writing Segment file: " + fileName);
            String segmentName = next.getName();
            String description = next.getDescription();
            ArrayList<SegmentElement> elements = mySegmentNameToSegmentElements.get(segmentName);
            for (SegmentElement nextElement : elements) {
                if ("*".equals(nextElement.type) || "VARIES".equals(nextElement.type)) {
                    nextElement.type = "Varies";
                }
            }
            SegmentGenerator.writeSegment(fileName, version, segmentName, elements, description, myBasePackage, datatypePackages, myTemplatePackage);
            switch(myGenerateDataTypes) {
                case SINGLE:
                    for (DatatypeDef nextFieldDef : next.getFieldDefs()) {
                        writeDatatype(nextFieldDef, alreadyWrittenDatatypes, version);
                    }
            }
        }
        if ("json".equals(myFileExt)) {
            String fileName = myTargetDirectory + "/structures." + myFileExt;
            ourLog.info("Writing Structures file: " + fileName);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, false), SourceGenerator.ENCODING));
            String templatePackage = myTemplatePackage.replace(".", "/");
            Template template = VelocityFactory.getClasspathTemplateInstance(templatePackage + "/available_structures.vsm");
            Context ctx = new VelocityContext();
            ctx.put("messages", Collections.singletonList(myMessageName));
            ctx.put("segments", alreadyWrittenSegments);
            ctx.put("datatypes", alreadyWrittenDatatypes);
            template.merge(ctx, out);
            out.flush();
            out.close();
        }
    }
