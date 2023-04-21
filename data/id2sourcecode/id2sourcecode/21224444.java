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
