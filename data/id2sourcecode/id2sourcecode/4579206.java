    public static boolean writeTo(GridFSDBFile dbFile, String filename) {
        File tempFile = new File(filename);
        String prefix = FileUtils.trimExtension(filename);
        String suffix = FileUtils.getExtension(filename);
        try {
            if (tempFile.exists()) {
                String newName = prefix + " [ " + Calendar.getInstance().getTimeInMillis() + " ]." + suffix;
                dbFile.writeTo(newName);
                System.out.println("=> File [ " + filename + " ] already exists , [ " + newName + " ] renamed file writed successfully ...");
                return true;
            }
            dbFile.writeTo(filename);
            System.out.println("=> File [ " + filename + " ] writed successfully ...");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
