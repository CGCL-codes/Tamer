    protected void insertarEnVector(ArrayList rVectorDatos, ArrayList coincidencia, Value vdValorAInsertar) {
        int llIndiceIzq;
        int llIndiceDer;
        int llMedio;
        int indice = -1;
        double ldValorComparacion;
        double valorAInsertar = getValue(vdValorAInsertar);
        if (rVectorDatos.size() == 0) {
            rVectorDatos.add(vdValorAInsertar);
            coincidencia.add(new Integer(1));
            return;
        }
        llIndiceIzq = 0;
        llIndiceDer = rVectorDatos.size() - 1;
        llMedio = (llIndiceIzq + llIndiceDer) / 2;
        while (llIndiceIzq <= llIndiceDer) {
            ldValorComparacion = getValue((Value) rVectorDatos.get(llMedio));
            if (valorAInsertar > ldValorComparacion) {
                llIndiceIzq = llMedio + 1;
                llMedio = (llIndiceIzq + llIndiceDer) / 2;
            } else if (valorAInsertar < ldValorComparacion) {
                llIndiceDer = llMedio - 1;
                llMedio = (llIndiceIzq + llIndiceDer) / 2;
            } else if (valorAInsertar == ldValorComparacion) {
                indice = llMedio;
                int index = rVectorDatos.indexOf(vdValorAInsertar);
                int coin = ((Integer) coincidencia.get(index)).intValue() + 1;
                coincidencia.remove(index);
                coincidencia.add(index, new Integer(coin));
                return;
            }
        }
        ldValorComparacion = getValue((Value) rVectorDatos.get(llMedio));
        if (valorAInsertar > ldValorComparacion) {
            indice = llMedio + 1;
        } else {
            indice = llMedio;
        }
        rVectorDatos.add(indice, vdValorAInsertar);
        coincidencia.add(indice, new Integer(1));
    }
