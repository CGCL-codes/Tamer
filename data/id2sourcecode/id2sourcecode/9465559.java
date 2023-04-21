    private void Baceptar_actionPerformed() {
        try {
            if (!checkCabe()) return;
            if (jt.isVacio()) {
                mensajeErr("Introduzca valores en grid");
                return;
            }
            String s = "SELECT l.*,a.* FROM v_albavel as l, V_albvenpar as a where " + " l.emp_codi = " + emp_codiE.getValorInt() + " and l.avc_ano = " + avc_anoE.getValorInt() + " and l.avc_nume = " + avc_numeE.getValorInt() + " and l.avc_serie = '" + avc_seriE.getText() + "'" + " and a.emp_codi = " + emp_codiE.getValorInt() + " and a.avc_ano = " + avc_anoE.getValorInt() + " and a.avc_serie = '" + avc_seriE.getText() + "'" + " and a.avc_nume = " + avc_numeE.getValorInt() + " and a.avl_numlin = l.avl_numlin" + " order by l.avl_numlin,a.avp_numlin ";
            if (!dtCon1.select(s)) {
                msgBox("Lineas de Albaran NO encontradas");
                mensajeErr("Lineas de Albaran NO encontradas");
                return;
            }
            swRotoAlb = true;
            swRotoLin = true;
            int numLin = dtCon1.getInt("avl_numlin");
            double kilos;
            double impAlb = 0;
            nLiAlb = 0;
            nLiDes = 0;
            impAcum = 0;
            nLiJT = 0;
            double impGrid = jt.getValorDec(nLiJT, 2);
            double acumAlb = 0;
            do {
                if (dtCon1.getInt("avl_numlin") != numLin) {
                    numLin = dtCon1.getInt("avl_numlin");
                    swRotoLin = true;
                }
                impLinea = Formatear.Redondea(dtCon1.getDouble("avl_prven") * dtCon1.getDouble("avp_canti"), numDec);
                while (impLinea > 0 && nLiJT + 1 <= jt.getRowCount()) {
                    if (impAcum + impLinea > impGrid) {
                        impAlb = jt.getValorDec(nLiJT, 2) - impAcum;
                        kilos = Formatear.Redondea(impAlb / dtCon1.getDouble("avl_prven"), numDec);
                        acumAlb += Formatear.Redondea((kilos * dtCon1.getDouble("avl_prven")), numDec);
                        impLinea -= Formatear.Redondea((kilos * dtCon1.getDouble("avl_prven")), numDec);
                        guardaLineaDes(kilos);
                        swRotoAlb = true;
                        swRotoLin = true;
                        nLiJT++;
                        impGrid = jt.getValorDec(nLiJT, 2);
                        impAcum = 0;
                    } else {
                        kilos = Formatear.Redondea(impLinea / dtCon1.getDouble("avl_prven"), numDec);
                        acumAlb += Formatear.Redondea((kilos * dtCon1.getDouble("avl_prven")), numDec);
                        impAcum += Formatear.Redondea((kilos * dtCon1.getDouble("avl_prven")), numDec);
                        impLinea = 0;
                        guardaLineaDes(kilos);
                    }
                }
            } while (dtCon1.next());
            for (int n = 0; n < jt.getRowCount(); n++) {
                jt.setValor("" + actAlbaran(emp_codiE.getValorInt(), avc_anoE.getValorInt(), avc_seriE.getText(), jt.getValorInt(n, 0)), n, 2);
            }
            s = "DELETE FROM v_albavec WHERE avc_ano = " + avc_anoE.getValorInt() + " and emp_codi = " + emp_codiE.getValorInt() + " and avc_serie = '" + avc_seriE.getText() + "'" + " and avc_nume = " + avc_numeE.getValorInt();
            dtAdd.executeUpdate(s);
            s = "DELETE FROM v_albavel WHERE avc_ano = " + avc_anoE.getValorInt() + " and emp_codi = " + emp_codiE.getValorInt() + " and avc_serie = '" + avc_seriE.getText() + "'" + " and avc_nume = " + avc_numeE.getValorInt();
            dtAdd.executeUpdate(s);
            s = "DELETE FROM v_albvenpar WHERE avc_ano = " + avc_anoE.getValorInt() + " and emp_codi = " + emp_codiE.getValorInt() + " and avc_serie = '" + avc_seriE.getText() + "'" + " and avc_nume = " + avc_numeE.getValorInt();
            dtAdd.executeUpdate(s);
            dtAdd.commit();
            mensajeErr("Albaranes .... desglosados");
            jt.setEnabled(false);
            Baceptar.setEnabled(false);
            Bcalcular.setEnabled(false);
        } catch (Exception k) {
            Error("Error al calcular Albaranes", k);
            try {
                dtAdd.rollback();
            } catch (SQLException k1) {
                k1.printStackTrace();
            }
        }
    }
