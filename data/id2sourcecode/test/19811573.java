    public static void main(String[] args) throws Exception {
        CN2Helper helper = new CN2Helper();
        File input = new File(args[0]);
        File output = new File(args[1]);
        for (File d : input.listFiles(new FileFilter() {

            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        })) {
            for (String file : d.list(new FilenameFilter() {

                public boolean accept(File dir, String name) {
                    return name.endsWith(".arff");
                }
            })) {
                File dOutput = new File(output, d.getName());
                dOutput.mkdirs();
                helper.write(dOutput, new ArffHelper().read(new File(d, file)));
            }
        }
    }
