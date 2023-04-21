    private static void copyFile(File origem, File destino, String base, boolean pararError, boolean apagar, boolean debug, IFilterCopy... filtros) throws IOException {
        String relativo = getCaminhoRelativo(origem, base);
        File arquivoDestino = new File(destino, relativo);
        if (origem.isDirectory()) {
            if (filtros != null) for (IFilterCopy filtro : filtros) if (!filtro.copiar(origem, relativo)) return;
            for (File arquivo : origem.listFiles()) {
                copyFile(arquivo, destino, base, pararError, apagar, debug, filtros);
            }
            if (apagar) {
                if (origem.list().length == 0) FileUtils.deleteDirectory(origem);
            }
        } else {
            if (filtros != null) for (IFilterCopy filtro : filtros) if (!filtro.copiar(origem, arquivoDestino, relativo)) return;
            try {
                if (debug) System.out.println("Copiando arquivo: " + origem.getAbsolutePath());
                FileUtils.copyFile(origem, arquivoDestino);
                if (apagar) origem.delete();
            } catch (IOException e) {
                if (pararError) throw e;
                System.out.println("N�o foi poss�vel copiar o arquivo: " + origem.getAbsolutePath());
            }
        }
    }
