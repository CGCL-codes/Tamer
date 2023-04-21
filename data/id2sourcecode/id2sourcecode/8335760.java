    public static void process_exons(Log_Buffer LB, Ensembl Const, Chromosome Chr, List<Transcript> list, int current_chromosome, String input_species, String output_path, float min_percent, int min_observed) {
        BufferedWriter exonfile = null;
        BufferedWriter psnpfile = null;
        String psnpfile_name = output_path + Const.get_chromosome(current_chromosome) + ".psnps";
        String exonfile_name = output_path + Const.get_chromosome(current_chromosome) + ".exons";
        String chr_name = Const.get_chromosome(current_chromosome);
        try {
            exonfile = new BufferedWriter(new FileWriter(exonfile_name));
            psnpfile = new BufferedWriter(new FileWriter(psnpfile_name));
        } catch (IOException io) {
            LB.error("Can't create files : ");
            LB.error("\t" + psnpfile_name);
            LB.error("\t" + exonfile_name);
            LB.error("Message thrown by Java environment (may be null):" + io.getMessage());
            LB.die();
        }
        assert (psnpfile != null);
        assert (exonfile != null);
        for (Transcript t : list) {
            Location tl = t.getLocation();
            String Accession = t.getAccessionID();
            String Display = t.getDisplayName();
            StringBuffer NewTranscript = new StringBuffer();
            Translation t_translation = t.getTranslation();
            if (t.getCDNALocation().getEnd() > Chr.get_canonical_sequence_length()) {
                continue;
            }
            tl.getStart();
            int strand = tl.getStrand();
            int read_transcript = Chr.get_starts_and_ends(tl.getStart(), tl.getEnd());
            int read_trans_exons = 0;
            ArrayList<SNP> trans_snps = new ArrayList<SNP>();
            @SuppressWarnings("unchecked") List<Exon> exon_list = t.getExons();
            for (Exon exon : exon_list) {
                Location el = exon.getLocation();
                int exon_start = el.getStart();
                int exon_end = el.getEnd();
                if (exon_start > exon_end) {
                    LB.warning("Start greater than end!");
                    LB.warning("Strand: " + strand);
                }
                Sequence exon_seq = exon.getSequence();
                String exon_baseSeq = exon_seq.getString();
                float exon_avg_coverage = Chr.get_coverage_sum(exon_start, exon_end);
                float exon_coverage = Chr.get_coverage(exon_start, exon_end);
                int starts = Chr.get_starts_and_ends(exon_start, exon_end);
                try {
                    exonfile.write(Accession + "\t " + Display + "-" + exon_start + "-" + exon_end + "\t" + starts + "\t" + Utilities.DecimalPoints(exon_avg_coverage, 2) + "\t" + Utilities.DecimalPoints(exon_coverage * Constants.PERCENT_100, 2));
                    exonfile.newLine();
                } catch (IOException io) {
                    LB.error("Can't create file : " + output_path + Const.get_chromosome(current_chromosome) + ".exons");
                    LB.error("Message thrown by Java environment (may be null):" + io.getMessage());
                    LB.die();
                }
                read_trans_exons += starts;
                if (t_translation != null) {
                    ArrayList<SNP> VSNP = new ArrayList<SNP>();
                    VSNP = Chr.get_local_SNPs(exon_start, exon_end, min_percent, min_observed);
                    for (int y = 0; y < VSNP.size(); y++) {
                        SNP sp = VSNP.get(y);
                        if (strand == -1) {
                            sp.set_misc_value(exon_end - sp.get_position() + NewTranscript.length());
                        } else {
                            sp.set_misc_value(sp.get_position() - exon_start + NewTranscript.length());
                        }
                        VSNP.set(y, sp);
                    }
                    trans_snps.addAll(VSNP);
                    VSNP.clear();
                }
                NewTranscript.append(exon_baseSeq);
            }
            process_coding_exon(LB, psnpfile, t_translation, input_species, NewTranscript, trans_snps, chr_name, psnpfile_name, Accession, Display, strand);
            exon_list.clear();
            trans_snps.clear();
            try {
                exonfile.write(Accession + "\t " + Display + "-exons\t" + read_trans_exons);
                exonfile.newLine();
                exonfile.write(Accession + "\t " + Display + "-trans\t" + read_transcript);
                exonfile.newLine();
            } catch (IOException io) {
                LB.warning("Can't write to : " + output_path + Const.get_chromosome(current_chromosome) + ".exons");
                LB.warning("Message thrown by Java environment (may be null):" + io.getMessage());
            }
        }
        try {
            exonfile.close();
            psnpfile.close();
        } catch (IOException io) {
            LB.error("Can't close file : " + output_path + Const.get_chromosome(current_chromosome) + ".exons");
            LB.error("Message thrown by Java environment (may be null):" + io.getMessage());
            LB.die();
        }
    }
