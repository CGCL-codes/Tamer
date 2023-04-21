    public GraphicalViewer(Composite parent) {
        canvas = new Canvas(parent, SWT.NONE);
        parent.addListener(SWT.Resize, new Listener() {

            public void handleEvent(Event event) {
                canvas.setBounds(canvas.getParent().getClientArea());
                requestImageRedraw();
            }
        });
        canvas.addPaintListener(new PaintListener() {

            public void paintControl(PaintEvent e) {
                redrawImageIfRequested();
                GC gc = e.gc;
                paintCanvas(gc);
            }
        });
    }
