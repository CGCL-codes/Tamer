    void renderDistance(boolean renderLabel) {
        int zA = nodeA.screenZ - nodeA.screenDiameter - 10;
        int zB = nodeB.screenZ - nodeB.screenDiameter - 10;
        int radius = drawSegment(nodeA.screenX, nodeA.screenY, zA, nodeB.screenX, nodeB.screenY, zB);
        if (!renderLabel) return;
        int z = (zA + zB) / 2;
        if (z < 1) z = 1;
        int x = (nodeA.screenX + nodeB.screenX) / 2;
        int y = (nodeA.screenY + nodeB.screenY) / 2;
        paintMeasurementString(x, y, z, radius, (x - nodeA.screenX) * (y - nodeA.screenY) > 0, 0);
    }
