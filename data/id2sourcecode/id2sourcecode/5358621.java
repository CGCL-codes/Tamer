    private static void dump(PrintWriter writer, int depth, Frame parent, double threadTotalTime) {
        long total = parent._metrics.getTotalTime();
        long net = parent.netTime();
        writer.printf("%6d ", parent._metrics.getCount());
        writer.printf("%8.1f ", Math.nanoToMilli(total));
        writer.printf("%8.1f ", Math.nanoToMilli(net));
        if (total > 0) {
            double percent = Math.toPercent(total, threadTotalTime);
            writer.printf("%7.1f ", percent);
        } else {
            writer.print("        ");
        }
        if (net > 0 && threadTotalTime > 0.000) {
            double percent = Math.toPercent(net, threadTotalTime);
            if (percent > 0.1) {
                writer.printf("%7.1f ", percent);
            } else {
                writer.print("        ");
            }
        } else {
            writer.print("        ");
        }
        writer.print(" ");
        for (int i = 0; i < depth; i++) {
            writer.print("| ");
        }
        writer.print("+--");
        writer.print(parent.getInvertedName());
        writer.print(NEW_LINE);
        if (Controller._threadDepth != Controller.UNLIMITED && depth == Controller._threadDepth - 1) {
            return;
        }
        for (Frame child : parent.childIterator()) {
            if (belowThreshold(child)) {
                continue;
            }
            dump(writer, depth + 1, child, threadTotalTime);
        }
    }
