    public void save(boolean overwrite, File file) throws IOException, Exception {
        if (expander == null) {
            java.util.Hashtable templates = TemplateReader.readFile(ARGO_TEE);
            expander = new OCLExpander(templates);
        }
        preSave();
        ZipOutputStream stream = new ZipOutputStream(new FileOutputStream(file));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream, "UTF-8"));
        ZipEntry zipEntry = new ZipEntry(getBaseName() + UNCOMPRESSED_FILE_EXT);
        stream.putNextEntry(zipEntry);
        expander.expand(writer, this, "", "");
        writer.flush();
        stream.closeEntry();
        String path = file.getParent();
        Argo.log.info("Dir ==" + path);
        int size = _members.size();
        try {
            for (int i = 0; i < size; i++) {
                ProjectMember p = (ProjectMember) _members.elementAt(i);
                if (!(p.getType().equalsIgnoreCase("xmi"))) {
                    Argo.log.info("Saving member of type: " + ((ProjectMember) _members.elementAt(i)).getType());
                    stream.putNextEntry(new ZipEntry(p.getName()));
                    p.save(path, overwrite, writer);
                    writer.flush();
                    stream.closeEntry();
                }
            }
            for (int i = 0; i < size; i++) {
                ProjectMember p = (ProjectMember) _members.elementAt(i);
                if (p.getType().equalsIgnoreCase("xmi")) {
                    Argo.log.info("Saving member of type: " + ((ProjectMember) _members.elementAt(i)).getType());
                    stream.putNextEntry(new ZipEntry(p.getName()));
                    p.save(path, overwrite, writer);
                }
            }
        } catch (IOException e) {
            System.out.println("hat nicht geklappt: " + e);
            e.printStackTrace();
        }
        writer.close();
        postSave();
        try {
            setFile(file);
        } catch (PropertyVetoException ex) {
        }
    }
