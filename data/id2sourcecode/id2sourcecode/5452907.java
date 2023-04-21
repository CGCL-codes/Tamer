    public final void testSaveZuordnungAuswahlkriteriumQuantitativLagermittelDAO() {
        try {
            ZuordnungAuswahlkriteriumQuantitativLagermittelDAO dao = new ZuordnungAuswahlkriteriumQuantitativLagermittelDAO();
            Hashtable<ZuordnungXYTO, Boolean> datensaetze = dao.getZuordnungenAuswahlkriteriumQuantitativLagermittel();
            assertTrue("[testSaveZuordnungAuswahlkriteriumQuantitativLagermittelDAO]: Collection darf nicht NULL sein!", null != datensaetze);
            assertTrue("[testSaveZuordnungAuswahlkriteriumQuantitativLagermittelDAO]: Collection muss Elemente enthalten!", datensaetze.size() > 0);
            int readCount = datensaetze.size();
            dao.saveZuordnungenAuswahlkriteriumQuantitativLagermittel(datensaetze);
            Hashtable<ZuordnungXYTO, Boolean> datensaetzeNachSchreiben = dao.getZuordnungenAuswahlkriteriumQuantitativLagermittel();
            assertTrue("[testSaveZuordnungAuswahlkriteriumQuantitativLagermittelDAO]: Collection darf nicht NULL sein!", null != datensaetzeNachSchreiben);
            assertTrue("[testSaveZuordnungAuswahlkriteriumQuantitativLagermittelDAO]: Collection muss Elemente enthalten!", datensaetzeNachSchreiben.size() > 0);
            int writeCount = datensaetzeNachSchreiben.size();
            assertTrue("[testSaveZuordnungAuswahlkriteriumQuantitativLagermittelDAO]: Die Anzahl der Datens�tze muss �bereinstimmen!", readCount == writeCount);
        } finally {
        }
    }
