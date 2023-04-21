    public void draw(Graphics g, int panelx, int panely, long startbase, long endbase, float xscale, int yspace, FeatureProperty fp) {
        for (int i = 0; i < size(); i++) {
            SeqFeatureI sf = getFeatureAt(i);
            if (i < size() - 1) {
                long intstart = getFeatureAt(i).getEnd();
                long intend = getFeatureAt(i + 1).getStart();
                long intmid = (intend + intstart) / 2;
                if (fp.getStyle() == FeatureProperty.GENE) {
                    if (intstart < endbase && intend > startbase) {
                        g.setColor(Color.gray);
                        g.drawLine(panelx + (int) ((intstart - startbase + 1) / xscale), panely - yspace / 2, panelx + (int) ((intend - startbase) / xscale), panely - yspace / 2);
                    }
                }
            }
            if (fullDraw) {
                ((Drawable) getFeatureAt(i)).draw(g, panelx, panely, startbase, endbase, xscale, yspace, fp);
            } else {
                _draw(g, panelx, panely, startbase, endbase, xscale, yspace, fp);
            }
        }
    }
