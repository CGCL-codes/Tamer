    protected void appliquerFacteur(double facteur) {
        double xCentre = (xMin + xMax) / 2;
        double yCentre = (yMin + yMax) / 2;
        xMax = (xMax - xCentre) * facteur + xCentre;
        xMin = (xMin - xCentre) * facteur + xCentre;
        yMax = (yMax - yCentre) * facteur + yCentre;
        yMin = (yMin - yCentre) * facteur + yCentre;
    }
