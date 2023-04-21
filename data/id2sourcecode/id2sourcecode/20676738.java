    public static void copiar(File origem, File destino, String base, boolean pararError, boolean apagar, boolean debug, IOFileFilter... filtros) throws IOException {
        if (!origem.isDirectory() && origem.exists()) {
            File destinoArquivo = new File(destino, getCaminhoRelativo(origem, base).equals("") ? origem.getName() : getCaminhoRelativo(origem, base));
            FileUtils.copyFile(origem, destinoArquivo);
            if (apagar) origem.delete();
            return;
        }
        if (origem.isDirectory() && origem.exists()) origem = new File(origem, "/**/*");
        PathPattern pp = new PathPattern(origem.getAbsolutePath());
        Enumeration e = pp.enumerateFiles();
        while (e.hasMoreElements()) {
            File origemArquivo = (File) e.nextElement();
            if (!accept(origemArquivo, filtros)) continue;
            File destinoArquivo = new File(destino, getCaminhoRelativo(origemArquivo, base));
            try {
                FileUtils.copyFile(origemArquivo, destinoArquivo);
                if (apagar) origemArquivo.delete();
            } catch (IOException e1) {
                if (pararError) throw e1;
            }
        }
    }
