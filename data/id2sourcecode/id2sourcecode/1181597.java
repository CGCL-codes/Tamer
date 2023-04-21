    public static boolean changeDir(File root, double changeLevel) {
        File[] childs = root.listFiles();
        if (childs.length == 0) {
            for (int i = 0; i < 5; i++) {
                if (!generateFile(root)) {
                    return false;
                }
            }
        }
        for (File f : childs) {
            if (f.isFile() && (random.nextFloat() < changeLevel)) {
                float rnd = random.nextFloat();
                if (rnd <= 0.33) {
                    if (!f.renameTo(new File(root, generateName()))) {
                        return false;
                    }
                } else if (rnd < 0.75) {
                    if (!f.delete()) {
                        return false;
                    }
                } else if (rnd <= 1) {
                    if (!generateFile(root)) {
                        return false;
                    }
                } else {
                }
            } else if (f.isDirectory()) {
                if (!changeDir(f, changeLevel)) {
                    return false;
                }
            }
        }
        return true;
    }
