    public static void main(String[] args) throws Exception {
        File sourceDirectory = new File(args[0]);
        if (!sourceDirectory.isDirectory()) {
            System.exit(1);
        }
        File targetDirectory = new File(args[1]);
        if (!targetDirectory.isDirectory()) {
            System.exit(1);
        }
        final String localeKey = args.length > 2 ? args[2] : "";
        String[] sourceFiles = sourceDirectory.list(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.matches("FreeColMessages_" + localeKey + ".*\\.properties");
            }
        });
        for (String name : sourceFiles) {
            System.out.println("Processing source file: " + name);
            File sourceFile = new File(sourceDirectory, name);
            Map<String, String> sourceProperties = readFile(sourceFile);
            File targetFile = new File(targetDirectory, name);
            if (targetFile.exists()) {
                Map<String, String> targetProperties = readFile(targetFile);
                List<Entry<?, ?>> missingProperties = new ArrayList<Entry<?, ?>>();
                for (Entry<?, ?> entry : sourceProperties.entrySet()) {
                    if (!targetProperties.containsKey(entry.getKey())) {
                        missingProperties.add(entry);
                    }
                }
                if (!missingProperties.isEmpty()) {
                    FileWriter out = new FileWriter(targetFile, true);
                    out.write("### Merged from trunk on " + DateFormat.getDateTimeInstance().format(new Date()) + " ###\n");
                    for (Entry<?, ?> entry : missingProperties) {
                        out.write((String) entry.getKey());
                        out.write("=");
                        out.write((String) entry.getValue());
                        out.write("\n");
                    }
                    out.close();
                }
            } else {
                System.out.println("Copying " + name + " from trunk.");
                FileReader in = new FileReader(sourceFile);
                FileWriter out = new FileWriter(targetFile);
                int c;
                while ((c = in.read()) != -1) {
                    out.write(c);
                }
                in.close();
                out.close();
            }
        }
    }
