    @Override
    protected void createDefaultEditPolicies() {
        super.createDefaultEditPolicies();
        removeEditPolicy(org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles.CONNECTION_HANDLES_ROLE);
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new GraphicalNodeEditPolicy() {

            @Override
            protected Command getConnectionAndRelationshipCompleteCommand(CreateConnectionViewAndElementRequest request) {
                GraphicalViewer graphicalViewer = ((GraphicalViewer) (getHandlerEditPart()).getViewer());
                if (graphicalViewer.getEditDomain().getPaletteViewer() == null) {
                    SafiWorkshopEditorUtil.initializePalette();
                }
                return super.getConnectionAndRelationshipCompleteCommand(request);
            }

            @Override
            protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
                return super.getConnectionCreateCommand(request);
            }
        });
        installEditPolicy(EditPolicyRoles.OPEN_ROLE, new OpenEditorEditPolicy());
    }
