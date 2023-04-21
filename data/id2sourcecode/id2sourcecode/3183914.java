    public void run() {
        try {
            ExperimentNode eNode = this.getFigurePanel().getSourceNode();
            Retina retina = (Retina) eNode.getMethod().getOutput(Retina.class);
            IDataGrid grid = retina.getWeightVectors();
            ((VisualizerFrame) this.getFigurePanel().getGraphicalViewer()).showWaitPanel("Preparing P-Matrix. Please Wait ...");
            vademecum.data.PMatrix matrix = new vademecum.data.PMatrix(retina, grid);
            ((VisualizerFrame) this.getFigurePanel().getGraphicalViewer()).textFlushToWaitPanel("Calculating P-Matrix heights ...");
            matrix.calculateHeights();
            init(matrix, retina);
            ((VisualizerFrame) this.getFigurePanel().getGraphicalViewer()).hideWaitPanel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
