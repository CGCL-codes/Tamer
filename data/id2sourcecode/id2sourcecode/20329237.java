    private void addToolbox(final EditDomain editDomain, final GraphicalViewer viewer, VerticalPanel panel) {
        final SelectionTool tool1 = new SelectionTool();
        editDomain.setDefaultTool(tool1);
        final CreationTool tool = new CreationTool();
        tool.setFactory(new CreationFactory() {

            public Object getNewObject() {
                return new OrangeModel();
            }

            public Object getObjectType() {
                return OrangeModel.class;
            }
        });
        final ConnectionCreationTool tool2 = new ConnectionCreationTool();
        tool2.setUnloadWhenFinished(true);
        HorizontalPanel s = new HorizontalPanel();
        s.setSpacing(5);
        panel.add(s);
        tool2.setFactory(new CreationFactory() {

            public Object getNewObject() {
                return new MyConnectionModel();
            }

            public Object getObjectType() {
                return MyConnectionModel.class;
            }
        });
        {
            PushButton pb = new PushButton("Select", new ClickHandler() {

                public void onClick(ClickEvent event) {
                    editDomain.setActiveTool(tool1);
                }
            });
            s.add(pb);
        }
        {
            PushButton pb = new PushButton("Create Node", new ClickHandler() {

                public void onClick(ClickEvent event) {
                    editDomain.setActiveTool(tool);
                }
            });
            s.add(pb);
        }
        {
            PushButton pb = new PushButton("CreateConnection", new ClickHandler() {

                public void onClick(ClickEvent event) {
                    editDomain.setActiveTool(tool2);
                }
            });
            s.add(pb);
        }
    }
