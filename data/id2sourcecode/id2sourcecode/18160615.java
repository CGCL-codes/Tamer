    private static void version_headers() {
        LB.notice("Libraries in Use.  Please provide these IDs for debugging:");
        LB.notice("    Core Libraries:");
        LB.notice("\t" + Chromosome.SVNID);
        LB.notice("\t" + SNP.SVNID);
        LB.notice("    Libraries:");
        LB.notice("\t" + AlignedRead.SVNID);
        LB.notice("\t" + Ensembl.SVNID);
        LB.notice("\t" + Utilities.SVNID);
        LB.notice("    File writer/reader Libraries:");
        LB.notice("\t" + ElandIterator.SVNID);
        LB.notice("\t" + FastaIterator.SVNID);
    }
