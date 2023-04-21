    public static void main(String[] args) {
        System.out.println("This file is currently unsupported - Log Buffer has not been integrated.");
        System.exit(0);
        output_path = args[0];
        if (output_path.charAt(output_path.length() - 1) != '/') {
            output_path += '/';
        }
        String[] files = new String[args.length - 1];
        for (int i = 0; i < files.length; i++) {
            files[i] = args[i + 1];
        }
        float tmp_percent = 0;
        for (String file : files) {
            SnpIterator SI = new SnpIterator(LB, file);
            System.out.println("Processing file: " + file);
            try {
                for (SNPLine snp : new IterableIterator<SNPLine>(SI)) {
                    tmp_percent = ((float) snp.get_observed() / (float) snp.get_coverage()) * 100;
                    if (snp.get_type().equalsIgnoreCase("known")) {
                        KnownSNPsP.bin_value(tmp_percent);
                        KnownSNPsC.bin_value(snp.get_observed());
                    } else {
                        UnknownSNPsP.bin_value(tmp_percent);
                        UnknownSNPsC.bin_value(snp.get_observed());
                    }
                    AllSNPsP.bin_value(tmp_percent);
                    AllSNPsC.bin_value(snp.get_observed());
                }
            } catch (NoSuchElementException nsee) {
                continue;
            }
        }
        write_SNP_report();
    }
