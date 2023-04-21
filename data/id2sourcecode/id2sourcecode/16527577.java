    public static void main(String args[]) throws Exception {
        File largeIcons = new File(Raptor.RESOURCES_DIR + "icons/large/");
        largeIcons.mkdir();
        File mediumIcons = new File(Raptor.RESOURCES_DIR + "icons/medium/");
        mediumIcons.mkdir();
        File smallIcons = new File(Raptor.RESOURCES_DIR + "icons/small/");
        smallIcons.mkdir();
        File tinyIcons = new File(Raptor.RESOURCES_DIR + "icons/tiny/");
        tinyIcons.mkdir();
        @SuppressWarnings("unused") File tinySource = new File("/Users/mindspan/desktop/12x12-free-toolbar-icons/png");
        File smallSource = new File("/Users/mindspan/desktop/16x16-free-toolbar-icons/png");
        File mediumSource = new File("/Users/mindspan/desktop/20x20-free-toolbar-icons/png/20x20");
        File largeSource = new File("/Users/mindspan/desktop/24x24-free-toolbar-icons/png/24x24");
        for (String[] mapping : IMAGE_MAP) {
            FileUtils.copyFiles(new File(smallSource, mapping[1]), new File(smallIcons, mapping[0]));
            FileUtils.copyFiles(new File(mediumSource, mapping[1]), new File(mediumIcons, mapping[0]));
            FileUtils.copyFiles(new File(largeSource, mapping[1]), new File(largeIcons, mapping[0]));
            System.err.println("Processed " + mapping[0]);
        }
    }
