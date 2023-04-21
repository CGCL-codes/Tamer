    public void process_contig_and_reads(Vector<AlignedRead> aligned_reads, String fasta_sequence, String contig_name, int start_contig) {
        try {
            bw.write("AS 1 " + (aligned_reads.size() + 1));
            bw.newLine();
            bw.write("CO " + contig_name + " " + fasta_sequence.length() + " " + (aligned_reads.size() + 1) + " 1 U");
            bw.newLine();
            bw.write(split_in_lines(fasta_sequence, 9));
            bw.newLine();
            bw.write("BQ");
            bw.newLine();
            bw.write(dummy_quality_value(30, 20, fasta_sequence.length()));
            bw.newLine();
            StringBuffer rd_buff = new StringBuffer();
            for (AlignedRead read : aligned_reads) {
                char sense = 'U';
                String read_sequence = read.get_sequence();
                if (read.get_direction() == '-') {
                    sense = 'C';
                }
                bw.write("AF " + read.get_name() + " " + sense + " " + (read.get_alignStart() - start_contig + 1));
                bw.newLine();
                rd_buff.append("RD " + read.get_name() + " " + read_sequence.length() + " 0 0\n" + read_sequence + "\n\n");
                rd_buff.append("QA 1 " + read_sequence.length() + " 1 " + read_sequence.length() + "\n");
                rd_buff.append("DS PHD_FILE: x\n");
            }
            bw.write("AF CONTIG_" + contig_name + " U 1");
            bw.newLine();
            rd_buff.append("RD CONTIG_" + contig_name + " " + fasta_sequence.length() + " 0 0\n" + split_in_lines(fasta_sequence, 60) + "\n\n");
            rd_buff.append("QA 1 " + fasta_sequence.length() + " 1 " + fasta_sequence.length() + "\n");
            rd_buff.append("DS PHD_FILE: x\n");
            bw.write("BS 1 " + fasta_sequence.length() + " CONTIG_" + contig_name);
            bw.newLine();
            bw.newLine();
            bw.write(rd_buff.toString());
            bw.flush();
            bw.close();
        } catch (FileNotFoundException fnf) {
            LB.error(fnf.getMessage());
            LB.die();
        } catch (IOException ioe) {
            LB.error(ioe.getMessage());
            LB.die();
        }
        LB.notice("Output to: " + file_name);
    }
