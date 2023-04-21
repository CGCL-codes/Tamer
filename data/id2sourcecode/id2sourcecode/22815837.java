    public static byte[] createAttachmentData(Object attachment) throws FrameworkException, ApplicationExceptions {
        try {
            byte[] data = null;
            if (attachment.getClass().isArray() && attachment.getClass().getComponentType() == Byte.TYPE) {
                if (log.isDebugEnabled()) log.debug("A byte[] has been passed in. Will be used as is");
                data = (byte[]) attachment;
            } else if (attachment instanceof String) {
                if (log.isDebugEnabled()) log.debug("A String has been passed in. The getBytes() method will be used to create attachment data");
                data = ((String) attachment).getBytes();
            } else if (attachment instanceof File) {
                if (log.isDebugEnabled()) log.debug("A File has been passed in. The File contents will be used to create attachment data");
                InputStream is = null;
                ByteArrayOutputStream bos = null;
                try {
                    is = new BufferedInputStream(new FileInputStream((File) attachment));
                    bos = new ByteArrayOutputStream();
                    int b;
                    while ((b = is.read()) != -1) bos.write(b);
                    bos.flush();
                    data = bos.toByteArray();
                } finally {
                    try {
                        if (bos != null) bos.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (is != null) is.close();
                    } catch (IOException e) {
                    }
                }
            } else if (attachment.getClass().isAnnotationPresent(XmlRootElement.class)) {
                if (log.isDebugEnabled()) log.debug(attachment.getClass().getName() + " has the 'XmlRootElement' JAXB annotation, and hence will be marshalled using JAXB");
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                JAXBContext jc = JAXBHelper.obtainJAXBContext(attachment.getClass());
                Marshaller marshaller = jc.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                marshaller.marshal(attachment, new BufferedOutputStream(os));
                data = os.toByteArray();
            } else {
                if (log.isDebugEnabled()) log.debug(attachment.getClass().getName() + " does not have the 'XmlRootElement' JAXB annotation, and hence will be marshalled using XMLEncoder");
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                XMLEncoder e = new XMLEncoder(new BufferedOutputStream(os));
                e.writeObject(attachment);
                e.close();
                data = os.toByteArray();
            }
            return data;
        } catch (Exception e) {
            throw ExceptionHelper.throwAFR(e);
        }
    }
