    void traspDato1() {
        try {
            DatosTabla dtCon1 = new DatosTabla(ct);
            dtCon1.setStatement(stUp);
            int numAlb = 0;
            double kilosT = 0, kilos = 0;
            double unidT = 0, unid = 0;
            int nl = jt.getRowCount();
            for (int n = 0; n < nl; n++) {
                if (!jt.getValBoolean(n, 3)) continue;
                unidT++;
            }
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            mensaje("Espere, por favor ... traspasando Individuos");
            s = "SELECT num_serieX FROM v_numerac WHERE emp_codi = " + EU.em_cod + " AND eje_nume = " + EU.ejercicio;
            if (!dtCon1.select(s)) throw new Exception("s: " + s + "\nError al buscar numeracion serie X");
            numAlb = dtCon1.getInt("num_serieX");
            numAlb++;
            s = "UPDATE v_numerac set  num_serieX = " + numAlb + " WHERE emp_codi = " + EU.em_cod + " AND eje_nume = " + EU.ejercicio;
            stUp.executeUpdate(s);
            dtCon1.addNew("v_albavec");
            dtCon1.setDato("emp_codi", EU.em_cod);
            dtCon1.setDato("avc_ano", EU.ejercicio);
            dtCon1.setDato("avc_serie", "X");
            dtCon1.setDato("avc_nume", numAlb);
            dtCon1.setDato("avc_fecalb", avc_fecalbE.getText(), "dd-MM-yyyy");
            dtCon1.setDato("usu_nomb", EU.usuario);
            dtCon1.setDato("alm_codori", alm_codioE.getValor());
            dtCon1.setDato("alm_coddes", alm_codifE.getValor());
            dtCon1.setDato("avc_almori", alm_codioE.getValor());
            dtCon1.setDato("avc_cerra", -1);
            dtCon1.setDato("avc_tarimp", 0);
            dtCon1.setDato("div_codi", 1);
            dtCon1.update(stUp);
            nl = jt.getRowCount();
            for (int n = 0; n < nl; n++) {
                if (!jt.getValBoolean(n, 3)) continue;
                kilosT += jt.getValorDec(n, 1);
                unidT++;
                dtCon1.addNew("v_albavel");
                dtCon1.setDato("emp_codi", EU.em_cod);
                dtCon1.setDato("avc_ano", EU.ejercicio);
                dtCon1.setDato("avc_serie", "X");
                dtCon1.setDato("avc_nume", numAlb);
                dtCon1.setDato("avl_numlin", n);
                dtCon1.setDato("avl_unid", jt.getValorInt(n, 2));
                dtCon1.setDato("pro_codi", pro_codiE.getText());
                dtCon1.setDato("alm_codi", alm_codifE.getValor());
                dtCon1.setDato("avl_canti", jt.getValorDec(n, 1));
                dtCon1.update(stUp);
                dtCon1.addNew("v_albvenpar");
                dtCon1.setDato("emp_codi", EU.em_cod);
                dtCon1.setDato("avc_ano", EU.ejercicio);
                dtCon1.setDato("avc_serie", "X");
                dtCon1.setDato("avc_nume", numAlb);
                dtCon1.setDato("avl_numlin", n);
                dtCon1.setDato("pro_codi", pro_codiE.getText());
                dtCon1.setDato("avp_tiplot", "P");
                dtCon1.setDato("avp_ejelot", eje_numeE.getValorInt());
                dtCon1.setDato("avp_emplot", emp_codiE.getValorInt());
                dtCon1.setDato("avp_serlot", pro_serieE.getText());
                dtCon1.setDato("avp_numpar", pro_numlotE.getValorInt());
                dtCon1.setDato("avp_numind", jt.getValorDec(n, 0));
                dtCon1.setDato("avp_numuni", 1);
                dtCon1.setDato("avp_canti", jt.getValorDec(n, 1));
                dtCon1.update(stUp);
                s = "UPDATE V_STKPART SET alm_codi = " + alm_codifE.getValorInt() + "  WHERE  EJE_NUME= " + eje_numeE.getValorInt() + " AND EMP_CODI= " + emp_codiE.getValorInt() + " AND PRO_SERIE='" + pro_serieE.getText() + "'" + " AND pro_nupar= " + pro_numlotE.getValorInt() + " and alm_codi = " + alm_codioE.getValor() + " and pro_codi= " + pro_codiE.getValorInt() + " and pro_numind = " + jt.getValorDec(n, 0);
                stUp.executeUpdate(s);
                stkPart.actAcum(pro_codiE.getValorInt(), alm_codioE.getValorInt(), jt.getValorDec(n, 1) * -1, jt.getValorInt(n, 2) * -1, avc_fecalbE.getText());
                stkPart.actAcum(pro_codiE.getValorInt(), alm_codifE.getValorInt(), jt.getValorDec(n, 1), jt.getValorInt(n, 2), avc_fecalbE.getText());
            }
            ctUp.commit();
            mensaje("");
            msgBox("Traspaso REALIZADO ... ALBARAN N. " + numAlb);
        } catch (Exception k) {
            try {
                ctUp.rollback();
            } catch (java.sql.SQLException k1) {
            }
            Error("Error al generar el traspaso", k);
            return;
        }
        Pdatos.setEnabled(true);
        activar(false);
        pro_codiE.requestFocus();
    }
