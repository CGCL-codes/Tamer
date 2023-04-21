    public void drawScene(Graphics g) {
        Vector bonds = new Vector();
        for (int ii = 0; ii < pdb.chains.size(); ii++) {
            if (((PDBChain) pdb.chains.elementAt(ii)).isVisible) {
                Vector tmp = ((PDBChain) pdb.chains.elementAt(ii)).bonds;
                for (int i = 0; i < tmp.size(); i++) {
                    bonds.addElement(tmp.elementAt(i));
                }
            }
        }
        if (zbuffer) {
            Zsort.Zsort(bonds);
        }
        for (int i = 0; i < bonds.size(); i++) {
            Bond tmpBond = (Bond) bonds.elementAt(i);
            xstart = (int) ((tmpBond.start[0] - centre[0]) * scale + size().width / 2);
            ystart = (int) ((tmpBond.start[1] - centre[1]) * scale + size().height / 2);
            xend = (int) ((tmpBond.end[0] - centre[0]) * scale + size().width / 2);
            yend = (int) ((tmpBond.end[1] - centre[1]) * scale + size().height / 2);
            xmid = (xend + xstart) / 2;
            ymid = (yend + ystart) / 2;
            if (depthcue && !bymolecule) {
                if (tmpBond.start[2] < (centre[2] - maxwidth / 6)) {
                    g.setColor(tmpBond.startCol.darker().darker());
                    drawLine(g, xstart, ystart, xmid, ymid);
                    g.setColor(tmpBond.endCol.darker().darker());
                    drawLine(g, xmid, ymid, xend, yend);
                } else if (tmpBond.start[2] < (centre[2] + maxwidth / 6)) {
                    g.setColor(tmpBond.startCol.darker());
                    drawLine(g, xstart, ystart, xmid, ymid);
                    g.setColor(tmpBond.endCol.darker());
                    drawLine(g, xmid, ymid, xend, yend);
                } else {
                    g.setColor(tmpBond.startCol);
                    drawLine(g, xstart, ystart, xmid, ymid);
                    g.setColor(tmpBond.endCol);
                    drawLine(g, xmid, ymid, xend, yend);
                }
            } else if (depthcue && bymolecule) {
                if (tmpBond.start[2] < (centre[2] - maxwidth / 6)) {
                    g.setColor(Color.green.darker().darker());
                    drawLine(g, xstart, ystart, xend, yend);
                } else if (tmpBond.start[2] < (centre[2] + maxwidth / 6)) {
                    g.setColor(Color.green.darker());
                    drawLine(g, xstart, ystart, xend, yend);
                } else {
                    g.setColor(Color.green);
                    drawLine(g, xstart, ystart, xend, yend);
                }
            } else if (!depthcue && !bymolecule) {
                g.setColor(tmpBond.startCol);
                drawLine(g, xstart, ystart, xmid, ymid);
                g.setColor(tmpBond.endCol);
                drawLine(g, xmid, ymid, xend, yend);
            } else {
                drawLine(g, xstart, ystart, xend, yend);
            }
        }
    }
