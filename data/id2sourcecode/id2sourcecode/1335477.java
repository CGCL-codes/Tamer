    public final void testSaveZuordnungenAuswahlkriteriumTransportmittel() {
        try {
            ZuordnungAuswahlkriteriumTransportmittelDAO dao = new ZuordnungAuswahlkriteriumTransportmittelDAO();
            Hashtable<ZuordnungXYTO, Boolean> datensaetze = dao.getZuordnungenAuswahlkriteriumTransportmittel();
            assertTrue("[testSaveZuordnungenAuswahlkriteriumTransportmittel]: Collection darf nicht NULL sein!", null != datensaetze);
            assertTrue("[testSaveZuordnungenAuswahlkriteriumTransportmittel]: Collection muss Elemente enthalten!", datensaetze.size() > 0);
            int readCount = datensaetze.size();
            dao.saveZuordnungenAuswahlkriteriumTransportmittel(datensaetze);
            Hashtable<ZuordnungXYTO, Boolean> datensaetzeNachSchreiben = dao.getZuordnungenAuswahlkriteriumTransportmittel();
            assertTrue("[testSaveZuordnungenAuswahlkriteriumTransportmittel]: Collection darf nicht NULL sein!", null != datensaetzeNachSchreiben);
            assertTrue("[testSaveZuordnungenAuswahlkriteriumTransportmittel]: Collection muss Elemente enthalten!", datensaetzeNachSchreiben.size() > 0);
            int writeCount = datensaetzeNachSchreiben.size();
            assertTrue("[testSaveZuordnungenAuswahlkriteriumTransportmittel]: Die Anzahl der Datens�tze muss �bereinstimmen!", readCount == writeCount);
        } finally {
        }
    }
