    private void setSequence(long sequence) throws org.osid.id.IdException {
        if (this.file == null) {
            logger.logError("no sequence file specified");
            throw new org.osid.id.IdException(org.osid.id.IdException.CONFIGURATION_ERROR);
        }
        java.io.File file;
        java.nio.channels.FileChannel channel;
        java.nio.channels.FileLock lock;
        try {
            file = new java.io.File(this.file);
            channel = new java.io.RandomAccessFile(file, "r").getChannel();
            lock = channel.lock(0, Integer.MAX_VALUE, true);
        } catch (Exception e) {
            logger.logError("unable to lock file " + this.file + ": " + e.getMessage());
            throw new org.osid.id.IdException(org.osid.id.IdException.OPERATION_FAILED);
        }
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEE, dd MMM yyy HH:mm:ss Z");
        java.util.Date date = new java.util.Date();
        try {
            java.io.BufferedWriter out = new java.io.BufferedWriter(new java.io.FileWriter(file));
            out.write("<?xml version=\"1.0\"?>\n");
            out.write("<IdManager impl=\"edu.mit.osidimpl.id.local\">\n");
            out.write("    <id assignedDate=\"" + sdf.format(date) + "\">\n");
            if (this.prefix != null) {
                out.write("        <prefix>\n");
                out.write("            " + this.prefix + "\n");
                out.write("        </prefix>\n");
            }
            out.write("        <sequence>\n");
            out.write("            " + sequence + "\n");
            out.write("        </sequence>\n");
            out.write("    </id>\n");
            out.write("</IdManager>\n");
            out.close();
        } catch (java.io.IOException ie) {
            logger.logError("unable to write identifier to " + this.file + ": " + ie.getMessage());
            throw new org.osid.id.IdException(org.osid.id.IdException.OPERATION_FAILED);
        }
        try {
            lock.release();
            channel.close();
        } catch (java.io.IOException ie) {
            logger.logError("cannot release lock on file " + this.file + ": " + ie.getMessage());
            throw new org.osid.id.IdException(org.osid.id.IdException.OPERATION_FAILED);
        }
        return;
    }
