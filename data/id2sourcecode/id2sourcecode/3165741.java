    public void doMenuCloneIt() {
        SubCloning prog = new SubCloning();
        for (int i = 0; i < 2; ++i) {
            prog.plasmids[i].setSequence(this.seqArea[i].getText());
            prog.plasmids[i].setName(this.seqName[i].getText().trim());
            prog.plasmids[i].digest(getEnzymeList(), new EnzymeSelector() {

                @Override
                public boolean accept(Enzyme e) {
                    return true;
                }
            });
        }
        if (!fillInsertData(prog.insert)) return;
        Thread t = new Thread(prog);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
        }
    }
