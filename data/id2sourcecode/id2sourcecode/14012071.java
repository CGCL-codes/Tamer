    @Override
    public void createSection(final Composite parent, final FormToolkit toolkit, final boolean collapsable) {
        super.createSection(parent, toolkit, collapsable);
        final Composite client = (Composite) section.getClient();
        section.setText("Control Rendering");
        client.setLayout(new FormLayout());
        rendererName = new LabelledTextSelect(client, toolkit) {

            @Override
            protected Command getCommand(final Object textValue) {
                if (input instanceof Control) {
                    return new SetSignatureRendererNameCommand(definitions, (Control) input, textValue.toString());
                }
                return null;
            }

            @Override
            protected IStructuredContentProvider getContentProvider() {
                return new IStructuredContentProvider() {

                    @Override
                    public void dispose() {
                    }

                    @Override
                    public Object[] getElements(final Object inputElement) {
                        return null;
                    }

                    @Override
                    public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
                    }
                };
            }
        };
        rendererName.setLabel("Renderer:");
        rendererName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        rendererName.setCommandStack(commandStack);
        rendererColour = new LabelledTextSelect(client, toolkit) {

            @Override
            protected Command getCommand(final Object textValue) {
                return null;
            }

            @Override
            protected IStructuredContentProvider getContentProvider() {
                return new IStructuredContentProvider() {

                    @Override
                    public void dispose() {
                    }

                    @Override
                    public Object[] getElements(final Object inputElement) {
                        return null;
                    }

                    @Override
                    public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
                    }
                };
            }
        };
        rendererColour.setLabel("Colour:");
        rendererColour.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        rendererColour.setCommandStack(commandStack);
        rendererImage = new LabelledTextSelect(client, toolkit) {

            @Override
            protected Command getCommand(final Object textValue) {
                return null;
            }

            @Override
            protected IStructuredContentProvider getContentProvider() {
                return new IStructuredContentProvider() {

                    @Override
                    public void dispose() {
                    }

                    @Override
                    public Object[] getElements(final Object inputElement) {
                        return null;
                    }

                    @Override
                    public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
                    }
                };
            }
        };
        rendererImage.setLabel("Image:");
        rendererImage.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        rendererImage.setCommandStack(commandStack);
        final GraphicalViewer rendererExample = createGraphicalViewer(client);
        try {
            final Bigraph bigraph = new Bigraph(new DomBigraph(new BasicSignature())) {

                @Override
                public PlaceFigure getRenderer(final Place place) {
                    if (place.isRoot()) {
                        return new HiddenPlaceFigure(place);
                    }
                    return super.getRenderer(place);
                }
            };
            final Place root = bigraph.getBigraph().createRoot();
            bigraph.getBigraph().addRoot(root);
            final Place examplePlace = bigraph.getBigraph().createNode("Example");
            root.addChild(examplePlace);
            rendererExample.setContents(bigraph);
            exampleNode = (PlacePart) rendererExample.getEditPartRegistry().get(examplePlace);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        FormData data = new FormData();
        data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
        data.left = new FormAttachment(0, 0);
        data.width = 120;
        rendererExample.getControl().setLayoutData(data);
        data = new FormData();
        data.top = new FormAttachment(0, ITabbedPropertyConstants.VMARGIN);
        data.left = new FormAttachment(rendererExample.getControl(), ITabbedPropertyConstants.HMARGIN);
        data.right = new FormAttachment(100, -ITabbedPropertyConstants.HSPACE);
        rendererName.setLayoutData(data);
        data = new FormData();
        data.top = new FormAttachment(rendererName, 3);
        data.left = new FormAttachment(rendererExample.getControl(), ITabbedPropertyConstants.HMARGIN);
        data.right = new FormAttachment(100, -ITabbedPropertyConstants.HSPACE);
        rendererColour.setLayoutData(data);
        data = new FormData();
        data.top = new FormAttachment(rendererColour, 3);
        data.left = new FormAttachment(rendererExample.getControl(), ITabbedPropertyConstants.HMARGIN);
        data.right = new FormAttachment(100, -ITabbedPropertyConstants.HSPACE);
        rendererImage.setLayoutData(data);
    }
