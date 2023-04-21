    public File generate(String rootName, String name) {
        File root = new File(rootName, name);
        if (root.exists()) {
            if (force) {
                FileSystemUtil.deleteDirR(root, FS);
            } else {
                System.out.printf("%s: such folder already exists!", root.getName());
                return null;
            }
        }
        int depth = random.nextInt(maxDepth + 1);
        generateDir(new File(rootName), name, depth);
        return root;
    }
