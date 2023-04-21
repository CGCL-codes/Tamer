    public void removeContact(Contact kontakt) {
        int i = getRecordNr(kontakt.getBuddyMail());
        for (int g = i; g < (list.length - 1); g++) {
            list[g] = list[g + 1];
        }
    }
