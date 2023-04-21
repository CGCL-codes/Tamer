    private void ZipOwners(ZipOutputStream out) throws Exception {
        if (owners == null || owners.isEmpty()) return;
        String filename = "TOPIC" + GetId() + ".owners";
        out.putNextEntry(new ZipEntry(filename));
        try {
            ZipWriter writer = new ZipWriter(out);
            java.util.Enumeration owners_enum = owners.elements();
            while (owners_enum.hasMoreElements()) {
                Owner owner = (Owner) owners_enum.nextElement();
                if (owner != null) writer.println(owner.GetID());
            }
        } finally {
            out.closeEntry();
        }
    }
