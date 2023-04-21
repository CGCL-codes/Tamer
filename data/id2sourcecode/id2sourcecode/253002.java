    public void addProtein(Collection c) {
        proteins.addAll((Vector) (c));
        if (proteins.size() < 10) {
            myColor = colors[0];
        } else if (proteins.size() < 20) {
            myColor = colors[1];
        } else if (proteins.size() < 30) {
            myColor = colors[2];
        } else if (proteins.size() < 40) {
            myColor = colors[3];
        } else if (proteins.size() < 60) {
            myColor = colors[4];
        } else {
            myColor = colors[5];
        }
        this.repaint();
        E2DProtein p = null;
        double pi = 0;
        for (int i = 0; i < ((Vector) c).size(); i++) {
            names.add(((E2DProtein) ((Vector) c).elementAt(i)).toString());
            p = ((E2DProtein) ((Vector) c).elementAt(i));
            pi = p.getPI();
            if (pi > maxPi) {
                maxPi = pi;
            } else if (pi < minPi) {
                minPi = pi;
            }
        }
        myPi = (maxPi + minPi) / 2;
    }
