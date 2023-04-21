    public static void defineDelta(Cross c0[], Cross c1[]) {
        float delta_0 = c0[0].get_centre_Blue() - c0[0].getEstimation();
        float delta_1 = c1[0].get_centre_Blue() - c1[0].getEstimation();
        for (int i = 1; i < c0.length; i++) {
            float help = c0[i].get_centre_Blue() - c0[i].getEstimation();
            delta_0 += help;
            help = c1[i].get_centre_Blue() - c1[i].getEstimation();
            delta_1 += help;
        }
        if (delta_0 < delta_1) m_inverse = false; else m_inverse = true;
        delta_0 /= c0.length;
        delta_1 /= c1.length;
        m_delta = (delta_0 + delta_1) / 2;
    }
