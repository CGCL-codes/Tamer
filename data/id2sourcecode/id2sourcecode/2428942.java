    public LearningSystemViewer(LearningSystem ls) throws ParseException {
        initComponents();
        int numParams = ls.getNumParams();
        final SimpleDataset d = ls.getDataset();
        labelNumParams.setText(ls.getNumParams() + " parameters:");
        if (d != null) {
            final int numFeats = d.getNumFeatures();
            labelNumFeats.setText(numFeats + " features:");
            AbstractListModel m = new AbstractListModel() {

                public int getSize() {
                    return numFeats;
                }

                public Object getElementAt(int i) {
                    return d.getFeatureName(i);
                }
            };
            listFeatures.setModel(m);
            int numInstances = d.getNumDatapoints();
            double dtime = d.getTimestamp(numInstances - 1);
            String ds = "" + (int) dtime;
            while (ds.length() < 9) {
                ds = "0" + ds;
            }
            Date date = SimpleDataset.dateFormat.parse(ds);
            SimpleDateFormat prettyDateFormat = new SimpleDateFormat("MM/dd/yyyy 'at' HH:mm:ss");
            labelSummary.setText("Dataset contains " + numInstances + " examples. Modified " + prettyDateFormat.format(date));
        } else {
            labelNumFeats.setText("No dataset, no features");
            listFeatures.removeAll();
            labelSummary.setText("");
        }
        panelParams.removeAll();
        LearningSystemParameterViewer[] vs = new LearningSystemParameterViewer[numParams];
        for (int i = 0; i < numParams; i++) {
            vs[i] = new LearningSystemParameterViewer(ls, i);
            panelParams.add(vs[i]);
        }
        panelParams.setPreferredSize(new Dimension(400, 263 * numParams));
        panelParams.repaint();
        this.repaint();
    }
