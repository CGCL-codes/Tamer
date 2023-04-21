    public static void writeResultsToFile(String filename, double time_deblur, double prec_tol) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(filename));
            out.write(new Date().toString());
            out.newLine();
            out.write("Number of processors: " + ConcurrencyUtils.getNumberOfThreads());
            out.newLine();
            out.write("deblur time (tol = " + prec_tol + "): ");
            out.write(String.format(format, time_deblur));
            out.write(" seconds");
            out.newLine();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
