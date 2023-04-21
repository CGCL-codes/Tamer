    public static void salvaUpload(InputStream input, ArquivoVO arquivo) throws FileNotFoundException, IOException {
        File dir = new File(Diretorio.UPLOAD);
        if (!dir.exists()) dir.mkdir();
        BufferedInputStream in = new BufferedInputStream(input);
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(arquivo.getNomeServidorCompleto()));
        for (int b = in.read(); b != -1; out.write(b), b = in.read()) ;
        in.close();
        out.flush();
        out.close();
    }
