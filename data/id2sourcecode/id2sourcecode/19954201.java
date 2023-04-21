    public void output(boolean filterDupes, float genome_size, FileOut fo) {
        double lambda = (this.get_coverage() / genome_size);
        fo.writeln("****************************");
        fo.writeln("Summary Statistics - Reads");
        fo.writeln("****************************");
        fo.writeln("Total Tags                    " + Utilities.FormatNumberForPrinting(this.get_total_tags(), FIXED_WIDTH_FIELD));
        fo.writeln("Actual Peak Coverage, bases   " + Utilities.FormatNumberForPrinting(this.get_coverage(), FIXED_WIDTH_FIELD));
        fo.writeln("Actual Peak Coverage, percent " + Utilities.DecimalPoints(((float) this.get_coverage() * PERCENTATGE) / genome_size, 3) + "%");
        if (filterDupes) {
            fo.writeln("Unique Tags (used)            " + Utilities.FormatNumberForPrinting(pstore.get_reads_used(), FIXED_WIDTH_FIELD));
            fo.writeln("Duplicate tags in genome      " + Utilities.FormatNumberForPrinting(pstore.get_reads_filtered(), FIXED_WIDTH_FIELD) + " (" + Utilities.DecimalPoints((((float) this.get_tags_filtered() / (float) (this.get_reads_used())) * PERCENTATGE), 3) + "%)*");
        }
        fo.writeln("");
        fo.writeln("****************************");
        fo.writeln("Summary Statistics - Peaks");
        fo.writeln("****************************");
        fo.writeln("Num. peaks          " + Utilities.FormatNumberForPrinting(this.get_number_of_peaks(), FIXED_WIDTH_FIELD));
        fo.writeln("Tallest             " + Utilities.DecimalPoints(this.tallest, 1));
        fo.writeln("Lambda              " + Utilities.DecimalPoints(lambda, 4));
        fo.writeln("Sum of Peak Heights " + Utilities.FormatNumberForPrinting(this.sum_peak_heights, FIXED_WIDTH_FIELD));
        fo.writeln("avg peak height     " + Utilities.FormatNumberForPrinting(this.sum_peak_heights / (float) this.get_number_of_peaks(), FIXED_WIDTH_FIELD));
        fo.writeln("");
    }
