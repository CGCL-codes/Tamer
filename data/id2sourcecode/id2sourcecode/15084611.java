    public void getData(int x1, int y1, int x2, int y2, String rootDir) {
        Vector imagesV = new Vector();
        itemsV.removeAllElements();
        Rectangle rect = new Rectangle(x1 + INV_BOX_X_OFFSET, y1 + INV_BOX_Y_OFFSET, x2, y2);
        for (int i = 0; i < 5; i++) {
            selectInventoryTab(x1, y1, i);
            try {
                imagesV.add(robot.createScreenCapture(rect));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < imagesV.size(); i++) {
            Vector v = muleOCR.getInventoryItems((BufferedImage) imagesV.elementAt(i), i + 1);
            selectInventoryTab(x1, y1, i);
            for (int j = 0; j < v.size(); j++) {
                robot.mouseMove(x1 + x2 - 5, y1 + INV_BOX_Y_OFFSET + 5 + ITEM_OFFSET * (j + 1));
                robot.delay(700);
                getFullItemDescription(x1 + x2, y1 + INV_BOX_Y_OFFSET + 5 + ITEM_OFFSET * (j + 1) + ITEM_Y_OFFSET, i + 1, j + 1, rootDir);
            }
            itemsV.addAll(v);
        }
    }
