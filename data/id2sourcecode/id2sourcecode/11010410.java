    private void paintPort(final Graphics _g) {
        _g.setColor(Color.black);
        if (tableauGare_.size() == 1) {
            PointPort p = (PointPort) tableauGare_.get(0);
            _g.fillOval(p.x_ - 7, p.y_ - 7, 15, 15);
            _g.drawString("" + this.donnees_.getListeGare_().retournerGare(0), p.x_ + 10, p.y_ - 15);
        }
        for (int i = 0; i < this.donnees_.getParams_().grapheTopologie.nbArcs; i++) {
            final int xg1 = this.donnees_.getParams_().grapheTopologie.graphe[i].xGare1;
            final int yg1 = this.donnees_.getParams_().grapheTopologie.graphe[i].yGare1;
            final int xg2 = this.donnees_.getParams_().grapheTopologie.graphe[i].xGare2;
            final int yg2 = this.donnees_.getParams_().grapheTopologie.graphe[i].yGare2;
            paintGares(_g, i, xg1, yg1, xg2, yg2);
            _g.setColor(Color.black);
            final float xi1 = (xg1 + 10 + xg2) / 2;
            final float yi1 = (yg1 + yg2 + 10) / 2;
            final float xi2 = (xg1 + xg2 - 10) / 2;
            final float yi2 = (yg1 + yg2 - 10) / 2;
            final float xi11 = (xi1 + xg1 + 5) / 2;
            final float yi11 = (yi1 + yg1 + 5) / 2;
            final float xi21 = (xi2 + xg1 - 5) / 2;
            final float yi21 = (yi2 + yg1 - 5) / 2;
            final float xi31 = (xi11 + xi21) / 2;
            final float yi31 = (yi11 + yi21) / 2;
            final float xi12 = (xi1 + 5 + xg2) / 2;
            final float yi12 = (yi1 + yg2 + 5) / 2;
            final float xi22 = (xi2 - 5 + xg2) / 2;
            final float yi22 = (yi2 + yg2 - 5) / 2;
            final float xi32 = (xi12 + xi22) / 2;
            final float yi32 = (yi12 + yi22) / 2;
            switch(this.donnees_.getParams_().grapheTopologie.graphe[i].typeConnection) {
                case 0:
                    if (xg1 != 0 && yg1 != 0) {
                        _g.drawLine(xg1 + 5, yg1, xg1 + 25 * 3, yg1);
                        _g.drawRect(xg1 + 25 * 3, yg1 - 45, 180, 90);
                        if (afficheConnections_) {
                            _g.setColor(Color.red);
                            _g.drawString("" + this.donnees_.getParams_().grapheTopologie.graphe[i].nomConnection, xg1 + 25 * 3 + 60, yg1 - 50);
                            _g.setColor(Color.blue);
                            final int initx = xg1 + 125 / 2 + 25 + 10;
                            int inity = yg1 - 35;
                            for (int k = 0; k < this.donnees_.getlQuais_().getlQuais_().size(); k++) {
                                if (donnees_.getlQuais_().retournerQuais(k).getNomBassin_() == this.donnees_.getParams_().grapheTopologie.graphe[i].nomConnection) {
                                    _g.drawString("" + this.donnees_.getlQuais_().retournerQuais(k).getNom(), initx + 25, inity);
                                }
                                inity = inity + 10;
                            }
                        }
                    }
                    break;
                case 10:
                    if (xg1 != 0 && yg1 != 0) {
                        _g.drawLine(xg1, yg1, xg1, yg1 + 25 * 3);
                        _g.drawRect(xg1 - 90, yg1 + 25 * 3, 180, 90);
                        if (afficheConnections_) {
                            _g.setColor(Color.red);
                            final int initx = xg1 - 90 + 50;
                            int inity = yg1 + 25 * 3;
                            _g.drawString("" + this.donnees_.getParams_().grapheTopologie.graphe[i].nomConnection, initx + 10, inity - 3);
                            _g.setColor(Color.blue);
                            for (int k = 0; k < this.donnees_.getlQuais_().getlQuais_().size(); k++) {
                                if (donnees_.getlQuais_().retournerQuais(k).getNomBassin_() == this.donnees_.getParams_().grapheTopologie.graphe[i].nomConnection) {
                                    _g.drawString("" + this.donnees_.getlQuais_().retournerQuais(k).getNom(), initx, inity + 15);
                                }
                                inity = inity + 10;
                            }
                        }
                    }
                    break;
                case 1000:
                    if (xg1 != 0 && yg1 != 0) {
                        _g.drawLine(xg1, yg1, xg1, yg1 - 25 * 3);
                        _g.drawRect(xg1 - 90, yg1 - 115 - 50, 180, 90);
                        if (afficheConnections_) {
                            _g.setColor(Color.red);
                            final int initx = xg1 - 90 + 50;
                            int inity = yg1 - 100;
                            _g.drawString("" + this.donnees_.getParams_().grapheTopologie.graphe[i].nomConnection, initx + 10, inity - 15 - 25 * 2);
                            _g.setColor(Color.blue);
                            for (int k = 0; k < this.donnees_.getlQuais_().getlQuais_().size(); k++) {
                                if (donnees_.getlQuais_().retournerQuais(k).getNomBassin_() == this.donnees_.getParams_().grapheTopologie.graphe[i].nomConnection) {
                                    _g.drawString("" + this.donnees_.getlQuais_().retournerQuais(k).getNom(), initx, inity - 25 * 2);
                                }
                                inity = inity + 10;
                            }
                        }
                    }
                    break;
                case 100:
                    if (xg1 != 0 && yg1 != 0) {
                        _g.drawLine(xg1, yg1, xg1 - 25 * 3, yg1);
                        _g.drawRect(xg1 - 205 - 50, yg1 - 45, 180, 90);
                        if (afficheConnections_) {
                            _g.setColor(Color.red);
                            final int initx = xg1 - 190;
                            int inity = yg1 - 20;
                            _g.drawString("" + this.donnees_.getParams_().grapheTopologie.graphe[i].nomConnection, initx, inity - 15);
                            _g.setColor(Color.blue);
                            for (int k = 0; k < this.donnees_.getlQuais_().getlQuais_().size(); k++) {
                                if (donnees_.getlQuais_().retournerQuais(k).getNomBassin_() == this.donnees_.getParams_().grapheTopologie.graphe[i].nomConnection) {
                                    _g.drawString("" + this.donnees_.getlQuais_().retournerQuais(k).getNom(), initx, inity);
                                }
                                inity = inity + 10;
                            }
                        }
                    }
                    break;
                case 1:
                    _g.drawLine(xg1, yg1 + 5, xg2, yg2 + 5);
                    _g.drawLine(xg1, yg1 - 5, xg2, yg2 - 5);
                    break;
                case 2:
                    _g.setColor(Color.blue);
                    if (afficheConnections_) {
                        _g.drawString("" + this.donnees_.getParams_().grapheTopologie.graphe[i].nomConnection, (xg2 + xg1) / 2 + 10, (yg2 + yg1) / 2 - 15);
                    }
                    _g.drawLine((int) xi31, (int) yi31, (int) xi1, (int) yi1);
                    _g.drawLine((int) xi31, (int) yi31, (int) xi2, (int) yi2);
                    _g.drawLine((int) xi32, (int) yi32, (int) xi1, (int) yi1);
                    _g.drawLine((int) xi32, (int) yi32, (int) xi2, (int) yi2);
                    _g.drawLine((int) xi32, (int) yi32, xg2, yg2);
                    _g.drawLine((int) xi31, (int) yi31, xg1, yg1);
                    _g.setColor(Color.black);
                    break;
                case 12:
                    _g.setColor(Color.blue);
                    if (afficheConnections_) {
                        _g.drawString("" + this.donnees_.getParams_().grapheTopologie.graphe[i].nomConnection, (xg2 + xg1) / 2 + 10, (yg2 + yg1) / 2 + 35);
                    }
                    _g.drawLine((int) xi31, (int) yi31 + 15, (int) xi1, (int) yi1 + 15);
                    _g.drawLine((int) xi31, (int) yi31 + 15, (int) xi2, (int) yi2 + 15);
                    _g.drawLine((int) xi32, (int) yi32 + 15, (int) xi1, (int) yi1 + 15);
                    _g.drawLine((int) xi32, (int) yi32 + 15, (int) xi2, (int) yi2 + 15);
                    _g.drawLine((int) xi32, (int) yi32 + 15, xg2, yg2);
                    _g.drawLine((int) xi31, (int) yi31 + 15, xg1, yg1);
                    _g.setColor(Color.black);
                    break;
                case 22:
                    _g.setColor(Color.blue);
                    if (afficheConnections_) {
                        _g.drawString("" + this.donnees_.getParams_().grapheTopologie.graphe[i].nomConnection, (xg2 + xg1) / 2 + 10, (yg2 + yg1) / 2 - 45);
                    }
                    _g.drawLine((int) xi31, (int) yi31 - 15, (int) xi1, (int) yi1 - 15);
                    _g.drawLine((int) xi31, (int) yi31 - 15, (int) xi2, (int) yi2 - 15);
                    _g.drawLine((int) xi32, (int) yi32 - 15, (int) xi1, (int) yi1 - 15);
                    _g.drawLine((int) xi32, (int) yi32 - 15, (int) xi2, (int) yi2 - 15);
                    _g.drawLine((int) xi32, (int) yi32 - 15, xg2, yg2);
                    _g.drawLine((int) xi31, (int) yi31 - 15, xg1, yg1);
                    _g.setColor(Color.black);
                    break;
                case 3:
                    _g.drawLine(xg1, yg1, xg2, yg2);
                    _g.drawOval(((xg2 + xg1) / 2) - 15, ((yg2 + yg1) / 2) - 15, 30, 30);
                    break;
            }
            _g.setColor(Color.white);
            _g.fillOval(0 - 7, 0 - 7, 15, 15);
            _g.setColor(Color.black);
        }
    }
