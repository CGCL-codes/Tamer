    private long computeMaximumBufferingFactorWithoutAgendaOverlap(final DAF daf, final QoSExpectations qos, final long maxBFactorSoFar) throws OptimizationException, AgendaException, WhenSchedulerException, SchemaMetadataException, TypeMappingException {
        Agenda agenda = null;
        long lowerBeta = 1;
        long upperBeta = maxBFactorSoFar;
        long beta;
        final long alpha_bms = (int) Agenda.msToBms_RoundUp((qos.getMaxAcquisitionInterval()));
        do {
            if (!this.allowDiscontinuousSensing) {
                beta = (lowerBeta + upperBeta) / 2;
            } else {
                beta = upperBeta;
            }
            try {
                agenda = new Agenda(qos.getMaxAcquisitionInterval(), beta, daf, costParams, "", allowDiscontinuousSensing);
                logger.trace("Agenda constructed successfully length=" + agenda.getLength_bms(Agenda.INCLUDE_SLEEP) + " met target length=" + alpha_bms * beta + " with beta=" + beta);
                lowerBeta = beta;
            } catch (AgendaLengthException e) {
                if (!this.allowDiscontinuousSensing) {
                    String msg = "Current acquisition interval cannot be supported without " + "discontinuous sensing enabled. To enable it, set the " + "compiler.allow_discontinuous_sensing option in the " + "snee.properties file to true";
                    logger.warn(msg);
                    throw new WhenSchedulerException(msg);
                }
                upperBeta = beta;
                logger.trace("Max Buffering factor reduced to " + beta);
                if (beta == 1) {
                    String msg = "Acquisition interval too small to be supported.";
                    logger.warn(msg);
                    throw new WhenSchedulerException(msg);
                }
                continue;
            }
        } while (lowerBeta + 1 < upperBeta);
        return lowerBeta;
    }
