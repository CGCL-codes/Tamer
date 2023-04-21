    protected void initializeGraphicalViewerContents() {
        super.initializeGraphicalViewerContents();
        List children = getDiagramEditPart().getChildren();
        for (Object o : children) {
            if (o instanceof ActorEditPart) {
                IGraphicalEditPart actorCompartment = ((ActorEditPart) o).getChildBySemanticHint(Integer.toString(ActorActorCompartmentEditPart.VISUAL_ID));
                ((ActorActorCompartmentEditPart) actorCompartment).forceRedirect();
            } else if (o instanceof AgentEditPart) {
                IGraphicalEditPart agentCompartment = ((AgentEditPart) o).getChildBySemanticHint(Integer.toString(AgentAgentCompartmentEditPart.VISUAL_ID));
                ((AgentAgentCompartmentEditPart) agentCompartment).forceRedirect();
            } else if (o instanceof RoleEditPart) {
                IGraphicalEditPart roleCompartment = ((RoleEditPart) o).getChildBySemanticHint(Integer.toString(RoleRoleCompartmentEditPart.VISUAL_ID));
                ((RoleRoleCompartmentEditPart) roleCompartment).forceRedirect();
            } else if (o instanceof PositionEditPart) {
                IGraphicalEditPart posCompartment = ((PositionEditPart) o).getChildBySemanticHint(Integer.toString(PositionPositionCompartmentEditPart.VISUAL_ID));
                ((PositionPositionCompartmentEditPart) posCompartment).forceRedirect();
            }
        }
    }
