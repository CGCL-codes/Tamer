    @Override
    public void paint(Graphics g) {
        for (int i = 0; i < 13; i++) {
            if (i != 6) {
                int leftBase = pointWidth * i;
                int rightBase = pointWidth * (i + 1);
                int middle = (leftBase + rightBase) / 2;
                Polygon tTri = new Polygon();
                tTri.addPoint(leftBase, top);
                tTri.addPoint(rightBase, top);
                tTri.addPoint(middle, top + pointHeight);
                Polygon bTri = new Polygon();
                bTri.addPoint(leftBase, bottom);
                bTri.addPoint(rightBase, bottom);
                bTri.addPoint(middle, bottom - pointHeight);
                if (i % 2 == 0) {
                    g.setColor(color1);
                    g.fillPolygon(tTri);
                    g.drawPolygon(tTri);
                    g.setColor(color2);
                    g.fillPolygon(bTri);
                    g.drawPolygon(bTri);
                } else if (i % 2 == 1) {
                    g.setColor(color2);
                    g.fillPolygon(tTri);
                    g.drawPolygon(tTri);
                    g.setColor(color1);
                    g.fillPolygon(bTri);
                    g.drawPolygon(bTri);
                }
            } else {
                int rectX = (left + pointWidth * i);
                int rectY = (top);
                g.setColor(barColor);
                g.fillRect(rectX, rectY, pointWidth, boardHeight);
                g.drawRect(rectX, rectY, pointWidth, boardHeight);
            }
        }
    }
