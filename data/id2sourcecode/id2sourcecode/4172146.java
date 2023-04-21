    @Override
    protected Control createContents(Composite parent) {
        final Color titleColor = ColorConstants.lightBlue;
        GridLayout windowLayout = new GridLayout();
        windowLayout.marginHeight = 0;
        windowLayout.marginWidth = 0;
        windowLayout.verticalSpacing = 1;
        parent.setLayout(windowLayout);
        final Composite titleComposite = new Composite(parent, SWT.NONE);
        GridLayout titleLayout = new GridLayout(2, false);
        titleLayout.marginHeight = 0;
        titleLayout.marginWidth = 1;
        titleLayout.verticalSpacing = 0;
        GridData titleGridData = new GridData(GridData.FILL_HORIZONTAL);
        titleGridData.heightHint = 16;
        titleComposite.setLayout(titleLayout);
        titleComposite.setLayoutData(titleGridData);
        titleComposite.setBackground(titleColor);
        titleComposite.addMouseListener(this);
        titleComposite.addMouseMoveListener(this);
        Label title = new Label(titleComposite, SWT.NONE);
        title.setBackground(titleColor);
        title.setForeground(ColorConstants.white);
        title.setText(ProcessDesignerMessages.OverviewDialog_Title);
        title.setAlignment(SWT.CENTER);
        GridData labelGridData = new GridData();
        labelGridData.horizontalIndent = 3;
        title.setLayoutData(labelGridData);
        title.addMouseMoveListener(this);
        title.addMouseListener(this);
        FontData fontData = title.getFont().getFontData()[0];
        FontData newFontData = new FontData(fontData.getName(), 8, fontData.getStyle() | SWT.BOLD);
        title.setFont(FontAndColorManager.getInstance().getFont(newFontData));
        Composite buttonsComposite = new Composite(titleComposite, SWT.NONE);
        GridData buttonsGridData = new GridData(SWT.END, SWT.CENTER, true, true);
        buttonsGridData.heightHint = 12;
        buttonsGridData.widthHint = 26;
        buttonsComposite.setLayoutData(buttonsGridData);
        buttonsComposite.setBackground(titleColor);
        buttonsComposite.addMouseMoveListener(this);
        buttonsComposite.addMouseListener(this);
        GridLayout buttonsLayout = new GridLayout(2, true);
        buttonsLayout.marginHeight = 0;
        buttonsLayout.marginWidth = 0;
        buttonsLayout.horizontalSpacing = 2;
        buttonsComposite.setLayout(buttonsLayout);
        Label dockButton = new Label(buttonsComposite, SWT.NONE);
        GridData dockGridData = new GridData(SWT.BEGINNING, SWT.CENTER, true, true);
        dockGridData.heightHint = 12;
        dockGridData.widthHint = 12;
        dockButton.setLayoutData(dockGridData);
        dockButton.setImage(ourDockImage);
        dockButton.setBackground(titleColor);
        dockButton.setCursor(Cursors.HAND);
        Label closeButton = new Label(buttonsComposite, SWT.NONE);
        GridData closeGridData = new GridData(SWT.END, SWT.CENTER, true, false);
        closeGridData.heightHint = 12;
        closeGridData.widthHint = 12;
        closeButton.setLayoutData(closeGridData);
        closeButton.setImage(ourCloseImage);
        closeButton.setBackground(titleColor);
        closeButton.setCursor(Cursors.HAND);
        dockButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseUp(MouseEvent e) {
                moveToDockLocation();
            }
        });
        closeButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseUp(MouseEvent e) {
                myOverviewVisible = false;
                close();
            }
        });
        myCanvas = new Canvas(parent, SWT.NONE);
        myCanvas.setLayoutData(new GridData(GridData.FILL_BOTH));
        myCanvas.setBackground(ColorConstants.white);
        initializeOverview();
        myShellMoveListener = new ControlAdapter() {

            @Override
            public void controlMoved(ControlEvent e) {
                if (myIsDockable) {
                    moveToDockLocation();
                }
            }
        };
        myGraphicalViewer.getControl().getShell().addControlListener(myShellMoveListener);
        myParentResizeListener = new ControlAdapter() {

            @Override
            public void controlResized(ControlEvent e) {
                if (myIsDockable) {
                    moveToDockLocation();
                }
            }
        };
        myGraphicalViewer.getControl().addControlListener(myParentResizeListener);
        myResizeListener = new ControlAdapter() {

            @Override
            public void controlResized(ControlEvent e) {
                if (!getShell().getLocation().equals(getDockableLocation(getShell().getSize()))) {
                    myIsDockable = false;
                }
            }
        };
        myCanvas.addControlListener(myResizeListener);
        myCanvas.addFocusListener(myFocusAdapter);
        return myCanvas;
    }
