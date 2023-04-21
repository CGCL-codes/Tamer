    public void exportMessages(Long[] msgIds, ExportFormat format, OutputStream outStream) throws NotFoundException, PermissionException, ExportMessagesException {
        if (ExportFormat.XML.equals(format)) throw new ExportMessagesException("The XML format is not supported, for now.");
        ZipOutputStream zipOutputStream = null;
        if (ExportFormat.RFC2822DIRECTORY.equals(format)) {
            CheckedOutputStream checksum = new CheckedOutputStream(outStream, new Adler32());
            zipOutputStream = new ZipOutputStream(new BufferedOutputStream(checksum));
        }
        try {
            for (Long msgId : msgIds) {
                Mail mail = this.getMailFor(msgId, Permission.VIEW_ARCHIVES);
                switch(format) {
                    case RFC2822DIRECTORY:
                        ZipEntry entry = new ZipEntry(msgId.toString() + ".eml");
                        entry.setComment("Message from " + mail.getFrom() + " for list " + mail.getList().getEmail());
                        entry.setTime(mail.getArrivalDate().getTime());
                        zipOutputStream.putNextEntry(entry);
                        this.writeMessage(mail, zipOutputStream);
                        zipOutputStream.closeEntry();
                        break;
                    case MBOX:
                        outStream.write(("FROM_ " + mail.getFrom()).getBytes());
                        outStream.write("\r\n".getBytes());
                        this.writeMessage(mail, outStream);
                        outStream.write("\r\n\r\n".getBytes());
                        break;
                    default:
                        throw new ExportMessagesException("Unsupported Format!" + format.toString());
                }
            }
            switch(format) {
                case RFC2822DIRECTORY:
                    zipOutputStream.close();
                    break;
            }
        } catch (Exception e) {
            log.error("Error Exporting! ", e);
            throw new ExportMessagesException("Error:" + e.getMessage());
        }
    }
