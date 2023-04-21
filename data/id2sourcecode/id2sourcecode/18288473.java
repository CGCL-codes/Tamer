    public static void replace(String file, String[] changes) throws Exception {
        File properties = null;
        if (!(properties = new File(file)).exists()) {
            System.out.println("File does not exist : " + file);
            return;
        }
        System.out.println("Copying file : " + properties.getAbsoluteFile() + " : to : " + properties.getAbsoluteFile() + ".bak");
        FileUtils.copyFile(properties, new File(properties.getAbsoluteFile() + ".bak"));
        changeNonProperty(properties, new File(properties.getAbsoluteFile() + ".bak"), changes);
    }
