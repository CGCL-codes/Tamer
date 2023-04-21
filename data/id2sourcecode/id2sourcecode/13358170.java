    public void put(GML gmlNew, int cursor) {
        if (intPointer < CAPACITY - 1) {
            intPointer++;
            vctGMLs.add(intPointer, gmlNew.clone());
            cursors[intPointer] = cursor;
            if ((vctGMLs.lastElement() != null) && (!vctGMLs.lastElement().equals(gmlNew))) {
                while (vctGMLs.size() > intPointer + 1) vctGMLs.removeElementAt(intPointer + 1);
                for (int i = intPointer + 1; i < cursors.length; i++) cursors[i] = -1;
            }
        } else {
            vctGMLs.remove(0);
            for (int i = 0; i < cursors.length - 1; i++) {
                cursors[i] = cursors[i + 1];
            }
            vctGMLs.add(gmlNew.clone());
            cursors[intPointer] = cursor;
        }
    }
