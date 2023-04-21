    public BufferedImage createScreenCapture(Rectangle screenRect) {
        int w = screenRect.width;
        int h = screenRect.height;
        int fmt = X11Defs.ZPixmap;
        long root = x11.XRootWindow(dpy, screen);
        X11.XImage ximg = createXImage(w, h);
        if (ximg == null) {
            return null;
        }
        ximg = x11.XGetSubImage(dpy, root, screenRect.x, screenRect.y, w, h, AllPlanes, fmt, ximg, 0, 0);
        BufferedImage bufImg = XVolatileImage.biFromXImage(ximg, gc);
        x11.XDestroyImage(ximg);
        return bufImg;
    }
