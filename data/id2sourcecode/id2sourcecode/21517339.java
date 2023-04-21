    public static Image createImage(GraphicalViewer viewer) {
        Display display = PlatformUI.getWorkbench().getDisplay();
        ImageCreator runnable = new ImageCreator(viewer);
        display.syncExec(runnable);
        return runnable.img;
    }
