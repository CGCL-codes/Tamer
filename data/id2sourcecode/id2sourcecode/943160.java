    public void rotateImage(int direction) {
        if (zoomlevel == 1) {
            rotateImageInPlace(direction);
            return;
        }
        Image newImage;
        try {
            newImage = Image.createImage(h, w);
        } catch (OutOfMemoryError e) {
            newImage = null;
            midlet.alert("Out of Memory! [roti]");
            System.gc();
            return;
        }
        Graphics g = newImage.getGraphics();
        int transform;
        if (direction == ROTATE_CW) {
            transform = Sprite.TRANS_ROT90;
            for (int ychunk = 0; ychunk * 100 < h; ychunk++) {
                int destx = Math.max(0, h - ychunk * 100 - 100);
                for (int xchunk = 0; xchunk * 100 < w; xchunk++) {
                    int desty = xchunk * 100;
                    int regionh = Math.min(100, h - ychunk * 100);
                    int regionw = Math.min(100, w - xchunk * 100);
                    g.drawRegion(im, xchunk * 100, ychunk * 100, regionw, regionh, transform, destx, desty, Graphics.TOP | Graphics.LEFT);
                }
            }
            int x0 = -x;
            int y0 = -y;
            x = h - y0 - (dispHeight + dispWidth) / 2;
            y = x0 + (dispWidth - dispHeight) / 2;
            x = -x;
            y = -y;
            orientation = (orientation + 1) % 4;
        } else if (direction == ROTATE_CCW) {
            transform = Sprite.TRANS_ROT270;
            for (int ychunk = 0; ychunk * 100 < h; ychunk++) {
                int destx = ychunk * 100;
                for (int xchunk = 0; xchunk * 100 < w; xchunk++) {
                    int desty = Math.max(0, w - xchunk * 100 - 100);
                    int regionh = Math.min(100, h - ychunk * 100);
                    int regionw = Math.min(100, w - xchunk * 100);
                    g.drawRegion(im, xchunk * 100, ychunk * 100, regionw, regionh, transform, destx, desty, Graphics.TOP | Graphics.LEFT);
                }
            }
            int x0 = -x;
            int y0 = -y;
            y = w - x0 - (dispHeight + dispWidth) / 2;
            x = y0 + (dispHeight - dispWidth) / 2;
            x = -x;
            y = -y;
            orientation = (orientation - 1) % 4;
        } else {
            return;
        }
        im = newImage;
        w = im.getWidth();
        h = im.getHeight();
        newImage = null;
        System.gc();
        repaint();
    }
