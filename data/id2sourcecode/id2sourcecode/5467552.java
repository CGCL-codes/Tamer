    void writeThreadsDump() throws IOException {
        writer.write(I18N.getCurrentDateAndTime());
        writer.write("\n\n");
        for (final JavaInformations javaInformations : javaInformationsList) {
            writer.write("===== " + I18N.getFormattedString("Threads_sur", javaInformations.getHost()) + " =====");
            writer.write("\n\n");
            final HtmlThreadInformationsReport htmlThreadInformationsReport = new HtmlThreadInformationsReport(javaInformations.getThreadInformationsList(), javaInformations.isStackTraceEnabled(), writer);
            htmlThreadInformationsReport.writeThreadsDump();
        }
    }
