    public static void writeResultsToFile(String filename, double time_deblur, double time_deblur_regParam, double time_update) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(filename));
            out.write(new Date().toString());
            out.newLine();
            out.write("Number of processors: " + ConcurrencyUtils.getNumberOfThreads());
            out.newLine();
            out.write("deblur() time=");
            out.write(String.format(format, time_deblur));
            out.write(" seconds");
            out.newLine();
            out.write("deblur(regParam) time=");
            out.write(String.format(format, time_deblur_regParam));
            out.write(" seconds");
            out.newLine();
            out.write("update() time=");
            out.write(String.format(format, time_update));
            out.write(" seconds");
            out.newLine();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
