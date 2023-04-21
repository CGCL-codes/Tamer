    private void start() throws IOException {
        final HomologousReader hr = new HomologousReader();
        final Hashtable homologous = hr.start(homologousDir);
        final String entries[] = new File(inputDir).list();
        Reader reader = new Reader();
        Reader read_tmp = null;
        for (final String entry : entries) {
            System.out.println("Read now " + entry);
            final String dirName = inputDir + entry + File.separator;
            read_tmp = read(dirName);
            reader.getNucleotides().putAll(read_tmp.getNucleotides());
            reader.getBindingMotifsForGenes().putAll(read_tmp.getBindingMotifsForGenes());
            reader.getGenes().putAll(read_tmp.getGenes());
            reader.getOperons().putAll(read_tmp.getOperons());
            reader.getOrganisms().putAll(read_tmp.getOrganisms());
            reader.getProteins().putAll(read_tmp.getProteins());
            reader.getRegulations().putAll(read_tmp.getRegulations());
            reader.getRegulators().putAll(read_tmp.getRegulators());
            reader.getRegulatorTypes().putAll(read_tmp.getRegulatorTypes());
        }
        write(reader, possumDir, homologous, s);
    }
