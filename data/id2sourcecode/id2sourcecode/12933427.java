    public boolean checkCoherence(Vector<FSM> fsms, Container externalEventsList, Buffer buffer) throws FSMException {
        if (!checkReadWrite(fsms)) {
            this.setIsCoherent(false);
            throw new FSMException("Il sistema non contiene FSM readers o FSM writers.");
        }
        if (!checkExternalEvents(externalEventsList)) {
            this.setIsCoherent(false);
            throw new FSMException("La lista degli eventi esterni contiene eventi definiti" + "    come interni.");
        }
        for (int i = 0; i < fsms.size(); i++) {
            if (!fsms.get(i).checkInputEvents()) {
                this.setIsCoherent(false);
                throw new FSMException("Transizione nella FSM" + (i + 1) + " non ha un evento in ingresso.");
            }
        }
        for (int i = 0; i < fsms.size(); i++) {
            if (!(fsms.get(i).isReader()) && (!fsms.get(i).checkInternalExternalEvents())) {
                this.setIsCoherent(false);
                throw new FSMException("Gli eventi non sono digiunti.");
            }
        }
        for (int i = 0; i < fsms.size(); i++) {
            if ((fsms.get(i).isReader()) && (!buffer.checkBufferEvents(fsms.get(i).getInputEventList()))) {
                this.setIsCoherent(false);
                throw new FSMException("Il buffer contiene eventi non ammissibili");
            }
        }
        for (int i = 0; i < fsms.size(); i++) {
            for (int j = 0; j < fsms.size(); j++) {
                if ((!fsms.get(i).isReader()) && ((fsms.get(j).isReader())) && (!checkOutputEvents(fsms.get(i), fsms.get(j)))) {
                    this.setIsCoherent(false);
                    throw new FSMException("Un evento prodotto dalla fsm1 non è " + "consumabile dalla fsm2.");
                }
            }
        }
        return this.getIsCoherent();
    }
