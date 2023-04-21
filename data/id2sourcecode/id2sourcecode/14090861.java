    public void run() {
        try {
            ExperimentNode eNode = this.getXplorePanel().getSourceNode();
            Retina retina = (Retina) eNode.getMethod().getOutput(Retina.class);
            Retina grid = retina;
            IHeightMatrix heightMatrix = null;
            ((VisualizerFrame) this.getXplorePanel().getGraphicalViewer()).showWaitPanel("Reconstructing. Please Wait ...");
            if (getMatrixType().equals("U")) {
                heightMatrix = new vademecum.data.UMatrix(grid);
            } else {
                heightMatrix = new vademecum.data.PMatrix(grid, grid.getInputVectors());
                heightMatrix.calculateHeights();
            }
            log.debug("NumOfClusterCols: " + GridUtils.getClusterColumnPos(grid.getWeightVectors()));
            int clusterCol;
            if (GridUtils.getClusterColumnPos(grid.getWeightVectors()) != null) {
                clusterCol = GridUtils.getClusterColumnPos(grid.getWeightVectors()).size() - 1;
            } else {
                clusterCol = 0;
            }
            init(heightMatrix, grid.getBMList(), GridUtils.getClusterColumn(grid.getWeightVectors(), clusterCol), grid.getClusterSurface());
            ((VisualizerFrame) this.getXplorePanel().getGraphicalViewer()).hideWaitPanel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
