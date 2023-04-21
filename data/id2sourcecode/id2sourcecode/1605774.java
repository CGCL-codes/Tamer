    private void makeMorphResult(double position, TickEvent e) {
        misto = transformStrucIndex(position);
        morphResult = ((MorpherRT) this.morphStruc()).morphRT(this.fromTo, this.lpmorph, misto, e, ((LPlayer) e.getSource()).getChIDHistQ(from.getIdChannel()));
        morphResult[0].setChannel(this.fromTo[0].getPart().getChannel());
        morphResult[1].setChannel(this.fromTo[1].getPart().getChannel());
        if (this.lpmorph == null) {
        }
        if (this.lpmorph[0] == null) {
        }
        if (this.multi == null) {
        }
        if (this.fotole && this.lpmorph[0].getTonalManager() != this.multi.getTonalLeadObj()) {
            lpmorph[0].setPart(morphResult[0]);
            lpmorph[0].convertToDEPA();
            lpmorph[0].initToTonalManager(this.multi.getTonalLeadObj());
            lpmorph[0].convertFromDEPA();
            lpmorph[1].setPart(morphResult[1]);
            lpmorph[1].convertToDEPA();
            lpmorph[1].initToTonalManager(this.multi.getTonalLeadObj());
            lpmorph[1].convertFromDEPA();
            this.morphResult[0] = lpmorph[0].getPart();
            this.morphResult[1] = lpmorph[1].getPart();
        }
    }
