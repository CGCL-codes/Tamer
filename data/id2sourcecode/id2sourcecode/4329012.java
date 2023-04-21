    public void createPartControl(Composite parent) {
        Splitter splitter = new Splitter(parent, SWT.CENTER);
        createPaletteViewer(splitter);
        Canvas canvas = new Canvas(splitter, SWT.CENTER);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        canvas.setSize(720, 560);
        canvas.setBounds(100, 100, 720, 560);
        canvas.setBackground(new Color(Display.getCurrent(), 200, 200, 200));
        createGraphicalViewer(canvas);
        splitter.maintainSize(getPaletteViewer().getControl());
        splitter.setFixedSize(getInitialPaletteSize());
        splitter.addFixedSizeChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                handlePaletteResized(((Splitter) evt.getSource()).getFixedSize());
            }
        });
    }
