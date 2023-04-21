    private void ZipVars(ZipOutputStream out) throws Exception {
        if (vars == null || vars.isEmpty()) return;
        String filename = "SITE" + GetId() + ".vars";
        out.putNextEntry(new ZipEntry(filename));
        try {
            ZipWriter writer = new ZipWriter(out);
            java.util.Enumeration enum_vars = vars.elements();
            while (enum_vars.hasMoreElements()) {
                Var var = (Var) enum_vars.nextElement();
                writer.println(var.name);
                writer.println(Utils.Null2Empty(var.value));
            }
        } finally {
            out.closeEntry();
        }
    }
