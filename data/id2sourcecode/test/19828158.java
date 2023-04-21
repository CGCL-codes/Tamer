    public void buildClassifier(Instances data) throws Exception {
        getCapabilities().testWithFail(data);
        data = new Instances(data);
        data.deleteWithMissingClass();
        Random random = data.getRandomNumberGenerator(m_Seed);
        if (!m_hashtablegiven) {
            m_classifiers = new Hashtable();
        }
        int[] indices = new int[data.numClasses()];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = i;
        }
        for (int i = indices.length - 1; i > 0; i--) {
            int help = indices[i];
            int index = random.nextInt(i + 1);
            indices[i] = indices[index];
            indices[index] = help;
        }
        m_ndtree = new NDTree();
        m_ndtree.insertClassIndexAtNode(indices[0]);
        for (int i = 1; i < indices.length; i++) {
            int nodeIndex = random.nextInt(2 * i - 1);
            NDTree node = m_ndtree.locateNode(nodeIndex, new int[1]);
            node.insertClassIndex(indices[i]);
        }
        m_ndtree.unifyTree();
        buildClassifierForNode(m_ndtree, data);
    }
