    void doResize() {
        try {
            int yFieldP = (int) primaryField.getLocationOnScreen().getY() - (int) primaryEntity.getLocationOnScreen().getY() + primaryEntity.getLocation().y;
            int yFieldF = (int) foreignField.getLocationOnScreen().getY() - (int) foreignEntity.getLocationOnScreen().getY() + foreignEntity.getLocation().y;
            int py = yFieldP + (primaryField.getSize().height / 2);
            int fy = yFieldF + (foreignField.getSize().height / 2);
            int px1 = primaryEntity.getLocation().x;
            int px2 = px1 + primaryEntity.getSize().width;
            int fx1 = foreignEntity.getLocation().x;
            int fx2 = fx1 + foreignEntity.getSize().width;
            int xMin = 0, yMin = 0, xMax = 0, yMax = 0;
            if (px2 < fx1) {
                serie[0].x = px2;
                serie[0].y = py;
                serie[2].x = fx1;
                serie[2].y = fy;
                serie[1].x = (px2 + fx1) / 2;
                serie[1].y = (py + fy) / 2;
                xMin = px1;
                xMax = fx2;
            } else if (px1 > fx2) {
                serie[0].x = fx2;
                serie[0].y = fy;
                serie[2].x = px1;
                serie[2].y = py;
                serie[1].x = (fx2 + px1) / 2;
                serie[1].y = (py + fy) / 2;
                xMin = fx1;
                xMax = px2;
            } else {
                serie[0].x = px2;
                serie[0].y = py;
                serie[2].x = fx2;
                serie[2].y = fy;
                serie[1].x = Math.max(px2, fx2) + 30;
                serie[1].y = (py + fy) / 2;
                xMin = Math.min(px2, fx2);
                xMax = serie[1].x;
            }
            yMin = Math.min(py, fy);
            yMax = Math.max(py, fy);
            Rectangle area = new Rectangle(xMin, yMin, xMax - xMin + 1, yMax - yMin + 1);
            for (int i = 0; i < serie.length; ++i) {
                serie[i].x -= area.x;
                serie[i].y -= area.y;
            }
            setBounds(area);
            anchor.setLocation(serie[1].x + area.x - (anchor.getSize().width / 2), serie[1].y + area.y - (anchor.getSize().height / 2));
        } catch (Exception e) {
        }
    }
