    public static void exportSolvers(Tasks task, final ZipOutputStream stream, List<Solver> solvers) throws IOException, SQLException, JAXBException {
        task.setOperationName("Exporting solvers..");
        int current = 1;
        for (Solver s : solvers) {
            task.setStatus("Writing solver binary " + current + " / " + solvers.size());
            task.setTaskProgress(current / (float) solvers.size());
            s.parameters = ParameterDAO.getParameterFromSolverId(s.getId());
            s.graph = ParameterGraphDAO.loadParameterGraph(s);
            stream.putNextEntry(new ZipEntry("solver_" + s.getId() + ".solverbinaries"));
            SolverBinariesDAO.writeSolverBinariesToStream(new ObjectOutputStream(stream), s.getSolverBinaries());
            current++;
        }
        task.setTaskProgress(0.f);
        task.setStatus("Writing solver informations..");
        stream.putNextEntry(new ZipEntry("solvers.edacc"));
        writeSolversToStream(new ObjectOutputStream(stream), solvers);
        for (Solver s : solvers) {
            s.parameters = null;
            s.graph = null;
        }
        task.setStatus("Done.");
    }
