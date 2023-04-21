    public static void exportExperimentResults(final ZipOutputStream stream, Experiment experiment) throws IOException, SQLException, NoConnectionToDBException, InstanceNotInDBException, InterruptedException {
        stream.putNextEntry(new ZipEntry("experiment_" + experiment.getId() + ".jobs"));
        writeExperimentResultsToStream(new ObjectOutputStream(stream), ExperimentResultDAO.getAllByExperimentId(experiment.getId()));
    }
