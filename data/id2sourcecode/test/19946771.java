    public void writeResults() throws IOException {
        s_logger.debug("Printing record.");
        m_writer.write(m_intFormatter.format(m_currentSolveId.intValue()));
        m_writer.write(FIELD_SEPARATOR);
        m_writer.write(m_intFormatter.format(m_currentProblemId.intValue()));
        m_writer.write(FIELD_SEPARATOR);
        for (Criterion criterion : m_critsOrder) {
            if (m_currentSize != null) {
                final Double eval = m_currentSize.getEvaluations().get(criterion);
                m_writer.write(m_intFormatter.format(eval));
            }
            m_writer.write(FIELD_SEPARATOR);
        }
        if (m_currentDimension != null) {
            m_writer.write(m_intFormatter.format(m_currentDimension.getBinaries()));
        }
        m_writer.write(FIELD_SEPARATOR);
        if (m_currentDimension != null) {
            m_writer.write(m_intFormatter.format(m_currentDimension.getContinuous()));
        }
        m_writer.write(FIELD_SEPARATOR);
        if (m_currentDimension != null) {
            m_writer.write(m_intFormatter.format(m_currentDimension.getConstraints()));
        }
        m_writer.write(FIELD_SEPARATOR);
        if (m_mainDuration != null && m_mainDuration.getWallDuration_ms() != null) {
            m_writer.write(m_intFormatter.format(m_mainDuration.getWallDuration_ms()));
        }
        m_writer.write(FIELD_SEPARATOR);
        if (m_mainDuration != null && m_mainDuration.getThreadDuration_ms() != null) {
            m_writer.write(m_intFormatter.format(m_mainDuration.getThreadDuration_ms()));
        }
        m_writer.write(FIELD_SEPARATOR);
        if (m_mainDuration != null && m_mainDuration.getSolverWallDuration_ms() != null) {
            m_writer.write(m_intFormatter.format(m_mainDuration.getSolverWallDuration_ms()));
        }
        m_writer.write(FIELD_SEPARATOR);
        if (m_mainDuration != null && m_mainDuration.getSolverCpuDuration_ms() != null) {
            m_writer.write(m_intFormatter.format(m_mainDuration.getSolverCpuDuration_ms()));
        }
        m_writer.write(FIELD_SEPARATOR);
        if (m_currentDistances != null) {
            m_writer.write(m_intFormatter.format(m_currentDistances.getLastSumDist()));
        }
        m_writer.write(FIELD_SEPARATOR);
        if (m_currentDistances != null) {
            m_writer.write(m_intFormatter.format(m_currentDistances.getLastMaxDist()));
        }
        m_writer.write(FIELD_SEPARATOR);
        for (Double approx : m_approxes) {
            final double approxValue = approx.doubleValue();
            if (m_currentDistances != null) {
                m_writer.write(m_intFormatter.format(m_currentDistances.getNbEquals(approxValue)));
            }
            m_writer.write(FIELD_SEPARATOR);
        }
        if (m_currentDistances != null) {
            m_writer.write(m_intFormatter.format(m_currentDistances.getLastDistanceInAlternatives()));
        }
        m_writer.write(FIELD_SEPARATOR);
        if (m_comments != null) {
            protectAndWrite(m_comments);
        }
        m_writer.write(FIELD_SEPARATOR);
        if (m_findWeightsDurationSystem_ms != null) {
            m_writer.write(m_intFormatter.format(m_findWeightsDurationSystem_ms));
        }
        int nbDmsExported = 0;
        if (m_necessaryComparisons != null) {
            for (DecisionMaker dm : m_necessaryComparisons.keySet()) {
                m_writer.write(FIELD_SEPARATOR);
                m_writer.write(m_intFormatter.format(m_necessaryComparisons.get(dm)));
                ++nbDmsExported;
            }
        }
        for (int i = nbDmsExported; i < m_nbMaxDms; ++i) {
            m_writer.write(FIELD_SEPARATOR);
        }
        m_writer.write(FIELD_SEPARATOR);
        writeBool(m_error);
        m_writer.write(FIELD_SEPARATOR);
        writeBool(m_timeout);
        m_writer.write(FIELD_SEPARATOR);
        writeBool(m_outOfMemory);
        m_writer.write(FIELD_SEPARATOR);
        if (m_optimalValue != null) {
            m_writer.write(m_optimalValue.toString());
        }
        m_writer.newLine();
        m_writer.flush();
        reset();
        s_logger.info("Ended printing results.");
    }
