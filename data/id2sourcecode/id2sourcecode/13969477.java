    private ImagePlus doProjections(ImagePlus imp) {
        int nSlices;
        int projwidth, projheight;
        int xcenter, ycenter, zcenter;
        int theta;
        double thetarad;
        int sintheta, costheta;
        int offset;
        int curval, prevval, nextval, aboveval, belowval;
        int n, nProjections, angle;
        boolean minProjSize = true;
        stack = imp.getStack();
        if ((angleInc == 0) && (totalAngle != 0)) angleInc = 5;
        boolean negInc = angleInc < 0;
        if (negInc) angleInc = -angleInc;
        angle = 0;
        nProjections = 0;
        if (angleInc == 0) nProjections = 1; else {
            while (angle <= totalAngle) {
                nProjections++;
                angle += angleInc;
            }
        }
        if (angle > 360) nProjections--;
        if (nProjections <= 0) nProjections = 1;
        if (negInc) angleInc = -angleInc;
        ImageProcessor ip = imp.getProcessor();
        Rectangle r = ip.getRoi();
        left = r.x;
        top = r.y;
        right = r.x + r.width;
        bottom = r.y + r.height;
        nSlices = imp.getStackSize();
        imageWidth = imp.getWidth();
        width = right - left;
        height = bottom - top;
        xcenter = (left + right) / 2;
        ycenter = (top + bottom) / 2;
        zcenter = (int) (nSlices * sliceInterval / 2.0 + 0.5);
        projwidth = 0;
        projheight = 0;
        if (minProjSize && axisOfRotation != zAxis) {
            switch(axisOfRotation) {
                case xAxis:
                    projheight = (int) (Math.sqrt(nSlices * sliceInterval * nSlices * sliceInterval + height * height) + 0.5);
                    projwidth = width;
                    break;
                case yAxis:
                    projwidth = (int) (Math.sqrt(nSlices * sliceInterval * nSlices * sliceInterval + width * width) + 0.5);
                    projheight = height;
                    break;
            }
        } else {
            projwidth = (int) (Math.sqrt(nSlices * sliceInterval * nSlices * sliceInterval + width * width) + 0.5);
            projheight = (int) (Math.sqrt(nSlices * sliceInterval * nSlices * sliceInterval + height * height) + 0.5);
        }
        if ((projwidth % 2) == 1) projwidth++;
        int projsize = projwidth * projheight;
        if (projwidth <= 0 || projheight <= 0) {
            IJ.error("'projwidth' or 'projheight' <= 0");
            return null;
        }
        try {
            allocateArrays(nProjections, projwidth, projheight);
        } catch (OutOfMemoryError e) {
            Object[] images = stack2.getImageArray();
            if (images != null) for (int i = 0; i < images.length; i++) images[i] = null;
            stack2 = null;
            IJ.error("Projector - Out of Memory", "To use less memory, use a rectanguar\n" + "selection,  reduce \"Total Rotation\",\n" + "and/or increase \"Angle Increment\".");
            return null;
        }
        ImagePlus projections = new ImagePlus("Projections of " + imp.getShortTitle(), stack2);
        projections.setCalibration(imp.getCalibration());
        IJ.resetEscape();
        theta = initAngle;
        IJ.resetEscape();
        for (n = 0; n < nProjections; n++) {
            IJ.showStatus(n + "/" + nProjections);
            showProgress((double) n / nProjections);
            thetarad = theta * Math.PI / 180.0;
            costheta = (int) (BIGPOWEROF2 * Math.cos(thetarad) + 0.5);
            sintheta = (int) (BIGPOWEROF2 * Math.sin(thetarad) + 0.5);
            projArray = (byte[]) stack2.getPixels(n + 1);
            if (projArray == null) break;
            if ((projectionMethod == nearestPoint) || (opacity > 0)) {
                for (int i = 0; i < projsize; i++) zBuffer[i] = (short) 32767;
            }
            if ((opacity > 0) && (projectionMethod != nearestPoint)) {
                for (int i = 0; i < projsize; i++) opaArray[i] = (byte) 0;
            }
            if ((projectionMethod == brightestPoint) && (depthCueInt < 100)) {
                for (int i = 0; i < projsize; i++) brightCueArray[i] = (byte) 0;
                for (int i = 0; i < projsize; i++) cueZBuffer[i] = (short) 0;
            }
            if (projectionMethod == meanValue) {
                for (int i = 0; i < projsize; i++) sumBuffer[i] = 0;
                for (int i = 0; i < projsize; i++) countBuffer[i] = (short) 0;
            }
            switch(axisOfRotation) {
                case xAxis:
                    doOneProjectionX(nSlices, ycenter, zcenter, projwidth, projheight, costheta, sintheta);
                    break;
                case yAxis:
                    doOneProjectionY(nSlices, xcenter, zcenter, projwidth, projheight, costheta, sintheta);
                    break;
                case zAxis:
                    doOneProjectionZ(nSlices, xcenter, ycenter, zcenter, projwidth, projheight, costheta, sintheta);
                    break;
            }
            if (projectionMethod == meanValue) {
                int count;
                for (int i = 0; i < projsize; i++) {
                    count = countBuffer[i];
                    if (count != 0) projArray[i] = (byte) (sumBuffer[i] / count);
                }
            }
            if ((opacity > 0) && (projectionMethod != nearestPoint)) {
                for (int i = 0; i < projsize; i++) projArray[i] = (byte) ((opacity * (opaArray[i] & 0xff) + (100 - opacity) * (projArray[i] & 0xff)) / 100);
            }
            if (axisOfRotation == zAxis) {
                for (int i = projwidth; i < (projsize - projwidth); i++) {
                    curval = projArray[i] & 0xff;
                    prevval = projArray[i - 1] & 0xff;
                    nextval = projArray[i + 1] & 0xff;
                    aboveval = projArray[i - projwidth] & 0xff;
                    belowval = projArray[i + projwidth] & 0xff;
                    if ((curval == 0) && (prevval != 0) && (nextval != 0) && (aboveval != 0) && (belowval != 0)) projArray[i] = (byte) ((prevval + nextval + aboveval + belowval) / 4);
                }
            }
            theta = (theta + angleInc) % 360;
            if (IJ.escapePressed()) {
                done = true;
                IJ.beep();
                IJ.showProgress(1.0);
                IJ.showStatus("aborted");
                break;
            }
            projections.setSlice(n + 1);
        }
        showProgress(1.0);
        if (debugMode) {
            if (projArray != null) new ImagePlus("projArray", new ByteProcessor(projwidth, projheight, projArray, null)).show();
            if (opaArray != null) new ImagePlus("opaArray", new ByteProcessor(projwidth, projheight, opaArray, null)).show();
            if (brightCueArray != null) new ImagePlus("brightCueArray", new ByteProcessor(projwidth, projheight, brightCueArray, null)).show();
            if (zBuffer != null) new ImagePlus("zBuffer", new ShortProcessor(projwidth, projheight, zBuffer, null)).show();
            if (cueZBuffer != null) new ImagePlus("cueZBuffer", new ShortProcessor(projwidth, projheight, cueZBuffer, null)).show();
            if (countBuffer != null) new ImagePlus("countBuffer", new ShortProcessor(projwidth, projheight, countBuffer, null)).show();
            if (sumBuffer != null) {
                float[] tmp = new float[projwidth * projheight];
                for (int i = 0; i < projwidth * projheight; i++) tmp[i] = sumBuffer[i];
                new ImagePlus("sumBuffer", new FloatProcessor(projwidth, projheight, tmp, null)).show();
            }
        }
        return projections;
    }
