    public static void main(String[] args) throws Exception {
        C45Helper helper = new C45Helper();
        File input = new File(args[0]);
        File output = new File(args[1]);
        for (File d : input.listFiles(new FileFilter() {

            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        })) {
            for (String file : d.list(new FilenameFilter() {

                public boolean accept(File dir, String name) {
                    return name.startsWith("TR") && name.endsWith(".arff");
                }
            })) {
                File dOutput = new File(output, d.getName());
                dOutput.mkdirs();
                helper.write(dOutput, new ArffHelper().read(new File(d, file)));
                helper.write(dOutput, new ArffHelper().read(new File(d, file.replace("TR", "TS"))));
                File testFile = new File(dOutput, file.replace("TR", "TS").replace(".arff", ".data"));
                testFile.renameTo(new File(dOutput, testFile.getName().replace("TS", "TR").replace(".data", ".test")));
                testFile = new File(dOutput, file.replace("TR", "TS").replace(".arff", ".names"));
                testFile.delete();
            }
        }
    }
