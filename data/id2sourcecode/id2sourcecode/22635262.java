    public RandomStatsDetails(final IntegerMap<Integer> data) {
        m_data = data;
        if (data.totalValues() != 0) {
            int sumTotal = 0;
            int total = 0;
            for (int i = 1; i <= 6; i++) {
                sumTotal += i * m_data.getInt(Integer.valueOf(i));
                total += m_data.getInt(Integer.valueOf(i));
            }
            m_total = total;
            m_average = ((double) sumTotal) / ((double) data.totalValues());
            if (total % 2 != 0) {
                m_median = calcMedian((total / 2) + 1);
            } else {
                double tmp1 = 0;
                double tmp2 = 0;
                tmp1 = calcMedian((total / 2));
                tmp2 = calcMedian((total / 2) + 1);
                m_median = (tmp1 + tmp2) / 2;
            }
            double variance = 0;
            for (int i = 1; i <= 6; i++) {
                variance += (m_data.getInt(new Integer(i)) - (total / 6)) * (m_data.getInt(new Integer(i)) - (total / 6));
            }
            m_variance = variance / (total - 1);
            m_stdDeviation = Math.sqrt(m_variance);
        } else {
            m_total = 0;
            m_median = 0;
            m_average = 0;
            m_stdDeviation = 0;
            m_variance = 0;
        }
    }
