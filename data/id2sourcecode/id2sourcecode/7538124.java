    public static void main(String[] args) throws Exception {
        File f = new File("test.csv");
        if (!f.exists()) {
            throw new Exception("Pas de fichier !");
        }
        CSVReader reader = new CSVReader(new FileReader(f), CARACTERE_SEPARATEUR);
        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            try {
                System.out.println("Ligne du CSV " + nextLine[0] + nextLine[1] + nextLine[2] + nextLine[3]);
            } catch (java.lang.ArrayIndexOutOfBoundsException e) {
                break;
            }
        }
    }
