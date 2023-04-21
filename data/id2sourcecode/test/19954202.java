    public void output_per_iteration(int iterations, FileOut fo) {
        fo.writeln("");
        fo.writeln("*******************************************");
        fo.writeln("Summary Statistics - Average per Iteration");
        fo.writeln("*******************************************");
        fo.writeln("Num. peaks          " + Utilities.FormatNumberForPrinting(this.get_reads_used() / iterations, FIXED_WIDTH_FIELD));
        fo.writeln("Sum of Peak Heights " + Utilities.FormatNumberForPrinting(this.sum_peak_heights / iterations, FIXED_WIDTH_FIELD));
        fo.writeln("");
    }
