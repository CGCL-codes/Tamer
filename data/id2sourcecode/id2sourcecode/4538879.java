    private void updateProgramCombo(boolean playerRunning) {
        if (!isDisposed() && getChannel() != null) {
            List programNames = getProgramNames();
            if (!(this.programCombo.getData() instanceof List) || isDifferentList(programNames, (List) this.programCombo.getData())) {
                this.programCombo.removeAll();
                this.programCombo.setData(programNames);
                for (int i = 0; i < programNames.size(); i++) {
                    this.programCombo.add((String) programNames.get(i));
                }
            }
            if (getChannel().getProgram() >= 0 && getChannel().getProgram() < this.programCombo.getItemCount()) {
                this.programCombo.select(getChannel().getProgram());
            }
        }
    }
