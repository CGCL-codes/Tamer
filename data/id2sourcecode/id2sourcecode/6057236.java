    public void mergeTemplate(Reader reader, Map vars, File out) throws TemplateEngineException {
        VelocityContext vc = VelocityHelper.createVelocityContext(vars);
        try {
            FileWriter writer = new FileWriter(out);
            Velocity.evaluate(vc, writer, out.getName(), reader);
            writer.close();
        } catch (Exception ex) {
            System.out.println(">>> " + out.getAbsolutePath());
            throw new TemplateEngineException("Error merging template: " + ex.getMessage(), ex);
        }
    }
