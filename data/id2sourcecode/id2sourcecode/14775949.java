    public AlgorithmData execute(AlgorithmData data) throws AlgorithmException {
        AlgorithmParameters map = data.getParams();
        function = map.getInt("distance-function", EUCLIDEAN);
        factor = map.getFloat("distance-factor", 1.0f);
        absolute = map.getBoolean("distance-absolute", false);
        calculateMeans = map.getBoolean("calculate-means", true);
        kmcGenes = map.getBoolean("kmc-cluster-genes", true);
        hcl_function = map.getInt("hcl-distance-function", EUCLIDEAN);
        hcl_absolute = map.getBoolean("hcl-distance-absolute", false);
        int number_of_iterations = map.getInt("number-of-iterations", 0);
        int number_of_clusters = map.getInt("number-of-clusters", 0);
        boolean hierarchical_tree = map.getBoolean("hierarchical-tree", false);
        int method_linkage = map.getInt("method-linkage", 0);
        boolean calculate_genes = map.getBoolean("calculate-genes", false);
        boolean calculate_experiments = map.getBoolean("calculate-experiments", false);
        this.expMatrix = data.getMatrix("experiment");
        number_of_genes = this.expMatrix.getRowDimension();
        number_of_samples = this.expMatrix.getColumnDimension();
        this.clusterConvergence = new int[number_of_clusters];
        KMCluster[] clusters;
        FloatMatrix means = null;
        FloatMatrix medians = null;
        FloatMatrix variances = null;
        if (calculateMeans) {
            clusters = calculate(number_of_genes, number_of_clusters, number_of_iterations);
            means = getMeans(clusters);
            variances = getVariances(clusters, means);
        } else {
            clusters = calculateMedians(number_of_genes, number_of_clusters, number_of_iterations);
            medians = getMedians(clusters);
            variances = getVariances(clusters, medians);
        }
        float[] tempConv = new float[clusterConvergence.length];
        for (int i = 0; i < clusterConvergence.length; i++) {
            tempConv[i] = (float) clusterConvergence[i];
        }
        QSort qsort = new QSort(tempConv);
        tempConv = qsort.getSorted();
        int[] sortedClusterIndices = qsort.getOrigIndx();
        int temp;
        for (int i = 0; i < number_of_clusters - 1; i++) {
            for (int j = 0; j < number_of_clusters - 1 - i; j++) {
                if (tempConv[j] == tempConv[j + 1]) {
                    if (clusters[sortedClusterIndices[j]].size() < clusters[sortedClusterIndices[j + 1]].size()) {
                        temp = sortedClusterIndices[j];
                        sortedClusterIndices[j] = sortedClusterIndices[j + 1];
                        sortedClusterIndices[j + 1] = temp;
                    }
                }
            }
        }
        KMCluster[] newClusterOrder = new KMCluster[clusters.length];
        FloatMatrix newMeansMedsOrder = new FloatMatrix(clusters.length, number_of_samples);
        FloatMatrix newVariancesOrder = new FloatMatrix(clusters.length, number_of_samples);
        for (int i = 0; i < clusters.length; i++) {
            newClusterOrder[i] = clusters[sortedClusterIndices[i]];
            newVariancesOrder.A[i] = variances.A[sortedClusterIndices[i]];
            if (calculateMeans) newMeansMedsOrder.A[i] = means.A[sortedClusterIndices[i]]; else newMeansMedsOrder.A[i] = medians.A[sortedClusterIndices[i]];
            clusterConvergence[i] = (int) (tempConv[i]);
        }
        clusters = newClusterOrder;
        variances = newVariancesOrder;
        if (calculateMeans) means = newMeansMedsOrder; else medians = newMeansMedsOrder;
        AlgorithmEvent event = null;
        if (hierarchical_tree) {
            event = new AlgorithmEvent(this, AlgorithmEvent.SET_UNITS, clusters.length, "Calculate Hierarchical Trees");
            fireValueChanged(event);
            event.setIntValue(0);
            event.setId(AlgorithmEvent.PROGRESS_VALUE);
            fireValueChanged(event);
        }
        Cluster result_cluster = new Cluster();
        NodeList nodeList = result_cluster.getNodeList();
        int[] features;
        for (int i = 0; i < clusters.length; i++) {
            if (stop) {
                throw new AbortException();
            }
            features = convert2int(clusters[i]);
            Node node = new Node(features);
            nodeList.addNode(node);
            if (hierarchical_tree) {
                node.setValues(calculateHierarchicalTree(features, method_linkage, calculate_genes, calculate_experiments));
                event.setIntValue(i + 1);
                fireValueChanged(event);
            }
        }
        AlgorithmData result = new AlgorithmData();
        result.addCluster("cluster", result_cluster);
        if (calculateMeans) result.addMatrix("clusters_means", means); else result.addMatrix("clusters_means", medians);
        result.addMatrix("clusters_variances", variances);
        result.addParam("iterations", String.valueOf(getIterations()));
        result.addParam("converged", String.valueOf(getConverged()));
        result.addIntArray("convergence-iterations", clusterConvergence);
        return result;
    }
