    protected List<FolderModel> getModelChildren() {
        List<FolderModel> children = new ArrayList<FolderModel>();
        children.add(new FolderModel(DBPlugin.getResourceString("label.table"), (RootModel) getModel()) {

            @Override
            public List<?> getChildren() {
                String filterText = VisualDBOutlinePage.getFilterText();
                if (filterText.length() == 0) {
                    return root.getTables();
                }
                List<TableModel> result = new ArrayList<TableModel>();
                for (TableModel table : root.getTables()) {
                    if (table.getTableName().startsWith(filterText)) {
                        result.add(table);
                    }
                }
                return result;
            }

            @Override
            public void doEdit() {
            }
        });
        if (VisualDBOutlinePage.getFilterText().length() == 0) {
            children.add(new FolderModel(DBPlugin.getResourceString("label.dommain"), (RootModel) getModel()) {

                @Override
                public List<?> getChildren() {
                    return root.getDommains();
                }

                @Override
                public void doEdit() {
                    new DommainEditAction((GraphicalViewer) UIUtils.getActiveEditor().getAdapter(GraphicalViewer.class)).run();
                }
            });
        }
        return children;
    }
