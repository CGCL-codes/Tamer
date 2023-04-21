    private static void print(int threads, int numberInvokes, long successfull, long summedDurations, long creation, long execution, long destruction, PrintWriter writer) {
        if (writer == null) return;
        writer.println("-----------------------");
        writer.println("successfull: " + successfull);
        writer.println("duration creation: " + creation);
        writer.println("duration execution: " + execution);
        writer.println("duration destruction: " + destruction);
        writer.println("summed durations for execution: " + summedDurations);
        writer.println("average response time for execution: " + (summedDurations / numberInvokes));
        writer.println("threads: " + threads);
    }
