    public DiagramVisualizer(final DiagramVisualizerSettings settings, Composite parent) {
        settings.checkRootEditPart();
        mySettings = settings;
        myPageBook = new PageBook(parent, SWT.NONE);
        myMessagePane = new Composite(myPageBook, SWT.NONE);
        myMessagePane.setLayout(new FillLayout());
        myMessageLabel = new Label(myMessagePane, SWT.NONE);
        if (mySettings.isInfiniteDiagramSize()) {
            myViewer = new InfiniteGraphicalViewer();
        } else {
            myViewer = new FiniteGraphicalViewer();
        }
        myViewer.setRootEditPart(mySettings.getRootEditPart());
        myViewer.setEditPartFactory(mySettings.getEditPartFactory());
        myViewer.setEditDomain(new EditDomain());
        myDiagramPane = new Composite(myPageBook, SWT.NONE);
        myDiagramPane.setLayout(new FillLayout());
        if (mySettings.getToolbarBuilder() == null) {
            myViewer.createControl(myDiagramPane);
        } else {
            Composite c = new Composite(myDiagramPane, SWT.NONE);
            GridLayout gridLayout = new GridLayout();
            gridLayout.marginHeight = 0;
            gridLayout.marginWidth = 0;
            gridLayout.verticalSpacing = 0;
            c.setLayout(gridLayout);
            CoolBar coolbar = new CoolBar(c, SWT.FLAT);
            coolbar.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
            Label separator = new Label(c, SWT.SEPARATOR | SWT.HORIZONTAL);
            separator.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
            myViewer.createControl(c);
            myViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
            CoolBarManager coolBarManager = new CoolBarManager(coolbar);
            for (Iterable<IAction> toolbar : mySettings.getToolbarBuilder().buildToolbar(getViewer())) {
                ToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT | SWT.WRAP | SWT.HORIZONTAL);
                for (IAction action : toolbar) {
                    toolBarManager.add(action);
                }
                coolBarManager.add(toolBarManager);
            }
            coolBarManager.update(false);
        }
        if (mySettings.getContentMenu() != null) {
            myViewer.setContextMenu(mySettings.getContentMenu());
        }
        mySelectionProvider = new StructuredSelectionConverter(myViewer, new ObjectConverterImpl()) {

            @Override
            protected void setConvertedSelection(ISelection selection, boolean forceListen) {
                super.setConvertedSelection(selection, forceListen);
                if (!selection.isEmpty()) {
                    EditPart editPart = (EditPart) ((IStructuredSelection) selection).getFirstElement();
                    myViewer.reveal(editPart);
                }
            }

            @Override
            protected IStructuredSelection convertFromOriginal(IStructuredSelection originalSelection) {
                IStructuredSelection selection = super.convertFromOriginal(originalSelection);
                if (selection.isEmpty()) {
                    if (mySettings.getModelProvider().getRootUuid() != null) {
                        IElementWrapper elementWrapper = DataCacheManager.getWrapperByUuid(mySettings.getModelProvider().getRootUuid());
                        if ((elementWrapper != null) && !elementWrapper.isGhost()) {
                            return new StructuredSelection(elementWrapper);
                        }
                    }
                    return StructuredSelection.EMPTY;
                } else if ((selection.size() == 1) && FORBID_SELECTION.equals(selection.getFirstElement())) {
                    return StructuredSelection.EMPTY;
                } else {
                    return selection;
                }
            }
        };
        showDiagram();
    }
