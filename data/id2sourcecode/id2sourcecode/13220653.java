    public OMElement create(String namespace, String localPart, OMContainer parent, XMLStreamReader reader, OMFactory factory) throws OMException {
        try {
            String prefix = reader.getPrefix();
            StreamingOMSerializer ser = new StreamingOMSerializer();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            XMLStreamWriter writer = StAXUtils.createXMLStreamWriter(baos, encoding);
            ser.serialize(reader, writer, false);
            writer.flush();
            byte[] bytes = baos.toByteArray();
            String text = new String(bytes, "utf-8");
            ByteArrayDataSource ds = new ByteArrayDataSource(bytes, encoding);
            OMNamespace ns = factory.createOMNamespace(namespace, prefix);
            OMElement om = null;
            if (parent instanceof SOAPHeader && factory instanceof SOAPFactory) {
                om = ((SOAPFactory) factory).createSOAPHeaderBlock(localPart, ns, ds);
            } else {
                om = factory.createOMElement(ds, localPart, ns);
            }
            parent.addChild(om);
            return om;
        } catch (XMLStreamException e) {
            throw new OMException(e);
        } catch (OMException e) {
            throw e;
        } catch (Throwable t) {
            throw new OMException(t);
        }
    }
