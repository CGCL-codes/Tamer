        public void actionPerformed(ActionEvent e) {
            List<Renderer> renderers1 = Arrays.asList(getRenderers());
            if (renderers1.isEmpty()) {
                return;
            }
            Renderer renderer = (Renderer) renderers1.get(0);
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showSaveDialog(DasPlot.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selected = chooser.getSelectedFile();
                try {
                    FileChannel out = new FileOutputStream(selected).getChannel();
                    DataSet ds = DataSetAdapter.createLegacyDataSet(renderer.getDataSet());
                    if (ds instanceof TableDataSet) {
                        TableUtil.dumpToAsciiStream((TableDataSet) ds, out);
                    } else if (ds instanceof VectorDataSet) {
                        VectorUtil.dumpToAsciiStream((VectorDataSet) ds, out);
                    }
                } catch (IOException ioe) {
                    DasExceptionHandler.handle(ioe);
                }
            }
        }
