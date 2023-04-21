    protected void standardReportTo(PrintWriter writer) {
        writer.print("Toe threads report - " + ArchiveUtils.get12DigitDate() + "\n");
        writer.print(" Job being crawled: " + this.controller.getOrder().getCrawlOrderName() + "\n");
        writer.print(" Number of toe threads in pool: " + getToeCount() + " (" + getActiveToeCount() + " active)\n");
        Thread[] toes = this.getToes();
        synchronized (toes) {
            for (int i = 0; i < toes.length; i++) {
                if (!(toes[i] instanceof ToeThread)) {
                    continue;
                }
                ToeThread tt = (ToeThread) toes[i];
                if (tt != null) {
                    writer.print("   ToeThread #" + tt.getSerialNumber() + "\n");
                    tt.reportTo(writer);
                }
            }
        }
    }
