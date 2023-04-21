    private void zoomout() {
        double gesamtlaenge, mitte, neuBegin, neuEnde;
        gesamtlaenge = currentEnde - currentBegin;
        mitte = (currentEnde + currentBegin) / 2;
        neuBegin = mitte - (gesamtlaenge / (2 * zoomValue));
        neuEnde = mitte + (gesamtlaenge / (2 * zoomValue));
        if (neuBegin < 0) neuBegin = 0;
        if (neuEnde > 1) neuEnde = 1;
        currentBegin = neuBegin;
        currentEnde = neuEnde;
        redraw();
    }
