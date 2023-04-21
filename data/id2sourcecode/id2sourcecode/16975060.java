    private void ZipVars(ZipOutputStream out) throws Exception {
        if (vars == null || vars.isEmpty()) return;
        String filename = "TOPIC" + GetId() + ".vars";
        out.putNextEntry(new ZipEntry(filename));
        try {
            ZipWriter writer = new ZipWriter(out);
            java.util.Enumeration vars_enum = vars.elements();
            while (vars_enum.hasMoreElements()) {
                Var var = (Var) vars_enum.nextElement();
                if (var != null) {
                    writer.println(var.name);
                    writer.println(Utils.Null2Empty(var.value));
                }
            }
        } finally {
            out.closeEntry();
        }
    }
