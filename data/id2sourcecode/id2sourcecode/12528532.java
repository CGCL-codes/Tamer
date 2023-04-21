    public static void saveConfiguration() {
        try {
            FileOutputStream out = new FileOutputStream("docco.prop");
            properties.store(out, "---  Docco settings ---");
            out.close();
        } catch (FileNotFoundException e) {
            System.out.println("Could not write session information, most likely" + "since running on a read-only file system. Session management" + "is disabled.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
