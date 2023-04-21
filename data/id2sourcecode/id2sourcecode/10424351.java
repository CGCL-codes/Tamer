    public void mouseDragged(WidgetMouseEvent e, ViewerCanvas view) {
        Camera cam = view.getCamera();
        Point dragPoint = e.getPoint();
        UVMappingViewer uvview = (UVMappingViewer) view;
        int dx, dy;
        dx = dragPoint.x - clickPoint.x;
        dy = dragPoint.y - clickPoint.y;
        if (controlDown) {
            double factor = Math.pow(1.01, dy);
            double midu = (minu + maxu) / 2;
            double midv = (minv + maxv) / 2;
            double newminu = ((minu - midu) / factor) + midu;
            double newmaxu = ((maxu - midu) / factor) + midu;
            double newminv = ((minv - midv) / factor) + midv;
            double newmaxv = ((maxv - midv) / factor) + midv;
            uvview.setParameters(newminu, newmaxu, newminv, newmaxv);
        } else {
            if (e.isShiftDown()) {
                if (Math.abs(dx) > Math.abs(dy)) dy = 0; else dx = 0;
            }
            double du = (minu - maxu) * dx / vwidth;
            double dv = (maxv - minv) * dy / vheight;
            uvview.setParameters(minu + du, maxu + du, minv + dv, maxv + dv);
        }
    }
