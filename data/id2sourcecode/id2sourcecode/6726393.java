                    public void run() {
                        StringWriter writer = new StringWriter();
                        character.export(new ExportHandler(templateFile), new BufferedWriter(writer));
                        StringReader reader = new StringReader(writer.toString());
                        EditorKit kit = htmlPane.getEditorKit();
                        HTMLDocument doc = new HTMLDocument();
                        try {
                            doc.setBase(templateFile.getParentFile().toURL());
                            doc.putProperty("imageCache", cache);
                            kit.read(reader, doc, 0);
                        } catch (IOException ex) {
                            Logging.errorPrint("Could not get parent of load template file " + "for info panel.", ex);
                        } catch (BadLocationException ex) {
                        }
                        if (installed) {
                            htmlPane.setDocument(doc);
                        }
                    }
