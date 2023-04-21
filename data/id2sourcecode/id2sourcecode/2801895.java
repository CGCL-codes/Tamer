    public void buildClusterer(Instances data) throws Exception {
        getCapabilities().testWithFail(data);
        m_Iterations = 0;
        m_ReplaceMissingFilter = new ReplaceMissingValues();
        Instances instances = new Instances(data);
        instances.setClassIndex(-1);
        if (!m_dontReplaceMissing) {
            m_ReplaceMissingFilter.setInputFormat(instances);
            instances = Filter.useFilter(instances, m_ReplaceMissingFilter);
        }
        m_FullMissingCounts = new int[instances.numAttributes()];
        if (m_displayStdDevs) {
            m_FullStdDevs = new double[instances.numAttributes()];
        }
        m_FullNominalCounts = new int[instances.numAttributes()][0];
        m_FullMeansOrMediansOrModes = moveCentroid(0, instances, false);
        for (int i = 0; i < instances.numAttributes(); i++) {
            m_FullMissingCounts[i] = instances.attributeStats(i).missingCount;
            if (instances.attribute(i).isNumeric()) {
                if (m_displayStdDevs) {
                    m_FullStdDevs[i] = Math.sqrt(instances.variance(i));
                }
                if (m_FullMissingCounts[i] == instances.numInstances()) {
                    m_FullMeansOrMediansOrModes[i] = Double.NaN;
                }
            } else {
                m_FullNominalCounts[i] = instances.attributeStats(i).nominalCounts;
                if (m_FullMissingCounts[i] > m_FullNominalCounts[i][Utils.maxIndex(m_FullNominalCounts[i])]) {
                    m_FullMeansOrMediansOrModes[i] = -1;
                }
            }
        }
        m_ClusterCentroids = new Instances(instances, m_NumClusters);
        int[] clusterAssignments = new int[instances.numInstances()];
        if (m_PreserveOrder) m_Assignments = clusterAssignments;
        m_DistanceFunction.setInstances(instances);
        Random RandomO = new Random(getSeed());
        int instIndex;
        HashMap initC = new HashMap();
        DecisionTableHashKey hk = null;
        Instances initInstances = null;
        if (m_PreserveOrder) initInstances = new Instances(instances); else initInstances = instances;
        if (m_initializeWithKMeansPlusPlus) {
            kMeansPlusPlusInit(initInstances);
        } else {
            for (int j = initInstances.numInstances() - 1; j >= 0; j--) {
                instIndex = RandomO.nextInt(j + 1);
                hk = new DecisionTableHashKey(initInstances.instance(instIndex), initInstances.numAttributes(), true);
                if (!initC.containsKey(hk)) {
                    m_ClusterCentroids.add(initInstances.instance(instIndex));
                    initC.put(hk, null);
                }
                initInstances.swap(j, instIndex);
                if (m_ClusterCentroids.numInstances() == m_NumClusters) {
                    break;
                }
            }
        }
        m_NumClusters = m_ClusterCentroids.numInstances();
        initInstances = null;
        int i;
        boolean converged = false;
        int emptyClusterCount;
        Instances[] tempI = new Instances[m_NumClusters];
        m_squaredErrors = new double[m_NumClusters];
        m_ClusterNominalCounts = new int[m_NumClusters][instances.numAttributes()][0];
        m_ClusterMissingCounts = new int[m_NumClusters][instances.numAttributes()];
        while (!converged) {
            emptyClusterCount = 0;
            m_Iterations++;
            converged = true;
            for (i = 0; i < instances.numInstances(); i++) {
                Instance toCluster = instances.instance(i);
                int newC = clusterProcessedInstance(toCluster, false, true);
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
                if (tempI[i].numInstances() == 0) {
                    emptyClusterCount++;
                } else {
                    moveCentroid(i, tempI[i], true);
                }
            }
            if (emptyClusterCount > 0) {
                m_NumClusters -= emptyClusterCount;
                if (converged) {
                    Instances[] t = new Instances[m_NumClusters];
                    int index = 0;
                    for (int k = 0; k < tempI.length; k++) {
                        if (tempI[k].numInstances() > 0) {
                            t[index++] = tempI[k];
                        }
                    }
                    tempI = t;
                } else {
                    tempI = new Instances[m_NumClusters];
                }
            }
            if (m_Iterations == m_MaxIterations) converged = true;
            if (!converged) {
                m_ClusterNominalCounts = new int[m_NumClusters][instances.numAttributes()][0];
            }
        }
        if (!m_FastDistanceCalc) {
            for (i = 0; i < instances.numInstances(); i++) {
                clusterProcessedInstance(instances.instance(i), true, false);
            }
        }
        if (m_displayStdDevs) {
            m_ClusterStdDevs = new Instances(instances, m_NumClusters);
        }
        m_ClusterSizes = new int[m_NumClusters];
        for (i = 0; i < m_NumClusters; i++) {
            if (m_displayStdDevs) {
                double[] vals2 = new double[instances.numAttributes()];
                for (int j = 0; j < instances.numAttributes(); j++) {
                    if (instances.attribute(j).isNumeric()) {
                        vals2[j] = Math.sqrt(tempI[i].variance(j));
                    } else {
                        vals2[j] = Utils.missingValue();
                    }
                }
                m_ClusterStdDevs.add(new DenseInstance(1.0, vals2));
            }
            m_ClusterSizes[i] = tempI[i].numInstances();
        }
    }
