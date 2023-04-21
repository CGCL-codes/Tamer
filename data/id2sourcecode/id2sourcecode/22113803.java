    public void ej_edit1() {
        recalcCostoLin();
        jtLin.salirFoco();
        int nRow = jtLin.getRowCount();
        try {
            for (int n = 0; n < nRow; n++) {
                s = "UPDATE v_despfin SET def_prcost = " + jtLin.getValorDec(n, 5) + ", def_numdes = " + (grd_numeE.getValorInt() > 100 ? grd_numeE.getValorInt() : 99) + " WHERE eje_nume = " + dtCons.getInt("eje_nume") + " AND  emp_codi = " + dtCons.getInt("emp_codi") + (grd_numeE2.getValorInt() > 100 ? " and def_numdes = " + grd_numeE2.getValorInt() : " AND deo_codi = " + deo_codiE.getValorInt()) + " AND pro_codi = " + jtLin.getValInt(n, 0);
                stUp.executeUpdate(s);
            }
            nRow = jtCab.getRowCount();
            for (int n = 0; n < nRow; n++) {
                s = "UPDATE v_desporig set deo_numdes = " + (grd_numeE.getValorInt() > 100 ? grd_numeE.getValorInt() : 99) + ", deo_prcost = " + jtCab.getValorDec(n, 6) + " WHERE  eje_nume = " + eje_numeE.getText() + " AND emp_codi = " + emp_codiE.getValorInt() + (grd_numeE2.getValorInt() > 100 ? " and deo_numdes = " + grd_numeE2.getValorInt() : " AND deo_codi = " + deo_codiE.getValorInt()) + " and pro_codi = " + jtCab.getValInt(n, 3);
                stUp.executeUpdate(s);
            }
            if (grd_numeE.getValorInt() >= 100) {
                s = "SELECT * FROM grupdesp WHERE eje_nume = " + dtCons.getInt("eje_nume") + " AND  emp_codi = " + dtCons.getInt("emp_codi") + " AND grd_nume = " + grd_numeE.getValorInt();
                if (!dtAdd.select(s, true)) {
                    dtAdd.addNew("grupdesp");
                    dtAdd.setDato("emp_codi", dtCons.getInt("emp_codi"));
                    dtAdd.setDato("eje_nume", dtCons.getInt("eje_nume"));
                    dtAdd.setDato("grd_nume", grd_numeE.getValorInt());
                    dtAdd.setDato("prv_codi", EU.lkEmpresa.getDatoInt("emp_prvdes"));
                    dtAdd.setDato("grd_serie", "V");
                    dtAdd.setDato("grd_block", "N");
                } else dtAdd.edit(dtAdd.getCondWhere());
                dtAdd.setDato("grd_kilo", deo_kilosE.getValorDec());
                dtAdd.setDato("grd_unid", 1);
                dtAdd.setDato("grd_prmeco", deo_prcostE.getValorDec());
                dtAdd.setDato("grd_incval", grd_incvalE.getSelecion());
                dtAdd.setDato("grd_valor", "S");
                dtAdd.update(stUp);
            }
            ctUp.commit();
        } catch (Exception ex) {
            try {
                ctUp.rollback();
            } catch (SQLException ex1) {
            }
            Error("Error al Actualizar Datos", ex);
        }
        activaTodo();
        verDatos(dtCons);
        nav.pulsado = navegador.NINGUNO;
        mensajeErr("Despieze ... Valorado");
        nav.pulsado = navegador.NINGUNO;
    }
