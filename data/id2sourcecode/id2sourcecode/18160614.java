    private static void process_exons(List<Transcript> list) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(output_path + ens.get_chromosome(current_chromosome) + ".exons"));
        } catch (IOException io) {
            io.printStackTrace();
            LB.error("Can't create file : " + output_path + ens.get_chromosome(current_chromosome) + ".exons/psnps");
            LB.error("Message thrown by Java environment (may be null):" + io.getMessage());
            LB.die();
        }
        assert (bw != null);
        for (Transcript t : list) {
            Location tl = t.getLocation();
            String Accession = t.getAccessionID();
            String Display = t.getDisplayName();
            if (t.getCDNALocation().getEnd() > Chr.get_canonical_sequence_length()) {
                continue;
            }
            tl.getStart();
            int strand = tl.getStrand();
            int read_transcript = Chr.get_starts_and_ends(tl.getStart(), tl.getEnd());
            int read_trans_exons = 0;
            ArrayList<SNP> trans_snps = new ArrayList<SNP>(100);
            @SuppressWarnings("unchecked") List<Exon> exon_list = t.getExons();
            for (Exon exon : exon_list) {
                Location el = exon.getLocation();
                int exon_start = el.getStart();
                int exon_end = el.getEnd();
                if (exon_start > exon_end) {
                    LB.warning("Start greater than end!");
                    LB.warning("Strand: " + strand);
                }
                float exon_avg_coverage = Chr.get_coverage_sum(exon_start, exon_end);
                float exon_coverage = Chr.get_coverage(exon_start, exon_end);
                int starts = Chr.get_starts_and_ends(exon_start, exon_end);
                try {
                    bw.write(Accession + "\t " + Display + "-" + exon_start + "-" + exon_end + "\t" + starts + "\t" + Utilities.DecimalPoints(exon_avg_coverage, 2) + "\t" + Utilities.DecimalPoints(exon_coverage * Constants.PERCENT_100, 2));
                    bw.newLine();
                } catch (IOException io) {
                    LB.error("Can't create file : " + output_path + ens.get_chromosome(current_chromosome) + ".exons");
                    LB.error("Message thrown by Java environment (may be null):" + io.getMessage());
                    LB.die();
                }
                read_trans_exons += starts;
            }
            exon_list.clear();
            trans_snps.clear();
            try {
                bw.write(Accession + "\t " + Display + "-exons\t" + read_trans_exons);
                bw.newLine();
                bw.write(Accession + "\t " + Display + "-trans\t" + read_transcript);
                bw.newLine();
            } catch (IOException io) {
                LB.error("Can't write to : " + output_path + ens.get_chromosome(current_chromosome) + ".exons");
                LB.error("Message thrown by Java environment (may be null):" + io.getMessage());
                LB.die();
            }
        }
        try {
            bw.close();
        } catch (IOException io) {
            LB.error("Can't close file : " + output_path + ens.get_chromosome(current_chromosome) + ".exons");
            LB.error("Message thrown by Java environment (may be null):" + io.getMessage());
            LB.die();
        }
    }
