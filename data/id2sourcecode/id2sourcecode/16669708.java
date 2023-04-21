    int parseURL(URL url) {
        DataInputStream in;
        InputStream ins;
        int ret;
        dflag = true;
        try {
            ins = url.openStream();
            st = new StreamTokenizer(ins);
            st.slashSlashComments(true);
            st.slashStarComments(true);
        } catch (Exception e) {
            yyerror("could not open " + url.toString());
            return 0;
        }
        ret = yyparse();
        try {
            ins.close();
        } catch (Exception e) {
            yyerror("could not open source data");
            return 0;
        }
        return ret;
    }
