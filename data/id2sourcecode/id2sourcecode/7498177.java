    public boolean hasFinished() {
        if (bm_index >= bmList.size()) {
            log.debug("P Outlier finished.");
            for (Enumeration e = ht.keys(); e.hasMoreElements(); ) {
                IBestMatch object_key = (IBestMatch) e.nextElement();
                Double double_object = (Double) ht.get(object_key);
                Sortable s = new Sortable(double_object, object_key);
                sortedArray.add(s);
            }
            Collections.sort(sortedArray);
            if (autoSeek == true) {
                double[] vals = new double[sortedArray.size()];
                double[] diffvals = new double[sortedArray.size()];
                for (int i = 0; i < sortedArray.size(); i++) {
                    Sortable s = (Sortable) sortedArray.elementAt(i);
                    Double integer_object = (Double) s.getKey();
                    vals[i] = integer_object;
                }
                double cn = 0d;
                double[] conc = new double[vals.length];
                for (int i = 0; i < vals.length; i++) {
                    if (i < vals.length - 1) {
                        diffvals[i] = vals[i + 1] - vals[i];
                    } else {
                        diffvals[i] = diffvals[i - 1];
                    }
                    cn += diffvals[i];
                    conc[i] = diffvals[i] * vals[i];
                }
                double vmean = Univariate.getMean(vals);
                for (int i = 0; i < conc.length; i++) {
                    conc[i] = conc[i] / (cn * vmean);
                }
                data_out = GridUtils.addArray(this.data_in, vals, "Densities");
                data_out = GridUtils.addArray(this.data_out, diffvals, "DiffDensities");
                data_out = GridUtils.addArray(this.data_out, conc, "ConcDensities");
                int markIndex = 0;
                double ascendingConcentration = 0d;
                for (int i = 0; i < conc.length; i++) {
                    if (conc[i] == 0d) {
                    } else {
                        if (conc[i] >= ascendingConcentration) {
                            ascendingConcentration = conc[i];
                        } else {
                            markIndex = i;
                            break;
                        }
                    }
                }
                System.out.println("Ascended to Index : " + markIndex);
                System.out.println("Please Check Indices:");
                for (int i = 0; i < markIndex; i++) {
                    Sortable s = (Sortable) sortedArray.elementAt(i);
                    IBestMatch object_key = (IBestMatch) s.getObject();
                    int outl = object_key.getIndex();
                    outlierIndices.add(outl);
                }
            }
            if (this.seekMinKValues) {
                outlierIndices.clear();
                for (int i = 0; i < this.minK; i++) {
                    Sortable s = (Sortable) sortedArray.elementAt(i);
                    IBestMatch object_key = (IBestMatch) s.getObject();
                    int outl = object_key.getIndex();
                    System.out.println("min k=" + i + " : " + outl);
                    outlierIndices.add(outl);
                }
            }
            log.info("Number of possible Outliers : " + outlierIndices.size());
            this.numberOfOutliers = outlierIndices.size();
            for (int indy : outlierIndices) {
                log.debug(indy);
            }
            if (outlierIndices.size() > 0) {
                if (newNode == null) {
                    DataNavigation nav = ((Expertice) Core.projectPanel.getSelectedComponent()).getDataNavigation();
                    newNode = ExtensionFactory.createDataNode("vademecum.clusterer.simpleClusterer@simpleClusterer");
                    ExperimentNode actNode = (ExperimentNode) nav.getLastSelectedPathComponent();
                    ExperimentNode parentNode = (ExperimentNode) actNode.getParent();
                    nav.addNode(parentNode, newNode);
                }
                int[] clusterinfo = new int[data_out.getNumRows()];
                for (int i = 0; i < this.outlierIndices.size(); i++) {
                    clusterinfo[outlierIndices.get(i)] = this.tagValue;
                }
                String clusterstr = "";
                for (Integer i : clusterinfo) clusterstr += "," + String.valueOf(i);
                newNode.getMethod().setProperty("clustering", clusterstr);
                newNode.setState(ExperimentNode.READY);
                newNode.getMethod().init();
            }
            return true;
        }
        return false;
    }
