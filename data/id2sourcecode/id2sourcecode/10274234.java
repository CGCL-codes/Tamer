    private void inintCluster(TextBasedDataSet dataSet) throws Exception {
        dataSet.generatePKList();
        m_Iterations = 0;
        m_ClusterCentroids = new ArrayList<TextBasedData>(m_NumClusters);
        m_ClusterCentroidsOwner = new TextBasedDataSet();
        m_ClusterCentroidsOwner.setAttrMeta(dataSet.getAttrMeta());
        m_clusterAssignments = new int[dataSet.getTextNumber()];
        Random RandomO = new Random(getSeed());
        int instIndex;
        HashMap<String, Object> initC = new HashMap<String, Object>();
        String textKey = null;
        for (int j = dataSet.getTextNumber() - 1; j >= 0; j--) {
            instIndex = RandomO.nextInt(j + 1);
            textKey = dataSet.getKeyByPosition(instIndex);
            if (!initC.containsKey(textKey)) {
                m_ClusterCentroids.add(dataSet.getByListPosition(instIndex));
                initC.put(textKey, null);
            }
            try {
                dataSet.swap(j, instIndex);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (m_ClusterCentroids.size() == m_NumClusters) {
                break;
            }
        }
        m_NumClusters = m_ClusterCentroids.size();
    }
