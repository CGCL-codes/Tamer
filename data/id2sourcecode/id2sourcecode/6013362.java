    private MultiLine calculateDefaultLine() {
        java.util.ArrayList<Integer> xCoords = new ArrayList<Integer>();
        java.util.ArrayList<Integer> yCoords = new ArrayList<Integer>();
        int startX = from.getOutputPos(fromPinNumber - from.getNumInputs()).x;
        int startY = from.getOutputPos(fromPinNumber - from.getNumInputs()).y;
        int endX = to.getInputPos(toPinNumber).x;
        int endY = to.getInputPos(toPinNumber).y;
        if (startX >= endX) {
            int startTop = from.getYpos();
            int startBottom = startTop + from.getHeight();
            int endTop = to.getYpos();
            int endBottom = endTop + to.getHeight();
            int minPossY = Math.min(startTop, endTop) - 5;
            int maxPossY = Math.max(startBottom, endBottom) + 5;
            int optimalY;
            if (startBottom + 5 < endTop) {
                optimalY = (startBottom + endTop) / 2;
            } else if (endBottom + 5 < startTop) {
                optimalY = (endBottom + startTop) / 2;
            } else if (startY - minPossY + endY - minPossY < maxPossY - startY + maxPossY - endY) {
                optimalY = minPossY;
            } else {
                optimalY = maxPossY;
            }
            xCoords.add(startX);
            xCoords.add(startX + 10);
            xCoords.add(startX + 10);
            xCoords.add(endX - 10);
            xCoords.add(endX - 10);
            xCoords.add(endX);
            yCoords.add(startY);
            yCoords.add(startY);
            yCoords.add(optimalY);
            yCoords.add(optimalY);
            yCoords.add(endY);
            yCoords.add(endY);
        } else {
            int avgX = (startX + endX) / 2;
            xCoords.add(startX);
            xCoords.add(avgX);
            xCoords.add(avgX);
            xCoords.add(endX);
            yCoords.add(startY);
            yCoords.add(startY);
            yCoords.add(endY);
            yCoords.add(endY);
        }
        return new MultiLine(xCoords, yCoords);
    }
