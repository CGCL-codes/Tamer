    public static void main(String[] args) {
        try {
            BufferedWriter ficheroSalida = new BufferedWriter(new FileWriter(Messages.getString("main.fichero.salida")));
            File dir = new File(directorio);
            File files[] = dir.listFiles(fileFilter);
            for (File file : files) {
                if (file.isFile()) {
                    ficheroSalida.write(readEXIFMetadaExtractor(file).toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
