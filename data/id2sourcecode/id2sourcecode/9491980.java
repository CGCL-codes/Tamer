    public void mouseDragged(MouseEvent e) {
        if (IJ.spaceBarDown()) return;
        if (e.getSource().equals(canvas)) {
            crossLoc = canvas.getCursorLoc();
        } else if (e.getSource().equals(xz_image.getCanvas())) {
            crossLoc.x = xz_image.getCanvas().getCursorLoc().x;
            int pos = xz_image.getCanvas().getCursorLoc().y;
            int z = (int) Math.round(pos / az);
            int slice = flipXZ ? imp.getNSlices() - z : z + 1;
            if (hyperstack) imp.setPosition(imp.getChannel(), slice, imp.getFrame()); else imp.setSlice(slice);
        } else if (e.getSource().equals(yz_image.getCanvas())) {
            int pos;
            if (rotateYZ) {
                crossLoc.y = yz_image.getCanvas().getCursorLoc().x;
                pos = yz_image.getCanvas().getCursorLoc().y;
            } else {
                crossLoc.y = yz_image.getCanvas().getCursorLoc().y;
                pos = yz_image.getCanvas().getCursorLoc().x;
            }
            int z = (int) Math.round(pos / az);
            if (hyperstack) imp.setPosition(imp.getChannel(), z + 1, imp.getFrame()); else imp.setSlice(z + 1);
        }
        update();
    }
