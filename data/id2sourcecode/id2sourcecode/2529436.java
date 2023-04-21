    protected void newCluster() {
        newCluster = new SimpleNode();
        newCluster.setHeight(newNodeHeight());
        newCluster.addChild(clusters[abi]);
        newCluster.addChild(clusters[abj]);
        clusters[abi] = newCluster;
        clusters[abj] = null;
        for (int k = 0; k < numClusters; k++) {
            if (k != besti && k != bestj) {
                int ak = alias[k];
                distance[ak][abi] = distance[abi][ak] = updatedDistance(besti, bestj, k);
                distance[ak][abj] = distance[abj][ak] = -1.0;
            }
        }
        distance[abi][abi] = 0.0;
        distance[abj][abj] = -1.0;
        for (int i = bestj; i < numClusters - 1; i++) {
            alias[i] = alias[i + 1];
        }
        tipCount[abi] += tipCount[abj];
        tipCount[abj] = 0;
        numClusters--;
    }
