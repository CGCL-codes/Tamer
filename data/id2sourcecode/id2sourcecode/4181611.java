    private void guardarXml() {
        try {
            Encabezado enc = str.getEncabezado();
            FileOutputStream fos = new FileOutputStream(path);
            XMLOutputFactory factory = XMLOutputFactory.newInstance();
            XMLStreamWriter writer = factory.createXMLStreamWriter(fos);
            writer.writeStartDocument("utf-8", "1.0");
            writer.writeStartElement("articulo");
            writer.writeStartElement("encabezado");
            tag(writer, "javascriptarchivos", enc.getJavascriptArchivos());
            tag(writer, "domready", enc.getDomReady());
            tag(writer, "cssarchivos", enc.getCssArchivos());
            writer.writeEndElement();
            tag(writer, "titulo", str.getTitulo());
            tag(writer, "wiki", str.getDb().getNombre());
            tag(writer, "id", str.getId() + "");
            tag(writer, "namespace", str.getNamespaceId() + "");
            tag(writer, "fecha", str.getFecha().getTime() + "");
            writer.writeStartElement("contenido");
            String c = str.getContenido().toString();
            writer.writeCharacters(c);
            writer.writeEndElement();
            writer.writeEndElement();
            writer.writeEndDocument();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (FactoryConfigurationError e) {
            e.printStackTrace();
        }
    }
