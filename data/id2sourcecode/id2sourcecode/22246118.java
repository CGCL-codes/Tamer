    public void getResult(final OutputStream os, final boolean dat, final float ignoreIonsScoreBelow, final float significanceThreshold) throws IOException {
        HttpURLConnection http = null;
        try {
            final StringBuffer url = new StringBuffer();
            if (dat) {
                url.append(params.getServer() + "/x-cgi/ms-status.exe?Show=RESULTFILE&DateDir=" + date + "&ResJob=" + mascotFilename);
            } else {
                url.append(params.getServer() + "/cgi/export_dat_2.pl?");
                url.append(buildString("file", "../data/" + date + "/" + mascotFilename));
                url.append(buildString("do_export", "1"));
                url.append(buildString("prot_hit_num", "1"));
                url.append(buildString("prot_acc", "1"));
                url.append(buildString("pep_query", "1"));
                url.append(buildString("pep_rank", "1"));
                url.append(buildString("pep_isbold", "1"));
                url.append(buildString("pep_exp_mz", "1"));
                url.append(buildString("_showallfromerrortolerant", ""));
                url.append(buildString("_onlyerrortolerant", ""));
                url.append(buildString("_noerrortolerant", ""));
                url.append(buildString("_show_decoy_report", ""));
                url.append(buildString("export_format", "XML"));
                url.append(buildString("_sigthreshold", Float.valueOf(significanceThreshold)));
                url.append(buildString("REPORT", "AUTO"));
                url.append(buildString("_server_mudpit_switch", Double.valueOf((params.isMudPit()) ? 0.000000001 : 99999999)));
                url.append(buildString("_ignoreionsscorebelow", Float.valueOf(ignoreIonsScoreBelow)));
                url.append(buildString("_showsubsets", "1"));
                url.append(buildString("search_master", "1"));
                url.append(buildString("show_header", "1"));
                url.append(buildString("show_mods", "1"));
                url.append(buildString("show_params", "1"));
                url.append(buildString("show_format", "1"));
                url.append(buildString("show_masses", "1"));
                url.append(buildString("protein_master", "1"));
                url.append(buildString("prot_score", "1"));
                url.append(buildString("prot_desc", "1"));
                url.append(buildString("prot_mass", "1"));
                url.append(buildString("prot_matches", "1"));
                url.append(buildString("peptide_master", "1"));
                url.append(buildString("pep_exp_mr", "1"));
                url.append(buildString("pep_exp_z", "1"));
                url.append(buildString("pep_calc_mr", "1"));
                url.append(buildString("pep_delta", "1"));
                url.append(buildString("pep_start", "1"));
                url.append(buildString("pep_end", "1"));
                url.append(buildString("pep_miss", "1"));
                url.append(buildString("pep_score", "1"));
                url.append(buildString("pep_expect", "1"));
                url.append(buildString("pep_seq", "1"));
                url.append(buildString("pep_var_mod", "1"));
                url.append(buildString("pep_scan_title", "1"));
                url.append(buildString("query_master", "1"));
                url.append(buildString("query_title", "1"));
                url.append(buildString("query_qualifiers", "1"));
                url.append(buildString("query_params", "1"));
                url.append(buildString("query_peaks", "1"));
                url.append(buildString("query_raw", "1"));
                url.append(buildString("show_same_sets", "1"));
            }
            http = (HttpURLConnection) new URL(url.toString()).openConnection();
            http.setRequestMethod("GET");
            new StreamReader(http.getInputStream(), new BufferedOutputStream(os)).read();
        } finally {
            if (http != null) {
                http.disconnect();
            }
        }
    }
