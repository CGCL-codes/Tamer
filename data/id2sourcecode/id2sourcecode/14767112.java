    private SMDOutlinePage getSMDOutlinePage() {
        if (null == smdOutlinePage && null != getGraphicalViewer()) {
            logger.debug("Instancie la vue outline de l'�diteur.");
            RootEditPart rootEditPart = getGraphicalViewer().getRootEditPart();
            if (rootEditPart instanceof ScalableFreeformRootEditPart) {
                smdOutlinePage = new SMDOutlinePage((ScalableFreeformRootEditPart) rootEditPart);
            }
        }
        return smdOutlinePage;
    }
