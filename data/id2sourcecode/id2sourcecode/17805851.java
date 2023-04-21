    public void show() {
        TacheUI tacheCourante = (TacheUI) aireDeDessin.getObjetSurvole();
        listeBoutons[2].activer(tacheCourante.getPere() != null && tacheCourante.getPere().getListefils().size() > 1);
        listeBoutons[3].activer(tacheCourante.getPere() != null);
        listeBoutons[7].activer(tacheCourante.getPere() != null);
        listeBoutons[4].setTypeAction(tacheCourante.estDeroule() ? Categorie.CACHE : Categorie.VUE);
        if (tacheCourante != null) {
            int x = (int) tacheCourante.getLocationOnScreen().getX() + aireDeDessin.getObjetSurvole().getWidth() / 2;
            int y = (int) tacheCourante.getLocationOnScreen().getY() + aireDeDessin.getObjetSurvole().getHeight() / 2;
            positionScreen = new Point(x - (largeur / 2), y - (hauteur / 2));
            backgroundImage = robot.createScreenCapture(new Rectangle(positionScreen.x, positionScreen.y, largeur, hauteur));
            repaint();
            popup = factory.getPopup(f, panel, positionScreen.x, positionScreen.y);
            popup.show();
        }
    }
