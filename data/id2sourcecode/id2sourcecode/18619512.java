    public void deleteSheet() {
        if (numberOfSheets > 1) {
            Sheet[] old;
            old = s;
            s = new Sheet[numberOfSheets - 1];
            if (currentSheet == 0) {
                old[0].undraw(panel);
                for (int x = 0; x < numberOfSheets - 1; x++) {
                    s[x] = old[x + 1];
                }
                currentSheet = 0;
                s[currentSheet].draw(panel);
            } else if (currentSheet == numberOfSheets - 1) {
                old[numberOfSheets - 1].undraw(panel);
                for (int x = 0; x < numberOfSheets - 1; x++) {
                    s[x] = old[x];
                }
                currentSheet = currentSheet - 1;
                s[currentSheet].draw(panel);
            } else {
                old[currentSheet].undraw(panel);
                for (int x = 0; x < currentSheet; x++) {
                    s[x] = old[x];
                }
                for (int x = currentSheet + 1; x <= numberOfSheets - 1; x++) {
                    s[x - 1] = old[x];
                }
                currentSheet = currentSheet - 1;
                s[currentSheet].draw(panel);
            }
            numberOfSheets--;
        } else {
            s[0].undraw(panel);
            s = new Sheet[1];
            currentSheet = 0;
            s[0] = new Sheet(cat, panel, baseURL, outputFormat, xmlDragging, xmlThreeClicks);
            s[0].draw(panel);
        }
    }
