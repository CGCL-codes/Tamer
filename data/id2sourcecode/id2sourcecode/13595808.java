    private static void createReport(Runner runner, File directory) {
        try {
            Connection con = runner.getConnection();
            Collection collection = con.getCollection("benchmark");
            if (collection == null) {
                Collection root = con.getCollection();
                CollectionManagementService cmgt = (CollectionManagementService) root.getService("CollectionManagementService", "1.0");
                collection = cmgt.createCollection("benchmark");
            }
            for (Iterator<?> i = FileUtils.iterateFiles(directory, new String[] { "xml" }, false); i.hasNext(); ) {
                File file = (File) i.next();
                Resource resource = collection.createResource(file.getName(), "XMLResource");
                resource.setContent(file);
                collection.storeResource(resource);
            }
            XQueryService service = (XQueryService) collection.getService("XQueryService", "1.0");
            ResourceSet result = service.execute(new ClassLoaderSource("/org/exist/performance/log2html.xql"));
            if (directory == null) directory = new File(System.getProperty("user.dir"));
            File htmlFile = new File(directory, "results.html");
            FileUtils.writeStringToFile(htmlFile, result.getResource(0).getContent().toString(), "UTF-8");
            FileUtils.copyFile(CSS_FILE, new File(directory, CSS_FILE.getName()));
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("ERROR: " + e.getMessage());
        }
    }
