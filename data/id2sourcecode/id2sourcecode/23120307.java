    public static void output_pkstats(PeakStats[] sim_stats, PeakStats exp_stats, FileOut fo) {
        int sets = sim_stats.length;
        double avg_tags = 0;
        double sd_avg_tags = 0;
        double avg_coverage = 0;
        double sd_avg_coverage = 0;
        double avg_peak_count = 0;
        double sd_avg_peak_count = 0;
        double avg_tallest = 0;
        double sd_avg_tallest = 0;
        double avg_peak_sum = 0;
        double sd_avg_peak_sum = 0;
        for (int i = 0; i < sets; i++) {
            avg_tags += sim_stats[i].get_total_tags();
            avg_coverage += sim_stats[i].get_coverage();
            avg_peak_count += sim_stats[i].get_number_of_peaks();
            avg_tallest += sim_stats[i].get_tallest();
            avg_peak_sum += sim_stats[i].get_sum_of_peaks();
        }
        avg_tags /= sets;
        avg_coverage /= sets;
        avg_peak_count /= sets;
        avg_tallest /= sets;
        avg_peak_sum /= sets;
        for (int i = 0; i < sets; i++) {
            double tmp = (sim_stats[i].get_total_tags() - avg_tags);
            sd_avg_tags += (tmp * tmp);
            tmp = (sim_stats[i].get_coverage() - avg_coverage);
            sd_avg_coverage += (tmp * tmp);
            tmp = (sim_stats[i].get_number_of_peaks() - avg_peak_count);
            sd_avg_peak_count += (tmp * tmp);
            tmp = (sim_stats[i].get_tallest() - avg_tallest);
            sd_avg_tallest += (tmp * tmp);
            tmp = (sim_stats[i].get_sum_of_peaks() - avg_peak_sum);
            sd_avg_peak_sum += (tmp * tmp);
        }
        sd_avg_tags /= sets;
        sd_avg_tags = Math.sqrt(sd_avg_tags);
        sd_avg_coverage /= sets;
        sd_avg_coverage = Math.sqrt(sd_avg_coverage);
        sd_avg_peak_count /= sets;
        sd_avg_peak_count = Math.sqrt(sd_avg_peak_count);
        sd_avg_tallest /= sets;
        sd_avg_tallest = Math.sqrt(sd_avg_tallest);
        sd_avg_peak_sum /= sets;
        sd_avg_peak_sum = Math.sqrt(sd_avg_peak_sum);
        fo.writeln("------------------------------------------------------------------------");
        fo.writeln("                        experiment        sim. average         std.dev.");
        fo.writeln("------------------------------------------------------------------------");
        fo.writeln("Tags used:            " + Utilities.FormatNumberForPrinting(exp_stats.get_total_tags(), FPConstants.FIELD_WIDTH_1) + " " + Utilities.FormatNumberForPrinting((int) avg_tags, FPConstants.FIELD_WIDTH_2) + " " + Utilities.FormatNumberForPrinting(sd_avg_tags, FPConstants.FIELD_WIDTH_3));
        fo.writeln("Coverage:             " + Utilities.FormatNumberForPrinting(exp_stats.get_coverage(), FPConstants.FIELD_WIDTH_1) + " " + Utilities.FormatNumberForPrinting((int) avg_coverage, FPConstants.FIELD_WIDTH_2) + " " + Utilities.FormatNumberForPrinting(sd_avg_coverage, FPConstants.FIELD_WIDTH_3));
        fo.writeln("Peaks count :         " + Utilities.FormatNumberForPrinting(exp_stats.get_number_of_peaks(), FPConstants.FIELD_WIDTH_1) + " " + Utilities.FormatNumberForPrinting((int) avg_peak_count, FPConstants.FIELD_WIDTH_2) + " " + Utilities.FormatNumberForPrinting(sd_avg_peak_count, FPConstants.FIELD_WIDTH_3));
        fo.writeln("Tallest :             " + Utilities.FormatNumberForPrinting(exp_stats.get_tallest(), FPConstants.FIELD_WIDTH_1) + " " + Utilities.FormatNumberForPrinting(avg_tallest, FPConstants.FIELD_WIDTH_2) + " " + Utilities.FormatNumberForPrinting(sd_avg_tallest, FPConstants.FIELD_WIDTH_3));
        fo.writeln("Sum of peak heights : " + Utilities.FormatNumberForPrinting(exp_stats.get_sum_of_peaks(), FPConstants.FIELD_WIDTH_1) + " " + Utilities.FormatNumberForPrinting(avg_peak_sum, FPConstants.FIELD_WIDTH_2) + " " + Utilities.FormatNumberForPrinting(sd_avg_peak_sum, FPConstants.FIELD_WIDTH_3));
        fo.writeln("Average peak height : " + Utilities.FormatNumberForPrinting((exp_stats.get_sum_of_peaks() / exp_stats.get_number_of_peaks()), FPConstants.FIELD_WIDTH_1) + " " + Utilities.FormatNumberForPrinting(avg_peak_sum / avg_peak_count, FPConstants.FIELD_WIDTH_2));
        fo.writeln("------------------------------------------------------------------------");
        fo.writeln("Estimaged Signal to noise ratio :  " + Utilities.FormatNumberForPrinting(exp_stats.get_total_tags() - avg_tags, 0) + " : " + Utilities.FormatNumberForPrinting(avg_tags, 0));
        fo.writeln("Enrichment of :                    " + Utilities.DecimalPoints(((exp_stats.get_total_tags() - avg_tags) / avg_tags), 2));
        fo.writeln("Signal in percent of reads:        " + Utilities.DecimalPoints((((float) exp_stats.get_total_tags() - avg_tags) / exp_stats.get_total_tags()) * FPConstants.PERCENTAGE, 2) + "%");
        fo.writeln("");
    }
