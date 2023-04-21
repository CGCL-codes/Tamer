    public final void testSaveZuordnungTransportmittelEinAuslagernVorzone() {
        try {
            ZuordnungTransportmittelEinAuslagernVorzoneDAO dao = new ZuordnungTransportmittelEinAuslagernVorzoneDAO();
            Hashtable<ZuordnungXYTO, Boolean> datensaetze = dao.getZuordnungenTransportmittelEinAuslagernVorzone();
            assertTrue("[testSaveZuordnungTransportmittelEinAuslagernVorzone]: Collection darf nicht NULL sein!", null != datensaetze);
            assertTrue("[testSaveZuordnungTransportmittelEinAuslagernVorzone]: Collection muss Elemente enthalten!", datensaetze.size() > 0);
            int readCount = datensaetze.size();
            dao.saveZuordnungenTransportmittelEinAuslagernVorzone(datensaetze);
            Hashtable<ZuordnungXYTO, Boolean> datensaetzeNachSchreiben = dao.getZuordnungenTransportmittelEinAuslagernVorzone();
            assertTrue("[testSaveZuordnungTransportmittelEinAuslagernVorzone]: Collection darf nicht NULL sein!", null != datensaetzeNachSchreiben);
            assertTrue("[testSaveZuordnungTransportmittelEinAuslagernVorzone]: Collection muss Elemente enthalten!", datensaetzeNachSchreiben.size() > 0);
            int writeCount = datensaetzeNachSchreiben.size();
            assertTrue("[testGetZuordnungenLagermittelTransportmittelEinAuslagern]: Die Anzahl der Datens�tze muss �bereinstimmen!", readCount == writeCount);
        } finally {
        }
    }
