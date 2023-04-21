    void renderDistance(boolean renderLabel) {
        tickInfo = measurement.getTickInfo();
        if (tickInfo != null) {
            drawLine(atomA.screenX, atomA.screenY, atomA.screenZ, atomB.screenX, atomB.screenY, atomB.screenZ, mad);
            if (tickInfo != null) drawTicks(atomA, atomB, mad, renderLabel);
            return;
        }
        int zA = atomA.screenZ - atomA.screenDiameter - 10;
        int zB = atomB.screenZ - atomB.screenDiameter - 10;
        int radius = drawLine(atomA.screenX, atomA.screenY, zA, atomB.screenX, atomB.screenY, zB, mad);
        if (!renderLabel) return;
        int z = (zA + zB) / 2;
        if (z < 1) z = 1;
        int x = (atomA.screenX + atomB.screenX) / 2;
        int y = (atomA.screenY + atomB.screenY) / 2;
        drawString(x, y, z, radius, doJustify && (x - atomA.screenX) * (y - atomA.screenY) > 0, false, false, (doJustify ? 0 : Integer.MAX_VALUE), measurement.getString());
    }
