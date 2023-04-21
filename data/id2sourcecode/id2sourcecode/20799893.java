    public static void main(String[] args) {
        File homePath = new File(System.getProperty("user.home"));
        File annotationDataPath = new File(new File(homePath, ".amaya"), "annotations");
        if (annotationDataPath.isDirectory()) {
            System.out.println("Found location: " + annotationDataPath);
        }
        File annotationIndexFile = new File(annotationDataPath, "annot.index");
        Map<String, String> annotationIndexMap = null;
        try {
            annotationIndexMap = loadAnnotationIndexMap(annotationIndexFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        if (annotationIndexMap.size() > 0) {
            for (String documentLocation : annotationIndexMap.keySet()) {
                System.out.println("\n\ndocument location: " + documentLocation);
                String rdfLocation = annotationIndexMap.get(documentLocation);
                if (rdfLocation != null && (rdfLocation.startsWith("file") || rdfLocation.startsWith("http"))) {
                    try {
                        URL url = new URL(rdfLocation);
                        InputStreamReader isr = new InputStreamReader(url.openStream());
                        showFile(isr);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        URL url = null;
        try {
            url = new URL("http://develvm03/svn/prototypes/ac_documentation/tags/rev_20071011/src/docbkx/interface/interface_engine/interface_engine_book.xml");
            System.out.println("Created url '" + url + "'");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
