    public Ponto getCG() {
        int xMenor = 0;
        int xMaior = 0;
        int yMenor = 0;
        int yMaior = 0;
        for (Ponto ponto : vertices) {
            int x = ponto.getX();
            int y = ponto.getY();
            if (x < xMenor) xMenor = x;
            if (x > xMaior) xMaior = x;
            if (y < yMenor) yMenor = y;
            if (y > yMaior) yMaior = y;
        }
        int x = (xMaior + xMenor) / 2;
        int y = (yMaior + yMenor) / 2;
        return new Ponto(x, y);
    }
