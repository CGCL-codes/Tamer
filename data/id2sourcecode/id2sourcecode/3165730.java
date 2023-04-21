    void digest() {
        for (Plasmid p : this.plasmids) {
            message("Digest " + p.getName());
            if (canceled()) break;
            p.digest(this.rebase, new EnzymeSelector() {

                @Override
                public boolean accept(Enzyme e) {
                    return e.getWeight() > 5f;
                }
            });
            message(p.getName() + " " + p.getSites().getSiteCount());
        }
    }
