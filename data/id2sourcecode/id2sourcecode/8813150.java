    private boolean removeCardFromProgram(int card) {
        log.debug("starting removeCardFromProgram.");
        if (programSet[card].isBlocked()) return false;
        log.debug("searching for card from program set in card set.");
        int i = 0;
        while (i < 9 && codeSet[i].getPriotity() != programSet[card].getPriotity()) {
            i++;
        }
        if (i == 9) {
            log.debug(" -- card not found");
            return false;
        } else log.debug(" -- found card at [" + i + "]");
        log.debug("removing card from programm card set.");
        programSet[card].removeCard(programSetPane);
        programSet[card] = null;
        codeSet[i].select(false);
        i = card;
        while (i < 4) {
            programSet[i] = programSet[i + 1];
            programSet[i + 1] = null;
            i++;
        }
        drawCards();
        return true;
    }
