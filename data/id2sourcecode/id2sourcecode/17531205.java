    protected void performNameDirectEdit() {
        logger.debug(this + " perform name direct edit.");
        if (directEditManagerName == null) {
            ValidationEnabledGraphicalViewer viewer = (ValidationEnabledGraphicalViewer) getViewer();
            ValidationMessageHandler handler = viewer.getValidationHandler();
            INamedFigure figure = (INamedFigure) getFigure();
            EditableLabel nameLabel = figure.getNameLabel();
            directEditManagerName = new ExtendedDirectEditManager(this, TextCellEditor.class, new LabelCellEditorLocator(nameLabel, false), nameLabel, getNameCellEditorValidator(handler), true);
        }
        directEditManagerName.show();
    }
