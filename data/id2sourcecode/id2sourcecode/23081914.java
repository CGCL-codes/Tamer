    void Baceptar_actionPerformed(ActionEvent e) {
        String s;
        try {
            s = "SELECT * FROM ven_angulo_iber where ran_fecalb >= '" + feciniE.getFechaDB() + "' and ran_fecalb <= '" + fecfinE.getFechaDB() + "'";
            rs = st1.executeQuery(s);
            if (rs.next()) {
                if (mensajes.mensajeYesNo("Hay registros en estas fechas\n nBorrarlos?") != mensajes.YES) {
                    mensajeErr("Ventas Angulo IberRioja ... NO creadas");
                    return;
                }
            }
            s = "DELETE FROM ven_angulo_iber where ran_fecalb >= '" + feciniE.getFechaDB() + "' and ran_fecalb <= '" + fecfinE.getFechaDB() + "'";
            st1.executeUpdate(s);
            s = "SELECT ac.emp_codi,ac.avc_ano,ac.emp_codi,ac.cli_codi, cl.cli_nomb,cl.cli_direc,cl.cli_pobl,ac.avc_serie,ac.avc_nume," + "al.avl_numlin,ac.avc_fecalb,al.pro_codi,al.avl_canti,al.avl_prbase,al.avl_prven," + "ac.avc_dtopp,cl.cli_zonrep from v_albavec ac,v_albavel al,v_cliente cl where " + " avc_fecalb >= '" + feciniE.getFechaDB() + "' and avc_fecalb <= '" + fecfinE.getFechaDB() + "'" + " and cli_zonrep like 'A%' AND cl.cli_activ ='S' and cl.cli_codi=ac.cli_codi " + " and ac.avc_serie = al.avc_serie" + " and ac.avc_nume = al.avc_nume " + " and ac.avc_ano = al.avc_ano " + " and ac.emp_codi = al.emp_codi " + " ORDER BY ac.avc_serie,ac.avc_nume,al.avl_numlin,al.avl_prven";
            rs = st.executeQuery(s);
            if (!rs.next()) {
                mensajes.mensajeAviso("NO encontrados Albaranes");
                return;
            }
            String alcSerie = rs.getString("avc_serie");
            int alcNumalb = rs.getInt("avc_nume");
            java.util.Date alcFecalb = rs.getDate("avc_fecalb");
            int cliCodi = rs.getInt("cli_codi");
            int proCodi = rs.getInt("pro_codi");
            double prVen = rs.getDouble("avl_prven");
            int numLin = rs.getInt("avl_numlin");
            int ranAno = rs.getInt("avc_ano");
            int empCodi = rs.getInt("emp_codi");
            String zonRep = rs.getString("cli_zonrep");
            double kgVen = 0;
            do {
                if (rs.getInt("pro_codi") == proCodi && prVen == rs.getDouble("avl_prven")) kgVen = kgVen + rs.getDouble("avl_canti"); else {
                    insRegistro(alcSerie, alcNumalb, alcFecalb, cliCodi, proCodi, prVen, kgVen, numLin, ranAno, empCodi, zonRep);
                    alcSerie = rs.getString("avc_serie");
                    alcNumalb = rs.getInt("avc_nume");
                    alcFecalb = rs.getDate("avc_fecalb");
                    cliCodi = rs.getInt("cli_codi");
                    proCodi = rs.getInt("pro_codi");
                    prVen = rs.getDouble("avl_prven");
                    kgVen = rs.getDouble("avl_canti");
                    numLin = rs.getInt("avl_numlin");
                    ranAno = rs.getInt("avc_ano");
                    empCodi = rs.getInt("emp_codi");
                    zonRep = rs.getString("cli_zonrep");
                }
            } while (rs.next());
            insRegistro(alcSerie, alcNumalb, alcFecalb, cliCodi, proCodi, prVen, kgVen, numLin, ranAno, empCodi, zonRep);
            ct.commit();
        } catch (Exception k) {
            fatalError("Error al Buscar Datos de Venta ", k);
            try {
                ct.rollback();
            } catch (Exception k1) {
            }
            return;
        }
        mensajeErr("Datos procesados ...");
    }
