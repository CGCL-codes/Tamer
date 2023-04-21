    void polyBond(byte styleBond) {
        boolean bothColors = !sameColor;
        xAxis1 -= dxHalf1;
        yAxis1 -= dyHalf1;
        xAxis2 -= dxHalf2;
        yAxis2 -= dyHalf2;
        offsetAxis2 -= half2;
        calcMag2dLine();
        calcSurfaceIntersections();
        calcExitPoint();
        int xExitTop = xExit, yExitTop = yExit;
        int xMidTop = (xAxis1 + xAxis2) / 2, yMidTop = (yAxis1 + yAxis2) / 2;
        int xSurfaceTop = xSurface2, ySurfaceTop = ySurface2;
        if (distanceExit >= mag2dLine / 2) {
            bothColors = false;
            if (distanceExit + distanceSurface2 >= mag2dLine) return;
        }
        xAxis1 += dxWidth1;
        yAxis1 += dyWidth1;
        xAxis2 += dxWidth2;
        yAxis2 += dyWidth2;
        offsetAxis2 += width2;
        calcMag2dLine();
        calcSurfaceIntersections();
        calcExitPoint();
        int xExitBot = xExit, yExitBot = yExit;
        int xMidBot = (xAxis1 + xAxis2) / 2, yMidBot = (yAxis1 + yAxis2) / 2;
        int xSurfaceBot = xSurface2, ySurfaceBot = ySurface2;
        xAxis1 -= dxOtherHalf1;
        yAxis1 -= dyOtherHalf1;
        xAxis2 -= dxOtherHalf2;
        yAxis2 -= dyOtherHalf2;
        offsetAxis2 -= otherHalf2;
        if (distanceExit >= mag2dLine / 2) {
            bothColors = false;
            if (distanceExit + distanceSurface2 >= mag2dLine) return;
        }
        drawEndCaps();
        if (!bothColors) {
            if (distanceExit < mag2dLine) {
                axPoly[0] = xExitTop;
                ayPoly[0] = yExitTop;
                axPoly[1] = xSurfaceTop;
                ayPoly[1] = ySurfaceTop;
                axPoly[2] = xSurfaceBot;
                ayPoly[2] = ySurfaceBot;
                axPoly[3] = xExitBot;
                ayPoly[3] = yExitBot;
                polyBond1(styleBond, color2, outline2);
            }
        } else {
            axPoly[0] = xExitTop;
            ayPoly[0] = yExitTop;
            axPoly[1] = xMidTop;
            ayPoly[1] = yMidTop;
            axPoly[2] = xMidBot;
            ayPoly[2] = yMidBot;
            axPoly[3] = xExitBot;
            ayPoly[3] = yExitBot;
            polyBond1(styleBond, color1, outline1);
            axPoly[0] = xMidTop;
            ayPoly[0] = yMidTop;
            axPoly[1] = xSurfaceTop;
            ayPoly[1] = ySurfaceTop;
            axPoly[2] = xSurfaceBot;
            ayPoly[2] = ySurfaceBot;
            axPoly[3] = xMidBot;
            ayPoly[3] = yMidBot;
            polyBond1(styleBond, color2, outline2);
        }
    }
