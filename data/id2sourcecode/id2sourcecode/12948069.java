    private int paintRecursively(HierarchicalClusterNode node, double baseDistance, Graphics g) {
        int leftPos = -1;
        int rightPos = -1;
        for (HierarchicalClusterNode subNode : node.getSubNodes()) {
            if ((subNode.getNumberOfSubNodes() > 0) || (subNode.getNumberOfExamplesInSubtree() > 1)) {
                int currentPos = paintRecursively(subNode, node.getDistance(), g);
                if (leftPos == -1) leftPos = currentPos;
                rightPos = currentPos;
            }
        }
        for (HierarchicalClusterNode subNode : node.getSubNodes()) {
            if ((subNode.getNumberOfExamplesInSubtree() == 1) && (subNode.getNumberOfSubNodes() == 0)) {
                int currentPos = countToXPos(count);
                drawLine(currentPos, weightToYPos(node.getDistance()), currentPos, weightToYPos(minDistance), g);
                if (leftPos == -1) leftPos = currentPos;
                rightPos = currentPos;
                count++;
            }
        }
        int middlePos = (rightPos + leftPos) / 2;
        drawLine(middlePos, weightToYPos(baseDistance), middlePos, weightToYPos(node.getDistance()), g);
        drawLine(leftPos, weightToYPos(node.getDistance()), rightPos, weightToYPos(node.getDistance()), g);
        return middlePos;
    }
