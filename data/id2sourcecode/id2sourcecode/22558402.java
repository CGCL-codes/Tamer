    public void mouseRangeSelected(MouseDragEvent e0) {
        MouseBoxEvent e = (MouseBoxEvent) e0;
        DatumRange xrange;
        DatumRange yrange;
        xrange = new DatumRange(xAxis.invTransform(e.getXMinimum()), xAxis.invTransform(e.getXMaximum()));
        yrange = new DatumRange(yAxis.invTransform(e.getYMaximum()), yAxis.invTransform(e.getYMinimum()));
        QDataSet ds = dsConsumer.getConsumedDataSet();
        if (ds == null) {
            UserMessageCenter.getDefault().notifyUser(this, "This renderer doesn't have a dataset loaded");
            return;
        }
        QDataSet outds;
        if (SemanticOps.isTableDataSet(ds)) {
            QDataSet tds = (QDataSet) ds;
            outds = new ClippedTableDataSet(tds, xrange, yrange);
        } else {
            QDataSet vds = (QDataSet) ds;
            DataSetBuilder builder = new DataSetBuilder(2, 100, 2);
            QDataSet xds = SemanticOps.xtagsDataSet(vds);
            for (int i = 0; i < vds.length(); i++) {
                if (yrange.contains(SemanticOps.getDatum(vds, vds.value(i))) & xrange.contains(SemanticOps.getDatum(xds, xds.value(i)))) {
                    builder.putValue(-1, 0, xds.value(i));
                    builder.putValue(-1, 1, vds.value(i));
                }
            }
            outds = builder.getDataSet();
        }
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {

            public boolean accept(File f) {
                if (f.toString() == null) return false;
                return f.toString().matches(".*\\.das2Stream");
            }

            public String getDescription() {
                return "*.das2Stream";
            }
        });
        int result = chooser.showSaveDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selected = chooser.getSelectedFile();
            try {
                FileChannel out = new FileOutputStream(selected).getChannel();
                DataSet outds2 = DataSetAdapter.createLegacyDataSet(outds);
                if (outds2 instanceof TableDataSet) {
                    TableUtil.dumpToAsciiStream((TableDataSet) outds2, out);
                } else if (outds instanceof VectorDataSet) {
                    VectorUtil.dumpToAsciiStream((VectorDataSet) outds2, out);
                }
            } catch (IOException ioe) {
                DasExceptionHandler.handle(ioe);
            }
        }
    }
