    public static void main(String[] args) throws EprogException {
        int[][] a = new int[3][4];
        int temp = 0;
        float[][] matrix = new float[3][4];
        float[][] matrix1 = new float[3][4];
        float ausgabex1 = 0;
        float ausgabex2 = 0;
        float ausgabex3 = 0;
        float umrechnungsFaktor = 0;
        boolean fehler = false;
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length + 1; j++) {
                try {
                    temp = readInt();
                } catch (EprogException e) {
                    fehler = true;
                }
                if ((temp < 21) && (temp > -21)) {
                    a[i][j] = temp;
                } else {
                    print("FALSCHE EINGABE");
                    System.exit(0);
                }
            }
        }
        for (int j = 0; j < 2; j++) {
            if (a[0][0] == 0) {
                int puffer[] = new int[4];
                for (int i = 0; i < 4; i++) {
                    puffer[i] = a[j + 1][i];
                    a[j + 1][i] = a[0][i];
                    a[0][i] = puffer[i];
                }
            }
        }
        if (a[0][0] == 0) {
            println("FALSCHE EINGABE");
            System.exit(0);
        }
        for (int index = 0; index < a.length; index++) {
            if (a[index][index] == 0) {
                println("FALSCHE EINGABE");
                System.exit(0);
            }
        }
        if (fehler == false) {
            for (int index = 0; index < a[1].length; index++) {
                matrix[0][index] = a[0][index];
                matrix[1][index] = a[1][index] - (a[1][0] / (float) a[0][0]) * a[0][index];
                matrix[2][index] = a[2][index] - (a[2][0] / (float) a[0][0]) * a[0][index];
            }
            for (int index = 0; index < matrix.length; index++) {
                if (matrix[index][index] == 0) {
                    println("FALSCHE EINGABE");
                    System.exit(0);
                }
            }
            umrechnungsFaktor = (matrix[2][1] - (matrix[2][0] / matrix[0][0]) * matrix[0][1]) / (matrix[1][1] - (matrix[1][0] / matrix[0][0]) * matrix[0][1]);
            for (int index = 0; index < matrix[1].length; index++) {
                matrix1[0][index] = matrix[0][index];
                matrix1[1][index] = matrix[1][index];
                matrix1[2][index] = (matrix[2][index]) - umrechnungsFaktor * matrix[1][index];
            }
            ausgabex3 = matrix1[2][3] / matrix1[2][2];
            ausgabex2 = (matrix1[1][3] - (ausgabex3 * matrix1[1][2])) / matrix1[1][1];
            ausgabex1 = (matrix1[0][3] - (ausgabex2 * matrix1[0][1]) - (ausgabex3 * matrix[0][2])) / matrix1[0][0];
            for (int index = 0; index < matrix1.length; index++) {
                if (matrix1[index][index] == 0) {
                    println("FALSCHE EINGABE");
                    System.exit(0);
                }
            }
            printFixed(ausgabex1);
            print(" ");
            printFixed(ausgabex2);
            print(" ");
            printFixed(ausgabex3);
        } else {
            println("?");
        }
    }
