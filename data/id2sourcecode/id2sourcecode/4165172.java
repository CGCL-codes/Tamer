    public void instanceMain(String[] args) throws Exception {
        if (args.length != 2) {
            printHelp();
            return;
        }
        String sourceOrderXmlFileArg = args[0];
        String destinationH3JobDirArg = args[1];
        File sourceOrderXmlFile = new File(sourceOrderXmlFileArg);
        if (!sourceOrderXmlFile.isFile()) {
            System.err.println("ERROR sourceOrderXmlFileArg is not a file: " + sourceOrderXmlFileArg);
            System.exit(1);
        }
        File destinationH3JobDir = new File(destinationH3JobDirArg);
        org.archive.util.FileUtils.ensureWriteableDirectory(destinationH3JobDir);
        System.out.println("H1 source: " + sourceOrderXmlFile.getAbsolutePath());
        System.out.println("H3 destination: " + destinationH3JobDir.getAbsolutePath());
        System.out.print("Migrating settings...");
        InputStream inStream = getClass().getResourceAsStream("/org/archive/crawler/migrate/migrate-template-crawler-beans.cxml");
        String template = IOUtils.toString(inStream);
        inStream.close();
        Map<String, String> migrateH1toH3Map = getMigrateMap();
        try {
            sourceOrderXmlDom = DOCUMENT_BUILDER.parse(sourceOrderXmlFile);
        } catch (SAXException e) {
            System.err.println("ERROR caught exception parsing input file: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
        Map<String, String> h1simpleSettings = flattenH1Order(sourceOrderXmlDom);
        List<String> notApplicable = new ArrayList<String>();
        List<String> needsAttention = new ArrayList<String>();
        int migrated = 0;
        StringBuilder sb = new StringBuilder();
        for (String key : h1simpleSettings.keySet()) {
            String beanPath = migrateH1toH3Map.get(key);
            String value = h1simpleSettings.get(key);
            System.out.print(".");
            if (beanPath == null) {
                needsAttention.add(key + " " + value);
                continue;
            }
            if (beanPath.startsWith("$")) {
                notApplicable.add(key + " " + value);
                continue;
            }
            if (beanPath.startsWith("*")) {
                if (beanPath.equals("*metadata.userAgentTemplate")) {
                    splitH1userAgent(value, sb);
                    migrated += 2;
                } else {
                    needsAttention.add(key + " " + value);
                }
                continue;
            }
            if (beanPath.startsWith("^")) {
                value = value.toUpperCase();
                beanPath = beanPath.substring(1);
            }
            sb.append(beanPath).append("=").append(value).append("\n");
            migrated++;
        }
        System.out.println();
        System.out.println();
        String beansCxml = template.replace("###MIGRATE_OVERRIDES###", sb.toString());
        File targetBeansXmlFile = new File(destinationH3JobDir, "crawler-beans.cxml");
        FileUtils.writeStringToFile(targetBeansXmlFile, beansCxml);
        File sourceSeedsTxtFile = new File(sourceOrderXmlFile.getParentFile(), "seeds.txt");
        File destinationSeedsTxtFile = new File(destinationH3JobDir, "seeds.txt");
        if (!sourceSeedsTxtFile.isFile()) {
            System.err.println("ERROR sourceSeedsTxtFile not found: " + sourceSeedsTxtFile);
            System.exit(1);
        }
        FileUtils.copyFile(sourceSeedsTxtFile, destinationSeedsTxtFile);
        System.out.println(notApplicable.size() + " settings skipped as not-applicable");
        System.out.println("These are probably harmless, but if the following settings were");
        System.out.println("important to your crawl process, investigate other options.");
        listProblems(notApplicable);
        System.out.println();
        System.out.println(needsAttention.size() + " settings may need attention");
        System.out.println("Please review your original crawl and the created H3 job, for each");
        System.out.println("of the following, and manually update as needed.");
        listProblems(needsAttention);
        System.out.println();
        System.out.println(migrated + " H1 settings successfully migrated to H3 configuration");
        System.out.println();
        System.out.println("Review your converted crawler-beans.cxml at:");
        System.out.println(targetBeansXmlFile.getAbsolutePath());
    }
