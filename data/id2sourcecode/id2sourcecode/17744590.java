    public final void testSaveZuordnungAuswahlkriteriumQualitativLagermittelDAO() {
        try {
            ZuordnungAuswahlkriteriumQualitativLagermittelDAO dao = new ZuordnungAuswahlkriteriumQualitativLagermittelDAO();
            Hashtable<ZuordnungXYTO, Boolean> datensaetze = dao.getZuordnungenAuswahlkriteriumQualitativLagermittel();
            assertTrue("[testSaveZuordnungAuswahlkriteriumQualitativLagermittelDAO]: Collection darf nicht NULL sein!", null != datensaetze);
            assertTrue("[testSaveZuordnungAuswahlkriteriumQualitativLagermittelDAO]: Collection muss Elemente enthalten!", datensaetze.size() > 0);
            int readCount = datensaetze.size();
            dao.saveZuordnungenAuswahlkriteriumQualitativLagermittel(datensaetze);
            Hashtable<ZuordnungXYTO, Boolean> datensaetzeNachSchreiben = dao.getZuordnungenAuswahlkriteriumQualitativLagermittel();
            assertTrue("[testSaveZuordnungAuswahlkriteriumQualitativLagermittelDAO]: Collection darf nicht NULL sein!", null != datensaetzeNachSchreiben);
            assertTrue("[testSaveZuordnungAuswahlkriteriumQualitativLagermittelDAO]: Collection muss Elemente enthalten!", datensaetzeNachSchreiben.size() > 0);
            int writeCount = datensaetzeNachSchreiben.size();
            assertTrue("[testSaveZuordnungAuswahlkriteriumQualitativLagermittelDAO]: Die Anzahl der Datens�tze muss �bereinstimmen!", readCount == writeCount);
        } finally {
        }
    }
