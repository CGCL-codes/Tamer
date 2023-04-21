    public boolean EliminarPatron(int indice) {
        int i;
        if ((m_numPatrones == 0) || (indice < 0) || (indice > m_numPatrones - 1)) return false;
        float[][] aux = new float[m_numPatrones - 1][];
        for (i = 0; i < indice; i++) aux[i] = m_patrones[i];
        for (i = indice; i < m_numPatrones; i++) aux[i] = m_patrones[i + 1];
        m_patrones = aux;
        m_numPatrones--;
        return true;
    }
