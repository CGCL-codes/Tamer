    public Collection<Rewrite> read_choices(Collection<Rule> rules) throws ProverException {
        Collection<Rewrite> choices;
        JFrame frame = theView.getFrame();
        try {
            choices = theModel.findRewrites(rules);
        } catch (ProverException pe) {
            ExceptionDialog.showProverError(frame, "Prover Backend Error", pe.getMessage());
            return null;
        }
        status(FrontendStatus.PROVING);
        if (choices.size() == 0) {
            JOptionPane.showMessageDialog(frame, "No inference step or transformation possible.", "Oops...", JOptionPane.ERROR_MESSAGE);
            return null;
        } else {
            return choices;
        }
    }
