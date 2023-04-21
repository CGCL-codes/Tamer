    public static void writeFFTBenchmarkResultsToFile(String filename, int nthread, int niter, boolean doWarmup, boolean doScaling, int[] sizes, double[] times) {
        String[] properties = { "os.name", "os.version", "os.arch", "java.vendor", "java.version" };
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(filename, false));
            out.write(new Date().toString());
            out.newLine();
            out.write("System properties:");
            out.newLine();
            out.write("\tos.name = " + System.getProperty(properties[0]));
            out.newLine();
            out.write("\tos.version = " + System.getProperty(properties[1]));
            out.newLine();
            out.write("\tos.arch = " + System.getProperty(properties[2]));
            out.newLine();
            out.write("\tjava.vendor = " + System.getProperty(properties[3]));
            out.newLine();
            out.write("\tjava.version = " + System.getProperty(properties[4]));
            out.newLine();
            out.write("\tavailable processors = " + Runtime.getRuntime().availableProcessors());
            out.newLine();
            out.write("Settings:");
            out.newLine();
            out.write("\tused processors = " + nthread);
            out.newLine();
            out.write("\tTHREADS_BEGIN_N_2D = " + ConcurrencyHelper.getThreadsMinSize2D());
            out.newLine();
            out.write("\tTHREADS_BEGIN_N_3D = " + ConcurrencyHelper.getThreadsMinSize3D());
            out.newLine();
            out.write("\tnumber of iterations = " + niter);
            out.newLine();
            out.write("\twarm-up performed = " + doWarmup);
            out.newLine();
            out.write("\tscaling performed = " + doScaling);
            out.newLine();
            out.write("--------------------------------------------------------------------------------------------------");
            out.newLine();
            out.write("sizes=[");
            for (int i = 0; i < sizes.length; i++) {
                out.write(Integer.toString(sizes[i]));
                if (i < sizes.length - 1) {
                    out.write(", ");
                } else {
                    out.write("]");
                }
            }
            out.newLine();
            out.write("times(in msec)=[");
            for (int i = 0; i < times.length; i++) {
                out.write(String.format("%.2f", times[i]));
                if (i < times.length - 1) {
                    out.write(", ");
                } else {
                    out.write("]");
                }
            }
            out.newLine();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
