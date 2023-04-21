    public void do_proofstep(Collection<Rule> rules) throws ImplementationException, ProverException {
        grape.status(FrontendStatus.READING_CHOICES);
        Collection<Rewrite> choices = grape.read_choices(rules);
        grape.status(FrontendStatus.CHOOSE_NEXT_STEP);
        if (choices != null) {
            Rewrite deflt = null;
            for (Rewrite r : choices) {
                deflt = r;
                original_selection = current_selection;
                break;
            }
            if (choices.size() == 1) {
                choose(deflt, original_selection);
            } else {
                grape.init_step_chooser(choices, deflt);
            }
        }
        current_selection = new Selection(derivation);
        grape.status(FrontendStatus.PROVING);
    }
