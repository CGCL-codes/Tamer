    public void buildClusterer(Instances data) throws Exception {
        getCapabilities().testWithFail(data);
        m_Iterations = 0;
        m_ReplaceMissingFilter = new ReplaceMissingValues();
        Instances instances = new Instances(data);
        instances.setClassIndex(-1);
        m_ReplaceMissingFilter.setInputFormat(instances);
        instances = Filter.useFilter(instances, m_ReplaceMissingFilter);
        m_Min = new double[instances.numAttributes()];
        m_Max = new double[instances.numAttributes()];
        for (int i = 0; i < instances.numAttributes(); i++) {
            m_Min[i] = m_Max[i] = Double.NaN;
        }
        m_ClusterCentroids = new Instances(instances, m_NumClusters);
        int[] clusterAssignments = new int[instances.numInstances()];
        for (int i = 0; i < instances.numInstances(); i++) {
            updateMinMax(instances.instance(i));
        }
        Random RandomO = new Random(getSeed());
        int instIndex;
        HashMap initC = new HashMap();
        DecisionTable.hashKey hk = null;
        for (int j = instances.numInstances() - 1; j >= 0; j--) {
            instIndex = RandomO.nextInt(j + 1);
            hk = new DecisionTable.hashKey(instances.instance(instIndex), instances.numAttributes(), true);
            if (!initC.containsKey(hk)) {
                m_ClusterCentroids.add(instances.instance(instIndex));
                initC.put(hk, null);
            }
            instances.swap(j, instIndex);
            if (m_ClusterCentroids.numInstances() == m_NumClusters) {
                break;
            }
        }
        m_NumClusters = m_ClusterCentroids.numInstances();
        int i;
        boolean converged = false;
        int emptyClusterCount;
        Instances[] tempI = new Instances[m_NumClusters];
        m_squaredErrors = new double[m_NumClusters];
        m_ClusterNominalCounts = new int[m_NumClusters][instances.numAttributes()][0];
        while (!converged) {
            emptyClusterCount = 0;
            m_Iterations++;
            converged = true;
            for (i = 0; i < instances.numInstances(); i++) {
                Instance toCluster = instances.instance(i);
                int newC = clusterProcessedInstance(toCluster, true);
                if (newC != clusterAssignments[i]) {
                    converged = false;
                }
                clusterAssignments[i] = newC;
            }
            m_ClusterCentroids = new Instances(instances, m_NumClusters);
            for (i = 0; i < m_NumClusters; i++) {
                tempI[i] = new Instances(instances, 0);
            }
            for (i = 0; i < instances.numInstances(); i++) {
                tempI[clusterAssignments[i]].add(instances.instance(i));
            }
            for (i = 0; i < m_NumClusters; i++) {
                double[] vals = new double[instances.numAttributes()];
                if (tempI[i].numInstances() == 0) {
                    emptyClusterCount++;
                } else {
                    for (int j = 0; j < instances.numAttributes(); j++) {
                        vals[j] = tempI[i].meanOrMode(j);
                        m_ClusterNominalCounts[i][j] = tempI[i].attributeStats(j).nominalCounts;
                    }
                    m_ClusterCentroids.add(new Instance(1.0, vals));
                }
            }
            if (emptyClusterCount > 0) {
                m_NumClusters -= emptyClusterCount;
                tempI = new Instances[m_NumClusters];
            }
            if (!converged) {
                m_squaredErrors = new double[m_NumClusters];
                m_ClusterNominalCounts = new int[m_NumClusters][instances.numAttributes()][0];
            }
        }
        m_ClusterStdDevs = new Instances(instances, m_NumClusters);
        m_ClusterSizes = new int[m_NumClusters];
        for (i = 0; i < m_NumClusters; i++) {
            double[] vals2 = new double[instances.numAttributes()];
            for (int j = 0; j < instances.numAttributes(); j++) {
                if (instances.attribute(j).isNumeric()) {
                    vals2[j] = Math.sqrt(tempI[i].variance(j));
                } else {
                    vals2[j] = Instance.missingValue();
                }
            }
            m_ClusterStdDevs.add(new Instance(1.0, vals2));
            m_ClusterSizes[i] = tempI[i].numInstances();
        }
    }
