    protected void initializeGraphicalViewer() {
        getGraphicalViewer().setEditPartFactory(new EditPartFactory() {

            public EditPart createEditPart(EditPart context, Object model) {
                if (model instanceof Container) {
                    switch(((Container) model).getType()) {
                        case Container.TYPE_ROOT:
                            return new DocumentPart(model);
                        case Container.TYPE_IMPORT_DECLARATIONS:
                            return new ImportsPart(model);
                        case Container.TYPE_COMMENT:
                        case Container.TYPE_PARAGRAPH:
                            return new BlockTextPart(model);
                        case Container.TYPE_INLINE:
                            return new InlineTextPart(model);
                        default:
                            throw new RuntimeException("unknown model type");
                    }
                } else if (model instanceof TextRun) {
                    switch(((TextRun) model).getType()) {
                        case TextRun.TYPE_IMPORT:
                            return new ImportPart(model);
                        default:
                            return new TextFlowPart(model);
                    }
                }
                throw new RuntimeException("unexpected model object");
            }
        });
        getGraphicalViewer().setContents(doc);
    }
