    private void morphVolume(Part into1, Part into2, int vol1, int vol2) {
        double mtp = this.mtracker.getPos();
        if (into1.getChannel() == into2.getChannel()) {
            if (this.morphSVol.getSelectedItem().equals("cross") || this.morphSVol.getSelectedItem().equals("early")) {
                into1.setVolume((int) (vol1 * (1 - mtp)) + (int) (vol2 * (mtp)));
            } else if (this.morphSVol.getSelectedItem().equals("constant")) {
                into1.setVolume((int) ((vol1 + vol2) / 2));
            }
            into2.setVolume(into1.getVolume());
        } else {
            if (this.morphSVol.getSelectedItem().equals("cross")) {
                into1.setVolume((int) (vol1 * (1 - mtp)));
                into2.setVolume((int) (vol2 * (mtp)));
            } else if (this.morphSVol.getSelectedItem().equals("early")) {
                if (mtp < 0.5) {
                    into1.setVolume(vol1);
                    into2.setVolume((int) (vol2 * (mtp * 2)));
                } else {
                    into1.setVolume((int) (vol1 * ((1 - mtp) * 2)));
                    into2.setVolume(vol2);
                }
            } else if (this.morphSVol.getSelectedItem().equals("constant")) {
                into1.setVolume((int) ((vol1 + vol2) / 2));
                into2.setVolume(into1.getVolume());
            } else if (this.morphSVol.getSelectedItem().equals("exp")) {
            }
        }
    }
