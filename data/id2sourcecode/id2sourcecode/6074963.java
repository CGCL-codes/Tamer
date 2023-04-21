    @Override
    public void setProperties(Properties p) {
        super.setProperties(p);
        String prefix = "VGraphics" + this.getID() + "_";
        try {
            ExperimentNode eNode = this.getFigurePanel().getSourceNode();
            IDataNode dn = eNode.getMethod();
            Retina retina = (Retina) dn.getOutput(Retina.class);
            IDataGrid grid = retina.getInputVectors();
            vademecum.data.PMatrix matrix = new vademecum.data.PMatrix(retina, grid);
            matrix.calculateHeights();
            PMatrix2D plot = new PMatrix2D(matrix, retina);
            BufferedImage image = plot.getImage();
            this.setImage(image);
            Surface3DRotate rot3d = (Surface3DRotate) this.interactionList.get(1);
            rot3d.setJRenderer3D(this.jRenderer3D);
        } catch (Exception e) {
            ((VisualizerFrame) this.getFigurePanel().getGraphicalViewer()).hideWaitPanel();
            ((VisualizerFrame) this.getFigurePanel().getGraphicalViewer()).dispose();
            System.err.println("PMatrix 2D : Something went wrong. Please note the following error messages :");
            System.err.println(e);
            e.printStackTrace();
        }
        this.repaint();
    }
