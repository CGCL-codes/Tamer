    public String newContentPackageSession(String _xmlName, String _format, int _renderOpts) throws NoSuchProcessorException, NoAttachmentException, InitialisationException, NoSuchPropertyException, MalformedURLException, SOAPException, IOException, RoutingException {
        try {
            Message request = MessageContext.getCurrentContext().getRequestMessage();
            if (request.countAttachments() == 0) {
                throw new NoAttachmentException("No supplied content package");
            }
            Iterator attachmentIter = request.getAttachments();
            AttachmentPart ap = (AttachmentPart) attachmentIter.next();
            String guid = ipc.newProcessor(_format, _renderOpts);
            String encodedXmlName = unpack(ap, guid, _xmlName);
            byte[] xml = StreamCopier.copyToByteArray(new FileInputStream(new File(encodedXmlName)));
            ipc.initialiseProcessor(guid, xml);
            return guid;
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw new RoutingException("Error creating new session", e);
        }
    }
