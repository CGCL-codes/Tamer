    static void dump(PrintWriter writer, Frame frame, int interaction) {
        if (Controller._compactThreadDepth) {
            if (!frame.hasChildren()) {
                return;
            }
            boolean childAboveThreshold = false;
            for (Frame child : frame.childIterator()) {
                if (!belowThreshold(child) || child.hasChildren()) {
                    childAboveThreshold = true;
                    break;
                }
            }
            if (!childAboveThreshold) {
                return;
            }
        }
        writer.print("+------------------------------");
        writer.print(NEW_LINE);
        writer.print("| Thread: ");
        writer.print(frame.getThreadId());
        if (interaction > 1) {
            writer.print(" (interaction #");
            writer.print(interaction);
            writer.print(")");
        }
        writer.print(NEW_LINE);
        writer.print("+------------------------------");
        writer.print(NEW_LINE);
        writer.print("              Time            Percent    ");
        writer.print(NEW_LINE);
        writer.print("       ----------------- ---------------");
        writer.print(NEW_LINE);
        writer.print(" Count    Total      Net   Total     Net  Location");
        writer.print(NEW_LINE);
        writer.print(" =====    =====      ===   =====     ===  =========");
        writer.print(NEW_LINE);
        long threadTotalTime = frame._metrics.getTotalTime();
        dump(writer, 0, frame, (double) threadTotalTime);
    }
