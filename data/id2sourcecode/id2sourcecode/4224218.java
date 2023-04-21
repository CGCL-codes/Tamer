        public HTMLDocument call() throws Exception {
            StringWriter writer = new StringWriter();
            character.export(new ExportHandler(templateFile), new BufferedWriter(writer));
            StringReader reader = new StringReader(writer.toString());
            EditorKit kit = htmlPane.getEditorKit();
            HTMLDocument doc = new HTMLDocument();
            doc.setBase(templateFile.getParentFile().toURL());
            doc.putProperty("imageCache", cache);
            kit.read(reader, doc, 0);
            return doc;
        }
