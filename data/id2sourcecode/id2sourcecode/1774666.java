    protected void initializeGraphicalViewer() {
        getGraphicalViewer().setEditPartFactory(new MyEditPartFactory(form));
        getGraphicalViewer().setContents(form);
        System.out.println(getGraphicalViewer());
        System.out.println(getPaletteViewer());
        System.out.println(getPaletteRoot());
        System.out.println(getInitialPaletteSize());
        System.out.println(getPaletteViewer().getControl());
        Composite parent = (Composite) getPaletteViewer().getControl();
        int i = 1;
        do {
            System.out.println("Parent No. " + i + " is a " + parent.getClass());
            System.out.println("***********************************************");
            System.out.println("Children : ");
            for (int j = 0; j < parent.getChildren().length; j++) {
                System.out.println(j + ". is a " + parent.getChildren()[j].getClass());
                System.out.println("... Size : " + parent.getChildren()[j].getBounds());
            }
            i++;
            parent = parent.getParent();
        } while (parent != null);
    }
