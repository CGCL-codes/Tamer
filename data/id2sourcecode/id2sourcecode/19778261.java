    private String downloadPhoto(final ContactEntry entry, final ContactsService service) throws ServiceException, IOException {
        try {
            final Link photoLink = entry.getContactPhotoLink();
            if (photoLink.getEtag() != null) {
                final Service.GDataRequest request = service.createLinkQueryRequest(photoLink);
                request.execute();
                final InputStream in = request.getResponseStream();
                final ByteArrayOutputStream out = new ByteArrayOutputStream();
                final String fileName = entry.getSelfLink().getHref().substring(entry.getSelfLink().getHref().lastIndexOf('/') + 1) + ".jpg";
                final RandomAccessFile file = new RandomAccessFile(fileName, "rw");
                final byte[] buffer = new byte[4096];
                for (int read = 0; (read = in.read(buffer)) != -1; out.write(buffer, 0, read)) {
                    ;
                }
                file.write(out.toByteArray());
                file.close();
                in.close();
                request.end();
                return (fileName);
            } else return ("");
        } catch (final ResourceNotFoundException e) {
        }
        return ("");
    }
