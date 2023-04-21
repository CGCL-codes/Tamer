    public static double halveringsmethode() {
        double onderGrens = 1;
        double bovenGrens = 2;
        double ans = 0;
        double x = (onderGrens + bovenGrens) / 2;
        double aanpassing = 0.001;
        int keer = 0;
        while ((bovenGrens - onderGrens) > 0.001) {
            System.out.println("---------------------------------");
            keer += 1;
            x = (onderGrens + bovenGrens) / 2;
            ans = Math.pow(x, 5) - (3 * Math.pow(x, 2)) - 2;
            System.out.println("Antwoord van de Berekening: " + ans);
            if (ans == 0) {
                onderGrens = x;
                bovenGrens = x;
                return x;
            }
            if (ans < 0) {
                onderGrens += aanpassing;
                System.out.println("onderGrens Omhoog");
            } else {
                bovenGrens -= aanpassing;
                System.out.println("bovenGrens Omlaag");
            }
        }
        System.out.println("x: " + x);
        System.out.println("Aantal Keer:" + keer);
        return x;
    }
