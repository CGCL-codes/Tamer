    private static void printHelp() {
        Reader inp;
        try {
            URL url = DChess.class.getClassLoader().getResource("org/amse/grigory/dchess/dchess/man");
            inp = new BufferedReader(new InputStreamReader(url.openStream()));
            char[] temp = new char[1];
            int c = 0;
            c = inp.read(temp);
            while (c > 0) {
                System.out.print(temp);
                c = inp.read(temp);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        System.out.println("");
    }
