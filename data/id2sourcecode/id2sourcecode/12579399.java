    public void writeFastaFile(List<Protein> proteins, String filePath) throws IOException, FastaFileWritingException {
        BufferedWriter writer = null;
        try {
            final File file = new File(filePath);
            if (file.exists()) {
                if (!file.delete()) {
                    throw new IllegalStateException("Unable to delete the old fasta file at path " + file.getAbsolutePath());
                }
            }
            if (!file.createNewFile()) {
                throw new IllegalStateException("Unable to create new fasta file at " + file.getAbsolutePath());
            }
            writer = new BufferedWriter(new FileWriter(file));
            for (Protein protein : proteins) {
                String seq = protein.getSequence();
                if (deviatesFromAlphabet(seq)) {
                    if (residueSubstitutions != null) {
                        for (String from : residueSubstitutions.keySet()) {
                            seq = seq.replaceAll(from, residueSubstitutions.get(from));
                        }
                    }
                    if (deviatesFromAlphabet(seq)) {
                        continue;
                    }
                }
                if (protein.getId() == null) {
                    throw new FastaFileWritingException("The FastaFileWriter class can only write out Protein objects that have already been persisted to the database as it uses the database primary key as the protein ID in the fasta file.", filePath);
                }
                FastaEntryWriter.writeFastaFileEntry(writer, protein.getId().toString(), seq, sequenceLineLength);
            }
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
