    private File loadFile(String fileName) {
        File BasicDirectory = new File("plugins/BasicBukkit/");
        if (!BasicDirectory.exists()) {
            BasicDirectory.mkdir();
            System.out.println("### BasicBukkut has created the BasicBukkit plugin directory");
        }
        File config = new File("plugins/BasicBukkit/" + fileName);
        if (!config.exists()) {
            InputStream defaultFile = getClass().getClassLoader().getResourceAsStream(fileName);
            try {
                System.out.println("### BasicBukkit did not detect a config file: createed new file \"" + fileName + "\"");
                BufferedWriter out = new BufferedWriter(new FileWriter("plugins/BasicBukkit/" + fileName));
                while (defaultFile.available() > 0) out.write(defaultFile.read());
                out.close();
            } catch (Exception e) {
                System.out.println("### BasicBukkit warning: " + e.getMessage());
            }
            config = new File("plugins/BasicBukkit/" + fileName);
        }
        return config;
    }
