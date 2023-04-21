    void lineBond() {
        calcMag2dLine();
        calcSurfaceIntersections();
        calcExitPoint();
        if (sameColor || distanceExit >= mag2dLine / 2) {
            if (distanceExit + distanceSurface2 >= mag2dLine) return;
            g.setColor(color2);
            drawLineInside(g, xExit, yExit, xSurface2, ySurface2);
            return;
        }
        int xMid = (xAxis1 + xAxis2) / 2;
        int yMid = (yAxis1 + yAxis2) / 2;
        g.setColor(color1);
        drawLineInside(g, xExit, yExit, xMid, yMid);
        g.setColor(color2);
        drawLineInside(g, xMid, yMid, xSurface2, ySurface2);
    }
