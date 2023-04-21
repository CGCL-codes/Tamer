    private static String writeFeatureSet(String indent, Transcript trans, boolean isa_gene, CurationSet curation) {
        StringBuffer buf = new StringBuffer();
        buf.append(indent + "<feature_set");
        if (trans.getId() != null && trans.getId().length() > 0) buf.append(writeID(trans.getId(), "feature_set"));
        AbstractSequence cDNA;
        cDNA = (AbstractSequence) trans.get_cDNASequence();
        if (cDNA != null && cDNA.getResidues() != null && cDNA.getResidues().length() > 0 && cDNA.getAccessionNo() != null) {
            buf.append(" produces_seq=\"" + cDNA.getAccessionNo() + "\"");
        }
        if (trans.isProblematic()) buf.append(" problem=\"" + trans.isProblematic() + "\"");
        buf.append(">\n");
        buf.append(writeName(trans.getName(), "", indent));
        buf.append(indent + "  <type>" + trans.getFeatureType() + "</type>\n");
        buf.append(writeDescription(indent + "  ", trans.getDescription()));
        if (trans.getOwner() != null) {
            buf.append(indent + "  <author>" + trans.getOwner() + "</author>\n");
        }
        buf.append(writeDate(indent + "  ", trans));
        buf.append(writeSynonyms(indent + "  ", trans.getSynonyms()));
        Vector xrefs = trans.getDbXrefs();
        for (int i = 0; i < xrefs.size(); i++) {
            DbXref xref = (DbXref) xrefs.elementAt(i);
            buf.append(writeXref(indent + indent, xref.getDbName(), xref.getIdValue(), xref.getIdType()));
        }
        Vector comments = trans.getComments();
        for (int i = 0; i < comments.size(); i++) {
            Comment comment = (Comment) comments.elementAt(i);
            buf.append(writeComment("      ", comment));
        }
        buf.append(writeProperties(indent + "  ", trans, "property"));
        if (isa_gene) {
            if (trans.hasReadThroughStop()) {
                if (trans.readThroughStopResidue().equals("X")) writeProperty(indent + " ", "readthrough_stop_codon", "true", buf); else writeProperty(indent + " ", "readthrough_stop_codon", trans.readThroughStopResidue(), buf);
            }
            if (trans.plus1FrameShiftPosition() > 0) {
                writeProperty(indent + "  ", "plus_1_translational_frame_shift", trans.plus1FrameShiftPosition() + "", buf);
            }
            if (trans.minus1FrameShiftPosition() > 0) {
                writeProperty(indent + "  ", "minus_1_translational_frame_shift", trans.minus1FrameShiftPosition() + "", buf);
            }
            if (gameVersion >= 1.1) {
                buf.append(writeSpan(indent + "  ", trans.getProteinFeat(), true, curation, true));
            } else {
                buf.append(writeTSS(indent + "  ", trans, "feature_span", curation));
            }
            if (trans.unConventionalStart()) {
                writeProperty(indent + "  ", "non_canonical_start_codon", trans.getStartCodon(), buf);
            }
            if (trans.isMissing5prime()) {
                writeProperty(indent + "  ", "missing_start_codon", "true", buf);
            }
            if (trans.isMissing3prime()) {
                writeProperty(indent + "  ", "missing_stop_codon", "true", buf);
            }
            if ((trans.getNonConsensusAcceptorNum() >= 0 || trans.getNonConsensusDonorNum() >= 0)) {
                writeProperty(indent + "  ", "non_canonical_splice_site", (trans.nonConsensusSplicingOkay() ? "approved" : "unapproved"), buf);
            }
        }
        Vector exons = trans.getFeatures();
        for (int i = 0; i < exons.size(); i++) {
            Exon exon = (Exon) exons.elementAt(i);
            buf.append(writeSpan(indent + "  ", exon, isa_gene, curation));
        }
        if ((cDNA != null) && cDNA.getAccessionNo() != null) {
            loadedSeqs.add(cDNA.getAccessionNo());
            buf.append(writeSequence(cDNA, indent + "  ", "", "cdna", true));
        }
        if (trans.isProteinCodingGene()) {
            AbstractSequence peptide = (AbstractSequence) trans.getPeptideSequence();
            if (peptide != null) {
                if (peptide.getAccessionNo() != null) {
                    loadedSeqs.add(peptide.getAccessionNo());
                }
            }
        }
        buf.append(indent + "</feature_set>\n");
        return buf.toString();
    }
