    public void resetRoot(EditPart c) {
        currentRootObject_ = (Container) ((LogicEditPart) c).getModel();
        getGraphicalViewer().setContents(c);
        c.refresh();
    }
