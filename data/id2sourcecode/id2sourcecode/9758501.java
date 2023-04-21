    protected boolean mbObtenerPosicionEnVector(ArrayList rVectorDatos, double vdValorAInsertar, int[] rlIndiceCorrespondiente, boolean[] rbNuevoElemento) {
        int llIndiceIzq;
        int llIndiceDer;
        int llMedio;
        double ldValorComparacion;
        rbNuevoElemento[0] = false;
        rlIndiceCorrespondiente[0] = -1;
        if (rVectorDatos.size() == 1) {
            if (((udtDatosEstudio) rVectorDatos.get(0)).Coincidencias == 0) {
                rlIndiceCorrespondiente[0] = 0;
                rbNuevoElemento[0] = false;
                return true;
            }
        }
        llIndiceIzq = 0;
        llIndiceDer = rVectorDatos.size() - 1;
        llMedio = (llIndiceIzq + llIndiceDer) / 2;
        while (llIndiceIzq <= llIndiceDer) {
            ldValorComparacion = ((udtDatosEstudio) rVectorDatos.get(llMedio)).Valor;
            if (vdValorAInsertar > ldValorComparacion) {
                llIndiceIzq = llMedio + 1;
                llMedio = (llIndiceIzq + llIndiceDer) / 2;
            } else if (vdValorAInsertar < ldValorComparacion) {
                llIndiceDer = llMedio - 1;
                llMedio = (llIndiceIzq + llIndiceDer) / 2;
            } else if (vdValorAInsertar == ldValorComparacion) {
                rlIndiceCorrespondiente[0] = llMedio;
                rbNuevoElemento[0] = false;
                return true;
            }
        }
        rbNuevoElemento[0] = true;
        ldValorComparacion = ((udtDatosEstudio) rVectorDatos.get(llMedio)).Valor;
        if (vdValorAInsertar > ldValorComparacion) {
            rlIndiceCorrespondiente[0] = llMedio + 1;
        } else {
            rlIndiceCorrespondiente[0] = llMedio;
        }
        return true;
    }
