    public static void main(String[] args) {
        try {
            System.out.print("Ambiente de Desenvolvimento Kad ");
            String arquivo = System.getenv("KAD_HOME");
            if (arquivo == null) {
                arquivo = System.getenv("KAD_LIBS");
            }
            if (arquivo != null) {
                arquivo += File.separator + "kad-prompt.txt";
                Scanner s = new Scanner(new File(arquivo));
                while (s.hasNextLine()) System.out.println(s.nextLine());
                s.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("PROMPT KAD ERRO");
        }
    }
