    public void setDragDrop(final ColorItem item) {
        final Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
        final int operations = DND.DROP_MOVE;
        final DragSource source = new DragSource(item, operations);
        source.setTransfer(types);
        source.addDragListener(new DragSourceListener() {

            public void dragFinished(DragSourceEvent event) {
                if (event.detail == DND.DROP_MOVE) {
                }
            }

            public void dragSetData(DragSourceEvent event) {
                String string = "";
                if (item.getColor() != null) {
                    RGB rgb = item.getColor().getRGB();
                    if (copyMode == 0) {
                        string = ColorUtils.getSWTColorString(rgb);
                    } else if (copyMode == 1) {
                        string = ColorUtils.getAWTColorString(rgb);
                    } else if (copyMode == 2) {
                        string = ColorUtils.getCssColorString(rgb);
                    } else if (copyMode == 3) {
                        string = ColorUtils.getHexColorString(rgb);
                    }
                } else {
                    string = item.getGradientData().toString();
                }
                event.data = string;
            }

            public void dragStart(DragSourceEvent event) {
                event.doit = true;
                isDragging = true;
            }
        });
        final DropTarget target = new DropTarget(item, operations);
        target.setTransfer(types);
        target.addDropListener(new DropTargetAdapter() {

            public void drop(DropTargetEvent event) {
                if (event.data == null) {
                    event.detail = DND.DROP_NONE;
                    return;
                }
                ColorItem selectedCanvas2 = getSelectedCanvas();
                if (selectedCanvas2 == null || !isDragging) {
                    return;
                }
                isDragging = false;
                int oldPos = selectedCanvas2.getIndex();
                final int newPos = item.index;
                if ((oldPos < colors.length) && (newPos < colors.length)) {
                    final IProxyColor tmp = colors[oldPos];
                    if (oldPos > newPos) {
                        for (int i = oldPos; i > newPos; i--) {
                            colors[i] = colors[i - 1];
                        }
                        colors[newPos] = tmp;
                    } else {
                        for (int i = oldPos; i < newPos; i++) {
                            colors[i] = colors[i + 1];
                        }
                        colors[newPos] = tmp;
                    }
                    updateColors();
                    isDirty = true;
                }
            }
        });
    }
