    private static void writeReport(ThreadLocalProfiler profiler) {
        Reporter reporter = reporterAtomicReference.get();
        if (reporter != null) {
            reporter.report(report(profiler));
        }
    }
