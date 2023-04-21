    private boolean read_genome(BufferedReader reader, BufferedWriter writer) throws Exception {
        char[] biallele = new char[2];
        BufferedReader mapReader = null;
        int allelesNumInPed = 0;
        File file = new File(mapfile);
        if (!file.exists() || !file.canRead()) {
            System.out.println("Cannot read mapping (snips) file " + mapfile);
            System.out.println();
            return false;
        }
        try {
            mapReader = new BufferedReader(new FileReader(file));
            while (true) {
                if (!read_biallele(reader, biallele, allelesNumInPed)) {
                    break;
                }
                allelesNumInPed++;
                rec = new Record();
                if (!read_mapping_line(mapReader, snipsLineNum)) {
                    System.out.println("WARN: Line " + lineNum + ": Number of valid alleles in SNP map (" + snipsLineNum + " is in total) is less than number of alleles in PED file (" + allelesNumInPed + ")");
                    return false;
                }
                writer.write(rec.rsid);
                writer.write(OUTPUT_DELIMITER);
                writer.write(rec.chromo);
                writer.write(OUTPUT_DELIMITER);
                writer.write(rec.position);
                writer.write(OUTPUT_DELIMITER);
                writer.write(biallele);
                writer.write(LINE_SEPARATOR_IN_OUTPUT);
            }
            if (read_mapping_line(mapReader, snipsLineNum)) {
                System.out.println("WARN: Line " + lineNum + ": PED is incomplete, number of valid records in SNP map (" + snipsLineNum + " is in total) is bigger than number of alleles in PED file (" + allelesNumInPed + ")");
                return false;
            }
            return true;
        } finally {
            if (mapReader != null) try {
                mapReader.close();
            } catch (Exception e) {
            }
        }
    }
