    public void writeAttachment(Long attachmentId, OutputStream stream) throws NotFoundException, PermissionException {
        Attachment a = this.em.get(Attachment.class, attachmentId);
        a.getMail().getList().checkPermission(this.getMe(), Permission.VIEW_ARCHIVES);
        Blob data = a.getContent();
        try {
            BufferedInputStream bis = new BufferedInputStream(data.getBinaryStream());
            int stuff;
            while ((stuff = bis.read()) >= 0) stream.write(stuff);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
