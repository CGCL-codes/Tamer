    public void SetPoligonPoints() {
        int leftX = 17 + (instanceSerial - 1) * 188;
        int rightX = 92 + (instanceSerial - 1) * 188;
        int upY = 58 + ((startSerial - 1) * 30) + ((instanceSerial - 1) * 7);
        int downY = 56 + ((stopSerial) * 30) + ((instanceSerial - 1) * 7);
        if (side == LEFT) {
            halfX = leftX + (rightX - leftX) / 2;
        } else if (side == RIGHT) {
            halfX = rightX + 8 + (rightX - leftX) / 2;
        }
        halfY = upY + (downY - upY) / 2;
        upperArrow.addPoint(halfX + (38 * side), upY);
        upperArrow.addPoint(halfX - (2 * side), upY);
        upperArrow.addPoint(halfX - (2 * side), halfY - 9);
        upperArrow.addPoint(halfX - (7 * side), halfY - 10);
        upperArrow.addPoint(halfX, halfY - 5);
        upperArrow.addPoint(halfX + (7 * side), halfY - 10);
        upperArrow.addPoint(halfX + (2 * side), halfY - 9);
        upperArrow.addPoint(halfX + (2 * side), upY + 4);
        upperArrow.addPoint(halfX + (38 * side), upY + 4);
        downerArrow.addPoint(halfX + (38 * side), downY);
        downerArrow.addPoint(halfX - (2 * side), downY);
        downerArrow.addPoint(halfX - (2 * side), halfY + 9);
        downerArrow.addPoint(halfX - (7 * side), halfY + 10);
        downerArrow.addPoint(halfX, halfY + 5);
        downerArrow.addPoint(halfX + (7 * side), halfY + 10);
        downerArrow.addPoint(halfX + (2 * side), halfY + 9);
        downerArrow.addPoint(halfX + (2 * side), downY - 4);
        downerArrow.addPoint(halfX + (38 * side), downY - 4);
        externalRect.addPoint(halfX - (30 * side), halfY - 7);
        externalRect.addPoint(halfX - (30 * side), halfY + 7);
        externalRect.addPoint(halfX + (30 * side), halfY + 7);
        externalRect.addPoint(halfX + (30 * side), halfY - 7);
        internalRect.addPoint(halfX - (27 * side), halfY - 5);
        internalRect.addPoint(halfX - (27 * side), halfY + 5);
        internalRect.addPoint(halfX + (27 * side), halfY + 5);
        internalRect.addPoint(halfX + (27 * side), halfY - 5);
    }
