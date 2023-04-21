    @Override
    public void setProperties(Properties p) {
        super.setProperties(p);
        String prefix = "VGraphics" + this.getID() + "_";
        int numDim = Integer.parseInt(p.getProperty(prefix + "numDim"));
        for (int i = 0; i < numDim; i++) {
            int var = Integer.parseInt(p.getProperty(prefix + "gridDim_" + Integer.toString(i)));
            try {
                ExperimentNode eNode = this.getFigurePanel().getSourceNode();
                IDataNode dn = eNode.getMethod();
                IDataGrid dataGrid = (IDataGrid) dn.getOutput(IDataGrid.class);
                this.setDataGrid(dataGrid, var);
            } catch (Exception e) {
                ((VisualizerFrame) this.getFigurePanel().getGraphicalViewer()).hideWaitPanel();
                ((VisualizerFrame) this.getFigurePanel().getGraphicalViewer()).dispose();
                System.err.println("PDEplot : Something went wrong. Please note the following error messages :");
                System.err.println(e);
                e.printStackTrace();
            }
        }
    }
