    public void generateEvidence(IndentWriter writer) {
        writer.println("Disk Manager");
        try {
            writer.indent();
            writer.println("percent_done=" + percentDone + ",allocated=" + allocated + ",remaining=" + remaining);
            writer.println("skipped_file_set_size=" + skipped_file_set_size + ",skipped_but_downloaded=" + skipped_but_downloaded);
            writer.println("already_moved=" + alreadyMoved);
        } finally {
            writer.exdent();
        }
    }
