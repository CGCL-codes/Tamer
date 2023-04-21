    protected void compactReportTo(PrintWriter writer) {
        writer.print(getToeCount() + " threads (" + getActiveToeCount() + " active)\n");
        Thread[] toes = this.getToes();
        boolean legendWritten = false;
        synchronized (toes) {
            for (int i = 0; i < toes.length; i++) {
                if (!(toes[i] instanceof ToeThread)) {
                    continue;
                }
                ToeThread tt = (ToeThread) toes[i];
                if (tt != null) {
                    if (!legendWritten) {
                        writer.println(tt.singleLineLegend());
                        legendWritten = true;
                    }
                    tt.singleLineReportTo(writer);
                }
            }
        }
    }
