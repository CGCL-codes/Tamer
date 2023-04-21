    public static void exportSolverConfigurations(final ZipOutputStream stream, Experiment experiment) throws IOException, SQLException, NoConnectionToDBException, InstanceNotInDBException, InterruptedException {
        List<SolverConfiguration> solverConfigs = getSolverConfigurationByExperimentId(experiment.getId());
        for (SolverConfiguration sc : solverConfigs) {
            sc.parameterInstances = ParameterInstanceDAO.getBySolverConfig(sc);
        }
        stream.putNextEntry(new ZipEntry("experiment_" + experiment.getId() + ".solverconfigs"));
        writeSolverConfigurationsToStream(new ObjectOutputStream(stream), solverConfigs);
        for (SolverConfiguration sc : solverConfigs) {
            sc.parameterInstances = null;
        }
    }
