    public void run() {
        try {
            ExperimentNode eNode = this.getFigurePanel().getSourceNode();
            Retina retina = (Retina) eNode.getMethod().getOutput(Retina.class);
            IDataGrid grid = retina.getInputVectors();
            ((VisualizerFrame) this.getFigurePanel().getGraphicalViewer()).showWaitPanel("Preparing P-Matrix. Please Wait ...");
            vademecum.data.PMatrix matrix = new vademecum.data.PMatrix(retina, grid);
            if (pdens != null) {
                pdens.setDataGrid(grid);
                pdens.setCenters(GridUtils.retinaToGrid(retina));
                matrix.setParetoDensity(pdens);
            }
            ((VisualizerFrame) this.getFigurePanel().getGraphicalViewer()).textFlushToWaitPanel("Calculating P-Matrix heights ...");
            matrix.calculateHeights();
            init(matrix, retina);
            ((VisualizerFrame) this.getFigurePanel().getGraphicalViewer()).hideWaitPanel();
            ((VisualizerFrame) this.getFigurePanel().getGraphicalViewer()).initToolbox();
        } catch (Exception e) {
            ((VisualizerFrame) this.getFigurePanel().getGraphicalViewer()).hideWaitPanel();
            ((VisualizerFrame) this.getFigurePanel().getGraphicalViewer()).dispose();
            System.err.println("PMatrix 2D : Something went wrong. Please note the following error messages :");
            System.err.println(e);
            e.printStackTrace();
        }
    }
