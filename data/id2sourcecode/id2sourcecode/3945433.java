    public Chaine getChaine() {
        EditPart part = getGraphicalViewer().getContents();
        if (part instanceof ChainePart) {
            if (((ChainePart) part).getModel() instanceof Chaine) {
                return (Chaine) ((ChainePart) part).getModel();
            }
        }
        return null;
    }
