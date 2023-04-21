    public void ecrirePostScript() {
        try {
            File fichier;
            final JFileChooser fc = new JFileChooser();
            final int returnVal = fc.showOpenDialog(container_);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                fichier = fc.getSelectedFile();
                final PrintWriter fichier_ps = new PrintWriter(new FileWriter(fichier + ".ps"));
                final int TMAX = 700;
                fichier_ps.println(" \n newpath\n");
                fichier_ps.println(" /Times-Roman findfont \n");
                fichier_ps.println(" 15 scalefont \n");
                fichier_ps.println(" setfont \n");
                fichier_ps.println(" 150 750 moveto \n");
                fichier_ps.println(" (Modelisation du Port) show \n");
                fichier_ps.println(" 150 730 moveto \n");
                fichier_ps.println(" ( nombre de Gares: " + donnees_.getListeGare_().getListeGares_().size() + "     nombre de Bassins: " + donnees_.getListebassin_().getListeBassins_().size() + ")show \n");
                fichier_ps.println(" 150 710 moveto \n");
                fichier_ps.println(" ( nombre de cheneaux: " + donnees_.getListeChenal_().getListeChenaux_().size() + "     nombre d'ecluses: " + donnees_.getListeEcluse_().getListeEcluses_().size() + ")show \n");
                fichier_ps.println(" 150 690 moveto \n");
                fichier_ps.println(" ( nombre de quais: " + donnees_.getlQuais_().getlQuais_().size() + "     nombre de cercles d'evitage: " + donnees_.getListeCercle_().getListeCercles_().size() + ")show \n");
                fichier_ps.println("/Helvetica findfont");
                fichier_ps.println("20 scalefont");
                fichier_ps.println("setfont");
                for (int i = 0; i < this.donnees_.getParams_().grapheTopologie.nbArcs; i++) {
                    fichier_ps.println(donnees_.getParams_().grapheTopologie.graphe[i].xGare1 + " " + (TMAX - donnees_.getParams_().grapheTopologie.graphe[i].yGare1) + " moveto");
                    fichier_ps.println(donnees_.getParams_().grapheTopologie.graphe[i].xGare1 + " " + (TMAX - donnees_.getParams_().grapheTopologie.graphe[i].yGare1) + " 6 0 360 arc");
                    fichier_ps.println(donnees_.getParams_().grapheTopologie.graphe[i].xGare2 + " " + (TMAX - donnees_.getParams_().grapheTopologie.graphe[i].yGare2) + " moveto");
                    fichier_ps.println(donnees_.getParams_().grapheTopologie.graphe[i].xGare2 + " " + (TMAX - donnees_.getParams_().grapheTopologie.graphe[i].yGare2) + " 6 0 360 arc");
                    fichier_ps.println("stroke");
                    final int xg1 = this.donnees_.getParams_().grapheTopologie.graphe[i].xGare1;
                    final int yg1 = this.donnees_.getParams_().grapheTopologie.graphe[i].yGare1;
                    final int xg2 = this.donnees_.getParams_().grapheTopologie.graphe[i].xGare2;
                    final int yg2 = this.donnees_.getParams_().grapheTopologie.graphe[i].yGare2;
                    switch(this.donnees_.getParams_().grapheTopologie.graphe[i].typeConnection) {
                        case 0:
                            fichier_ps.println(" " + (xg1 + 5) + " " + (TMAX - yg1) + " moveto");
                            fichier_ps.println(" " + (xg1 + 25) + " " + (TMAX - yg1) + " lineto");
                            fichier_ps.println(" stroke");
                            fichier_ps.println(" " + (xg1 + 25) + " " + (TMAX - yg1 - 15) + " moveto");
                            fichier_ps.println(" " + (xg1 + 25) + " " + (TMAX - yg1 - 15 + 50) + " lineto");
                            fichier_ps.println(" " + (xg1 + 25 + 50) + " " + (TMAX - yg1 - 15 + 50) + " lineto");
                            fichier_ps.println(" " + (xg1 + 25 + 50) + " " + (TMAX - yg1 - 15) + " lineto");
                            fichier_ps.println(" " + (xg1 + 25) + " " + (TMAX - yg1 - 15) + " lineto");
                            fichier_ps.println(" stroke");
                            break;
                        case 1:
                            fichier_ps.println(" " + (xg1 + 5) + " " + (TMAX - yg1 + 5) + " moveto");
                            fichier_ps.println(" " + (xg2) + " " + (TMAX - yg2 + 5) + " lineto");
                            fichier_ps.println(" stroke");
                            fichier_ps.println(" " + (xg1 - 5) + " " + (TMAX - yg1 - 5) + " moveto");
                            fichier_ps.println(" " + (xg2 - 5) + " " + (TMAX - yg2 - 5) + " lineto");
                            fichier_ps.println(" stroke");
                            break;
                        case 2:
                            final float xi1 = (xg1 + 10 + xg2) / 2;
                            final float yi1 = (yg1 + yg2 + 10) / 2;
                            final float xi2 = (xg1 + xg2 - 10) / 2;
                            final float yi2 = (yg1 + yg2 - 10) / 2;
                            final float xi11 = (xi1 + xg1 + 5) / 2;
                            final float yi11 = (yi1 + yg1 + 5) / 2;
                            final float xi21 = (xi2 + xg1 - 5) / 2;
                            final float yi21 = (yi2 + yg1 - 5) / 2;
                            final float xi3 = (xi1 + xi2) / 2;
                            final float yi3 = (yi1 + yi2) / 2;
                            fichier_ps.println(" " + (xi11) + " " + (TMAX - yi11) + " moveto");
                            fichier_ps.println(" " + (xi3) + " " + (TMAX - yi3) + " lineto");
                            fichier_ps.println(" stroke");
                            fichier_ps.println(" " + (xi21) + " " + (TMAX - yi21) + " moveto");
                            fichier_ps.println(" " + (xi3) + " " + (TMAX - yi3) + " lineto");
                            fichier_ps.println(" stroke");
                            final float xi12 = (xi1 + 5 + xg2) / 2;
                            final float yi12 = (yi1 + yg2 + 5) / 2;
                            final float xi22 = (xi2 - 5 + xg2) / 2;
                            final float yi22 = (yi2 + yg2 - 5) / 2;
                            final float xi32 = (xi12 + xi22) / 2;
                            final float yi32 = (yi12 + yi22) / 2;
                            fichier_ps.println(" " + (xi1) + " " + (TMAX - yi1) + " moveto");
                            fichier_ps.println(" " + (xi32) + " " + (TMAX - yi32) + " lineto");
                            fichier_ps.println(" stroke");
                            fichier_ps.println(" " + (xi2) + " " + (TMAX - yi2) + " moveto");
                            fichier_ps.println(" " + (xi32) + " " + (TMAX - yi32) + " lineto");
                            fichier_ps.println(" stroke");
                            fichier_ps.println(" " + (xg2) + " " + (TMAX - yg2) + " moveto");
                            fichier_ps.println(" " + (xi32) + " " + (TMAX - yi32) + " lineto");
                            fichier_ps.println(" stroke");
                            fichier_ps.println(" " + (xg1 + 5) + " " + (TMAX - yg1 + 5) + " moveto");
                            fichier_ps.println(" " + (xi11) + " " + (TMAX - yi11) + " lineto");
                            fichier_ps.println(" stroke");
                            fichier_ps.println(" " + (xg1 - 5) + " " + (TMAX - yg1 - 5) + " moveto");
                            fichier_ps.println(" " + (xi21) + " " + (TMAX - yi21) + " lineto");
                            fichier_ps.println(" stroke");
                            fichier_ps.println(" " + (xi1) + " " + (TMAX - yi1) + " moveto");
                            fichier_ps.println(" " + (xi11) + " " + (TMAX - yi11) + " lineto");
                            fichier_ps.println(" stroke");
                            fichier_ps.println(" " + (xi2) + " " + (TMAX - yi2) + " moveto");
                            fichier_ps.println(" " + (xi21) + " " + (TMAX - yi21) + " lineto");
                            fichier_ps.println(" stroke");
                            break;
                        case 3:
                            fichier_ps.println(" " + (xg1) + " " + (TMAX - yg1) + " moveto");
                            fichier_ps.println(" " + (xg2) + " " + (TMAX - yg2) + " lineto");
                            fichier_ps.println(" stroke");
                            fichier_ps.println((((xg2 + xg1) / 2) - 15) + " " + (TMAX - (((yg2 + yg1) / 2) - 15)) + " moveto");
                            fichier_ps.println((((xg2 + xg1) / 2) - 15) + " " + (TMAX - (((yg2 + yg1) / 2) - 15)) + " 20 0 360 arc");
                            break;
                    }
                }
                fichier_ps.print("\n\n\n showpage");
                fichier_ps.close();
            }
        } catch (final IOException exception) {
        }
    }
