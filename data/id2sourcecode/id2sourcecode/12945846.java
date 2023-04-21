            @Override
            protected Command getConnectionAndRelationshipCompleteCommand(CreateConnectionViewAndElementRequest request) {
                GraphicalViewer graphicalViewer = ((GraphicalViewer) (getHandlerEditPart()).getViewer());
                if (graphicalViewer.getEditDomain().getPaletteViewer() == null) {
                    SafiWorkshopEditorUtil.initializePalette();
                }
                return super.getConnectionAndRelationshipCompleteCommand(request);
            }
