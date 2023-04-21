    public File[] listFiles(String absolutePath) {
        File file = new File(absolutePath);
        if (!file.exists() || !file.isDirectory()) return null;
        ScriptItem item = getItemFromAbsolutePath(absolutePath, items);
        ArrayList<ScriptItem> children = null;
        if (item == null) children = items; else children = item.getItems();
        if (children == null || children.size() == 0) return null;
        deleteUnexistingItems(children, false);
        detectNewItems((item == null) ? SCRIPT_ROOT : item.getRelativePath(), children, 1);
        if (children == null || children.size() == 0) return null;
        int childrenCount = children.size();
        File[] result = new File[childrenCount];
        int childrenIndex;
        for (childrenIndex = 0; childrenIndex < childrenCount; childrenIndex++) {
            ScriptItem child = children.get(childrenIndex);
            result[childrenIndex] = child.getFile();
        }
        return result;
    }
