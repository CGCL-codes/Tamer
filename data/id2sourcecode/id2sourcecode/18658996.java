    public void relacionar(int clipTop, int clipBot, int clipLeft, int clipRig) {
        if (estado == novo) {
            x = (xOrigemOrig + xDestinoOrig) / 2;
            y = (yOrigemOrig + yDestinoOrig) / 2;
        }
        calcularLimites();
        if (estado == novo) {
            x = (xOrigem + xDestino) / 2;
            y = (yOrigem + yDestino) / 2;
            corrigePosicao(clipTop, clipBot, clipLeft, clipRig);
        }
    }
