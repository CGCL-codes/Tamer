    public static double getPI(String pro) {
        double pH = 7;
        double lowpH = 0, highpH = 14;
        int Plength = pro.length();
        double charge = 1;
        char type = 'n';
        double pK = 0;
        while (Math.abs(charge) >= .005) {
            charge = 0;
            for (int a = 0; a < Plength; a++) {
                switch(pro.charAt(a)) {
                    case 'R':
                        type = 'b';
                        pK = 12;
                        break;
                    case 'D':
                        type = 'a';
                        pK = 4.05;
                        break;
                    case 'C':
                        type = 'a';
                        pK = 9;
                        break;
                    case 'E':
                        type = 'a';
                        pK = 4.75;
                        break;
                    case 'H':
                        type = 'b';
                        pK = 5.98;
                        break;
                    case 'K':
                        type = 'b';
                        pK = 10;
                        break;
                    case 'Y':
                        type = 'a';
                        pK = 10;
                        break;
                    default:
                        type = 'n';
                        pK = 0;
                        break;
                }
                if (type == 'a') {
                    charge += -1 / (1 + Math.pow(10, pK - pH));
                }
                if (type == 'b') {
                    charge += 1 / (1 + Math.pow(10, pH - pK));
                }
            }
            charge += -1 / (1 + Math.pow(10, 3.2 - pH));
            charge += 1 / (1 + Math.pow(10, pH - 8.2));
            if (charge > 0.005) {
                lowpH = pH;
                pH = (lowpH + highpH) / 2;
            }
            if (charge < -0.005) {
                highpH = pH;
                pH = (lowpH + highpH) / 2;
            }
        }
        return pH;
    }
