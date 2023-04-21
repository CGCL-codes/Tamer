    private void calculate() {
        float px = -1;
        float py = -1;
        float ox = -1;
        float oy = -1;
        switch(chooseCorner) {
            case LEFT_UP_CORNER:
                {
                    px = x;
                    py = y;
                    ox = x;
                    oy = y + height;
                    bY = y;
                    cX = x;
                    cY = y + height;
                    break;
                }
            case LEFT_BOTTOM_CORNER:
                {
                    px = x;
                    py = y + height;
                    ox = x;
                    oy = y;
                    bY = y + height;
                    cX = x;
                    cY = y;
                    break;
                }
            case RIGHT_UP_CORNER:
                {
                    px = width + x;
                    py = y;
                    ox = width + x;
                    oy = height + y;
                    bY = y;
                    cX = x + width;
                    cY = y + height;
                    break;
                }
            case RIGHT_BOTTOM_CORNER:
                {
                    px = width + x;
                    py = height + y;
                    ox = width + x;
                    oy = y;
                    bY = y + height;
                    cX = x + width;
                    cY = y;
                    break;
                }
        }
        if (aY == py) {
            bY = aY;
            bX = (aX + px) / 2;
            dY = oy;
            dX = aX;
            cX = (aX + ox) / 2;
            cY = oy;
            angle = 0;
            return;
        }
        MNLine lineOne = MNLine.initLine(aX, aY, px, py);
        MNLine lineTwo = lineOne.getPBLine((aX + px) / 2, (aY + py) / 2);
        bX = lineTwo.getXbyY(bY);
        float tempCY = lineTwo.getYbyX(cX);
        if (tempCY >= y && tempCY <= y + height) {
            cY = tempCY;
            dX = cX;
            dY = cY;
        } else {
            cX = lineTwo.getXbyY(cY);
            lineOne.change(ox, oy);
            float[] xAndY = lineOne.getCross(lineTwo);
            dX = 2 * xAndY[0] - ox;
            dY = 2 * xAndY[1] - oy;
        }
        if (((Float) bX).isNaN() || ((Float) bY).isNaN() || ((Float) cX).isNaN() || ((Float) cY).isNaN() || ((Float) dX).isNaN() || ((Float) dY).isNaN()) {
            clearPosition();
        } else {
            MNLine lineThree = MNLine.initLine(aX, aY, dX, dY);
            angle = (float) (Math.atan(lineThree.getA()) * 180 / Math.PI);
            if (angle > 0) {
                if (aX <= dX && aY <= dY && chooseCorner == RIGHT_BOTTOM_CORNER) {
                    angle = 90 + angle;
                } else if (aX >= dX && aY >= dY && chooseCorner == LEFT_UP_CORNER) {
                    angle = 90 + angle;
                } else {
                    angle = angle - 90;
                }
            } else {
                if (aX <= dX && aY >= dY && chooseCorner == RIGHT_UP_CORNER) {
                    angle = -90 + angle;
                } else if (aX >= dX && aY <= dY && chooseCorner == LEFT_BOTTOM_CORNER) {
                    angle = -90 + angle;
                } else {
                    angle = angle + 90;
                }
            }
            shadowAngle = (float) (Math.atan(lineTwo.getA()) * 180 / Math.PI);
            if (shadowAngle > 0) {
                shadowAngle = shadowAngle - 90;
            } else {
                shadowAngle = shadowAngle + 90;
            }
        }
    }
