    private void ZipOwners(ZipOutputStream out) throws Exception {
        if (owners == null || owners.isEmpty()) return;
        String filename = "SITE" + GetId() + ".owners";
        out.putNextEntry(new ZipEntry(filename));
        try {
            ZipWriter writer = new ZipWriter(out);
            java.util.Enumeration enum_owners = owners.elements();
            while (enum_owners.hasMoreElements()) {
                Owner owner = (Owner) enum_owners.nextElement();
                writer.println(owner.GetID());
            }
        } finally {
            out.closeEntry();
        }
    }
