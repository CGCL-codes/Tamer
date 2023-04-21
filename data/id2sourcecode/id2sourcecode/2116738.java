    public static void rinnovaServizio(int servizioID) {
        int ordID = -1, moltiplicatore = 1, tipoPagamentoID = -1;
        double scontoTemplate = 0.00, scontoDaApplicare = 0.00, oggetti = 0.00;
        boolean togliQNT = false;
        try {
            jcPostgreSQL.creaTransaione("BEGIN");
            jcPostgreSQL.queryDBRo3("SELECT s.*, st.durata, st.durata_tipo, st.moltiplicatore, st.sconto FROM servizi AS s " + " LEFT JOIN servizi_template AS st ON (s.servizi_templateid=st.servizi_templateid) " + " WHERE s.serviziid=" + servizioID);
            String dataVecchia = jcFunzioni.formattaData(jcPostgreSQL.query3.getDate("data_rinnovo"));
            tipoPagamentoID = jcPostgreSQL.query3.getInt("tipo_pagamentoid");
            moltiplicatore = jcPostgreSQL.query3.getInt("moltiplicatore");
            scontoTemplate = jcPostgreSQL.query3.getObject("sconto") == null ? 0.00 : jcPostgreSQL.query3.getDouble("sconto");
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            java.util.Calendar c = java.util.Calendar.getInstance();
            try {
                c.setTime(sdf.parse(dataVecchia));
            } catch (ParseException ex) {
                Logger.getLogger(jcFunzioniServizi.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (jcPostgreSQL.query3.getString("durata_tipo").equals("G")) {
                c.add(java.util.Calendar.DATE, jcPostgreSQL.query3.getInt("durata"));
            } else if (jcPostgreSQL.query3.getString("durata_tipo").equals("M")) {
                c.add(java.util.Calendar.MONTH, jcPostgreSQL.query3.getInt("durata"));
            } else if (jcPostgreSQL.query3.getString("durata_tipo").equals("A")) {
                c.add(java.util.Calendar.YEAR, jcPostgreSQL.query3.getInt("durata"));
            }
            sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            jcPostgreSQL.eseguiDB("UPDATE servizi SET data_rinnovo='" + sdf.format(c.getTime()) + "'" + " WHERE serviziid=" + servizioID);
            sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            jcPostgreSQL.eseguiDB("INSERT INTO ordini (clienteid, adminid, tipodoc, tipo_pagamentoid, stato_ordiniid, note, riferimento_tipo, riferimento_id, come_spedire, codicerid_autonomo) VALUES (" + Integer.toString(jcPostgreSQL.query3.getObject("da_fatturare_a") == null ? jcPostgreSQL.query3.getInt("clienteid") : jcPostgreSQL.query3.getInt("da_fatturare_a")) + ", " + Integer.toString(jcPostgreSQL.adminID) + ", " + jcVariabili.SERVIZIO_TIPODOC + ", " + (jcPostgreSQL.query3.getObject("tipo_pagamentoid") == null ? "NULL" : jcPostgreSQL.query3.getInt("tipo_pagamentoid")) + ", " + jcVariabili.SERVIZIO_STATO + "," + "'Servizio " + jcPostgreSQL.query3.getString("nome").trim() + " con competenza dal " + dataVecchia + " al " + sdf.format(c.getTime()) + "', " + "'sv', " + servizioID + ", " + (jcPostgreSQL.query3.getObject("come_spedire") == null ? "NULL" : jcPostgreSQL.query3.getInt("come_spedire")) + ", " + (jcPostgreSQL.query3.getObject("codicerid_autonomo") == null ? "NULL" : "'" + jcPostgreSQL.query3.getString("codicerid_autonomo") + "'") + ")");
            jcPostgreSQL.queryDBRo("SELECT sottrai FROM tipi_documenti WHERE tipo_documentoid=" + jcVariabili.SERVIZIO_TIPODOC);
            togliQNT = jcPostgreSQL.query.getBoolean("sottrai");
            jcPostgreSQL.queryDBRo("SELECT ordineid FROM ordini WHERE adminid=" + Integer.toString(jcPostgreSQL.adminID) + " ORDER BY ordineid DESC LIMIT 1");
            ordID = jcPostgreSQL.query.getInt("ordineid");
            double daordinare = 0.00, p_vendita = 0.00, iva = 0.00;
            jcPostgreSQL.queryDB2("SELECT sa.* FROM servizi_aggiuntivi AS sa " + "INNER JOIN servizi_stato AS ss ON (sa.servizi_statoid=ss.servizi_statoid) " + "WHERE sa.serviziid=" + servizioID + " AND sa.data_cessazione IS NULL AND sa.in_fatturazione=TRUE AND ss.tipo_stato=10");
            while (jcPostgreSQL.query2.next()) {
                jcPostgreSQL.queryDBRo("SELECT p_vendita, p_grossista, qnt, iva FROM listino WHERE listinoid=" + jcPostgreSQL.query2.getInt("listinoid"));
                if ((jcPostgreSQL.query.getDouble("qnt") - moltiplicatore) >= 0) {
                    daordinare = 0;
                } else {
                    if (jcPostgreSQL.query.getDouble("qnt") > 0) {
                        daordinare = jcPostgreSQL.query.getDouble("qnt") - moltiplicatore;
                    } else {
                        daordinare = -1;
                    }
                }
                p_vendita = jcPostgreSQL.query2.getObject("prezzo") == null ? jcPostgreSQL.query.getDouble("p_vendita") : jcPostgreSQL.query2.getDouble("prezzo");
                iva = jcPostgreSQL.query2.getObject("iva") == null ? jcPostgreSQL.query.getDouble("iva") : jcPostgreSQL.query2.getDouble("iva");
                scontoDaApplicare = (scontoTemplate == 0.00 ? 0.00 : p_vendita * scontoTemplate / 100);
                oggetti = (p_vendita * moltiplicatore) - (scontoDaApplicare * moltiplicatore);
                jcPostgreSQL.eseguiDB("INSERT INTO ordini_oggetti (ordineid, listinoid, qnt, p_grossista, p_vendita, iva, sconto, daordinare, riferimento) VALUES (" + Integer.toString(ordID) + ", " + jcPostgreSQL.query2.getInt("listinoid") + "," + moltiplicatore + "," + jcPostgreSQL.query.getDouble("p_grossista") + "," + p_vendita + ", " + iva + ", " + (scontoTemplate == 0.00 ? "0.00" : Double.toString(scontoDaApplicare)) + " ," + daordinare + ", " + (jcPostgreSQL.query2.getObject("nome") == null ? "NULL" : "'" + jcPostgreSQL.togliApice(jcPostgreSQL.query2.getString("nome").trim()) + "'") + ")");
                if (togliQNT) {
                    jcPostgreSQL.eseguiDB("UPDATE listino SET qnt=qnt-" + moltiplicatore + " WHERE listinoid=" + jcPostgreSQL.query2.getInt("listinoid"));
                }
            }
            if (tipoPagamentoID != -1) {
                jcPostgreSQL.queryDBRo2("SELECT l.listinoid, l.iva, l.p_grossista, l.p_vendita FROM servizi AS s " + " INNER JOIN tipi_pagamenti AS tp ON (tp.tipo_pagamentoid=s.tipo_pagamentoid) " + " LEFT JOIN listino AS l ON (tp.spese_aggiuntive=l.listinoid) " + " WHERE s.serviziid=" + servizioID);
                if (jcPostgreSQL.query2.getObject("listinoid") != null) {
                    jcPostgreSQL.eseguiDB("INSERT INTO ordini_oggetti (ordineid, listinoid, qnt, p_grossista, p_vendita, iva, sconto, daordinare) VALUES (" + Integer.toString(ordID) + ", " + jcPostgreSQL.query2.getInt("listinoid") + "," + "1," + jcPostgreSQL.query2.getDouble("p_grossista") + "," + jcPostgreSQL.query2.getDouble("p_vendita") + ", " + jcPostgreSQL.query2.getDouble("iva") + ", " + "0.00 ," + "0 )");
                }
            }
            globali.jcDocumenti.aggiornaTotali(ordID, true);
            globali.jcFunzioniServizi.log(servizioID, "RINNOVO AUTOMATICO (con nuova data scadenza " + sdf.format(c.getTime()) + ")");
            jcPostgreSQL.creaTransaione("COMMIT");
        } catch (SQLException ex) {
            jcPostgreSQL.creaTransaione("ROLL BACK");
            Logger.getLogger(jcFunzioni.class.getName()).log(Level.SEVERE, null, ex);
            jcFunzioni.erroreSQL(ex.toString());
        }
    }
