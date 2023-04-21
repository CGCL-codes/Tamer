    public static void exportExperiments(Tasks task, final ZipOutputStream stream, List<Experiment> experiments) throws IOException, SQLException, NoConnectionToDBException, InstanceNotInDBException, InterruptedException {
        int current = 1;
        for (Experiment exp : experiments) {
            task.setOperationName("Exporting experiment " + current + " / " + experiments.size());
            task.setStatus("Writing jobs..");
            task.setTaskProgress(current / (float) experiments.size());
            ExperimentResultDAO.exportExperimentResults(stream, exp);
            task.setStatus("Writing solver configurations..");
            SolverConfigurationDAO.exportSolverConfigurations(stream, exp);
            task.setStatus("Retrieving instance and configuration scenario informations..");
            exp.instances = ExperimentHasInstanceDAO.getExperimentHasInstanceByExperimentId(exp.getId());
            exp.scenario = ConfigurationScenarioDAO.getConfigurationScenarioByExperimentId(exp.getId());
            current++;
        }
        task.setOperationName("Exporting experiments..");
        task.setTaskProgress(0.f);
        task.setStatus("Writing experiment informations..");
        stream.putNextEntry(new ZipEntry("experiments.edacc"));
        writeExperimentsToStream(new ObjectOutputStream(stream), experiments);
        for (Experiment exp : experiments) {
            exp.instances = null;
            exp.scenario = null;
        }
        task.setStatus("Done.");
    }
