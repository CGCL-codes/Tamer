    private boolean writeEntityStream(Representation entity, ZipOutputStream out, String entryName) throws IOException {
        if (entity != null && !entryName.endsWith("/")) {
            ZipEntry entry = new ZipEntry(entryName);
            if (entity.getModificationDate() != null) entry.setTime(entity.getModificationDate().getTime()); else {
                entry.setTime(System.currentTimeMillis());
            }
            out.putNextEntry(entry);
            BioUtils.copy(new BufferedInputStream(entity.getStream()), out);
            out.closeEntry();
            return true;
        }
        out.putNextEntry(new ZipEntry(entryName));
        out.closeEntry();
        return false;
    }
