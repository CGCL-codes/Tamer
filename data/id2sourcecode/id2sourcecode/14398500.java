    public void removeTabPanel(int i) {
        if (i < 0 || i >= nbTab) return;
        tabPanel.remove(cards[i]);
        for (int j = i; j < nbTab - 1; j++) {
            arrName[j] = arrName[j + 1];
            cards[j] = cards[j + 1];
        }
        nbTab--;
        if (selected == i) select(Math.min(selected + 1, nbTab - 1));
        mustRecalculate = true;
        repaint();
    }
