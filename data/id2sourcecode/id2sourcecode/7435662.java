        public void removeNode(Node oldNode, double[] oldPos, int depth) {
            if (oldNode.weight == 0.0) return;
            if (weight <= oldNode.weight) {
                weight = 0.0;
                node = null;
                for (int i = 0; i < children.length; i++) children[i] = null;
                childCount = 0;
                return;
            }
            for (int d = 0; d < 3; d++) {
                position[d] = (weight * position[d] - oldNode.weight * oldPos[d]) / (weight - oldNode.weight);
            }
            weight -= oldNode.weight;
            if (depth == MAX_DEPTH) {
                int childIndex = 0;
                while (children[childIndex].node != oldNode) childIndex++;
                childCount--;
                for (int i = childIndex; i < childCount; i++) {
                    children[i] = children[i + 1];
                }
                children[childCount] = null;
            } else {
                int childIndex = 0;
                for (int d = 0; d < 3; d++) {
                    if (oldPos[d] > (minPos[d] + maxPos[d]) / 2) {
                        childIndex += 1 << d;
                    }
                }
                children[childIndex].removeNode(oldNode, oldPos, depth + 1);
                if (children[childIndex].weight == 0.0) {
                    children[childIndex] = null;
                    childCount--;
                }
            }
        }
