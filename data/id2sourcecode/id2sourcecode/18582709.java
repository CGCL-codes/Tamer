    private void newCluster() {
        for (int k = 0; k < numClusters; k++) {
            if (k != besti && k != bestj) {
                int ak = alias[k];
                distance[ak][abi] = distance[abi][ak] = updatedDistance(besti, bestj, k);
            }
        }
        distance[abi][abi] = 0.0;
        height[abi] = getDist(besti, bestj) / 2.0;
        oc[abi] += oc[abj];
        NodeUtils.joinChilds(getRoot(), besti, bestj);
        for (int i = bestj; i < numClusters - 1; i++) {
            alias[i] = alias[i + 1];
        }
        numClusters--;
    }
