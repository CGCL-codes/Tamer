    public RubarSolutionSequentielReader(final File _f, final RubarSolutionSequentielResult _r) throws IOException {
        ch_ = new FileInputStream(_f).getChannel();
        nbVar_ = _r.nbVar_;
        timeLength_ = _r.timeLength_;
        valueLineLength_ = _r.resultLength_;
        nbLigneByValues_ = _r.nbLigne_;
        valueLastLineLength_ = _r.resultLastLineLength_;
        nbElt_ = _r.nbElt_;
        fmt_ = new int[8];
        Arrays.fill(fmt_, 10);
    }
