    private GeneralPath makeTriangle(double x1, double y1, double x2, double y2, int shape) {
        float xPixel = xToPixel(x1);
        float yPixelTop = yToPixel(y1);
        float yPixelBottom = yToPixel(y2);
        float height = Math.abs(yPixelTop - yPixelBottom);
        float xMid, yMid;
        switch(shape) {
            case RiemannSumRects.SQUARE_DIAG_FRONT:
                if (!inverse) {
                    xMid = xPixel - height / 2;
                } else {
                    xMid = xPixel + height / 2;
                }
                yMid = (yPixelBottom + yPixelTop) / 2;
                break;
            case RiemannSumRects.SQUARE_DIAG_REAR:
                if (!inverse) {
                    xMid = xPixel + height / 2;
                } else {
                    xMid = xPixel - height / 2;
                }
                yMid = (yPixelBottom + yPixelTop) / 2;
                break;
            case RiemannSumRects.EQUILATERAL:
                if (!inverse) {
                    xMid = (float) (xPixel - height * Math.sqrt(3) / 2);
                } else {
                    xMid = (float) (xPixel + height * Math.sqrt(3) / 2);
                }
                yMid = (yPixelBottom + yPixelTop) / 2;
                break;
            case RiemannSumRects.ISOSCELES_HYP:
                if (!inverse) {
                    xMid = xPixel - height / 2;
                } else {
                    xMid = xPixel + height / 2;
                }
                yMid = (yPixelBottom + yPixelTop) / 2;
                break;
            case RiemannSumRects.ISOSCELES_LEG:
                if (!inverse) {
                    xMid = xPixel - height;
                } else {
                    xMid = xPixel + height;
                }
                yMid = yPixelBottom;
                break;
            default:
                return null;
        }
        float width = Math.abs(xPixel - xMid);
        GeneralPath gp = new GeneralPath();
        if (!inverse) {
            gp.moveTo(xPixel, yPixelTop);
            gp.lineTo(xPixel, yPixelBottom);
            if (shape != RiemannSumRects.SQUARE_DIAG_REAR) {
                gp.lineTo(xMid + (1 - aspect) * width, yMid - height * slant);
            } else {
                gp.lineTo(xMid - (1 - aspect) * width, yMid + height * slant);
            }
            gp.closePath();
        } else {
            gp.moveTo(yPixelTop, xPixel);
            gp.lineTo(yPixelBottom, xPixel);
            if (shape != RiemannSumRects.SQUARE_DIAG_REAR) {
                gp.lineTo(yMid - height * slant, xMid - (1 - aspect) * width);
            } else {
                gp.lineTo(yMid + height * slant, xMid + (1 - aspect) * width);
            }
            gp.closePath();
        }
        return gp;
    }
