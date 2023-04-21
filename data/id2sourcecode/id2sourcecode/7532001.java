    private ItemBeanSet constructFromFiles(String directory) {
        File dir = new File(directory);
        ItemBeanSet set = new ItemBeanSet();
        if (dir.exists()) {
            if (dir.isDirectory() == false) {
                throw new MUDBootException("Data files should only be specified by directory, not absolute files.");
            } else {
                File[] dataFiles = dir.listFiles(new XMLFileNameFilter());
                for (File dataFile : dataFiles) {
                    log.fine("Processing ItemSet " + dataFile);
                    try {
                        FileInputStream stream = new FileInputStream(dataFile);
                        BeanParser<ItemBeanSet> roomParser = new BeanParser<ItemBeanSet>();
                        ItemBeanSet fileSet = roomParser.parse(stream, ItemBeanSet.class);
                        set.copyFrom(fileSet);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                return set;
            }
        } else {
            System.err.println("Directory does not exist: " + dir);
            return null;
        }
    }
