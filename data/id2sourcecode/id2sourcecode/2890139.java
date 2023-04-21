    public DirsTree(Composite parent, EdutexConfig config) {
        tree = new Tree(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.SINGLE);
        this.editor = new TreeEditor(this.tree);
        this.edutexFilter = new EdutexFileFilter(config);
        this.closedRootImage = ImageManager.image(this.tree.getDisplay(), "icon_ClosedDrive");
        this.openRootImage = ImageManager.image(this.tree.getDisplay(), "icon_OpenDrive");
        this.closedFolderImage = ImageManager.image(this.tree.getDisplay(), "icon_ClosedFolder");
        this.openFolderImage = ImageManager.image(this.tree.getDisplay(), "icon_OpenFolder");
        this.fileTexImage = ImageManager.image(this.tree.getDisplay(), "image_tex");
        this.filePxpImage = ImageManager.image(this.tree.getDisplay(), "image_pxp");
        this.filePdfImage = ImageManager.image(this.tree.getDisplay(), "image_pdf");
        this.filePsImage = ImageManager.image(this.tree.getDisplay(), "image_ps");
        this.fileDviImage = ImageManager.image(this.tree.getDisplay(), "image_dvi");
        tree.addTreeListener(new TreeAdapter() {

            @Override
            public void treeExpanded(TreeEvent event) {
                final TreeItem item = (TreeItem) event.item;
                Integer type = (Integer) item.getData(TREE_FILE_TYPE);
                if (type == ROOT) item.setImage(openRootImage);
                if (type == DIR) item.setImage(openFolderImage);
                expand(item);
            }

            @Override
            public void treeCollapsed(TreeEvent event) {
                final TreeItem item = (TreeItem) event.item;
                Integer type = (Integer) item.getData(TREE_FILE_TYPE);
                if (type == ROOT) item.setImage(closedRootImage);
                if (type == DIR) item.setImage(closedFolderImage);
            }
        });
    }
