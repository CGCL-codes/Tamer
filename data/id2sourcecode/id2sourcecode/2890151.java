    public final void expandToDir(File dir) {
        if (dir == null || dir.isFile()) return;
        ArrayList<File> parentDirs = new ArrayList<File>();
        File parent = dir;
        while (parent != null) {
            parentDirs.add(parent);
            parent = parent.getParentFile();
        }
        if (parentDirs.size() == 0) return;
        TreeItem[] roots = this.tree.getItems();
        TreeItem r = null;
        int indexRoot = -1;
        for (TreeItem it : roots) {
            if (parentDirs.contains(it.getData(TREE_FILE))) {
                r = it;
                indexRoot = parentDirs.indexOf(it.getData(TREE_FILE));
                break;
            }
        }
        if (r == null) {
            this.tree.setTopItem(this.tree.getItem(0));
            for (TreeItem it : roots) it.setExpanded(false);
            return;
        }
        ArrayList<TreeItem> items = null;
        items = this.expand(r);
        r.setExpanded(true);
        for (int i = indexRoot - 1; i >= 0; i--) {
            File current = parentDirs.get(i);
            r = null;
            for (TreeItem it : items) {
                if (it.getData(TREE_FILE).equals(current)) {
                    r = it;
                    break;
                }
            }
            if (r == null) break; else {
                items = this.expand(r);
                r.setExpanded(true);
                if (i == 0) {
                    this.tree.setSelection(r);
                    this.tree.setTopItem(r);
                }
            }
        }
    }
