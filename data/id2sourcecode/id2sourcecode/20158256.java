    private void setRGB(RGB rgb) {
        this.rgb = rgb;
        EditPart editPart = ((ERDiagramEditor) this.getWorkbenchPart()).getGraphicalViewer().getContents();
        ERDiagram diagram = (ERDiagram) editPart.getModel();
        diagram.setDefaultColor(this.rgb.red, this.rgb.green, this.rgb.blue);
        this.setColorToImage();
    }
