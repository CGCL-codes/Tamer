    private void writeToFile(HierarchyNode hnode, File file) throws ApplicationException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ByteArrayOutputStream errorMessage = new ByteArrayOutputStream();
        try {
            setStatus("Saving " + hnode);
            freeze(true);
            OutputStream fileData = new BufferedOutputStream(new FileOutputStream(file));
            int lastDot = file.getName().lastIndexOf('.');
            if (lastDot >= 0) {
                String extension = file.getName().substring(lastDot + 1);
                if (outputFilterCommands.containsKey(extension)) {
                    JOptionPane.showMessageDialog(this, "Warning: Some data may be lost converting to\n" + extension + " format.", "Export filter warning", JOptionPane.WARNING_MESSAGE);
                    XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
                    outputter.output(new Document(XMLVisitor.toXML(hnode)), buffer);
                    String command = outputFilterCommands.get(extension);
                    Process filter = Runtime.getRuntime().exec(command);
                    OutputStream processInput = new BufferedOutputStream(filter.getOutputStream());
                    InputStream processOutput = new BufferedInputStream(filter.getInputStream());
                    InputStream processError = new BufferedInputStream(filter.getErrorStream());
                    try {
                        copy(new ByteArrayInputStream(buffer.toByteArray()), processInput);
                        copy(processOutput, fileData);
                        copy(processError, errorMessage);
                    } catch (Exception e) {
                        filter.destroy();
                        throw new ApplicationException("Converter subprocess failed");
                    }
                    int exitCode = filter.waitFor();
                    if (exitCode != 0) {
                        String message = errorMessage.toString();
                        buffer.close();
                        throw new ApplicationException("Converter process returned exit code " + exitCode + "\n" + message);
                    }
                } else {
                    XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
                    ZipOutputStream zos = new ZipOutputStream(fileData);
                    zos.putNextEntry(new ZipEntry(file.getName()));
                    outputter.output(new Document(XMLVisitor.toXML(hnode)), zos);
                    zos.close();
                }
            } else {
                XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
                ZipOutputStream zos = new ZipOutputStream(fileData);
                zos.putNextEntry(new ZipEntry(file.getName()));
                outputter.output(new Document(XMLVisitor.toXML(hnode)), zos);
                zos.close();
            }
            freeze(false);
            setStatus("Saved " + hnode + ".");
        } catch (Exception e) {
            freeze(false);
            if (e instanceof ApplicationException) throw (ApplicationException) e; else throw new ApplicationException("Unable to save file -- IO error.");
        }
    }
