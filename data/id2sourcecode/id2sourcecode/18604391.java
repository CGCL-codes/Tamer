    public XMLMessage createXMLMessage(ObjectFactory factory, Message message) throws JMSException, IOException, EncoderException {
        try {
            XMLMessage rval = factory.createXMLMessage();
            if (message instanceof TextMessage) {
                rval = factory.createXMLTextMessage();
                XMLTextMessage textRval = (XMLTextMessage) rval;
                TextMessage textMessage = (TextMessage) message;
                if (isBase64EncodeTextMessages()) {
                    byte[] bytes = getBase64().encode(textMessage.getText().getBytes());
                    textRval.setText(new String(bytes, "ASCII"));
                    textRval.setCodec(BASE64_CODEC);
                } else {
                    textRval.setText(textMessage.getText());
                }
            } else if (message instanceof MapMessage) {
                rval = factory.createXMLMapMessage();
                XMLMapMessage mapRval = (XMLMapMessage) rval;
                MapMessage mapMessage = (MapMessage) message;
                for (Enumeration iter = mapMessage.getMapNames(); iter.hasMoreElements(); ) {
                    String propertyName = (String) iter.nextElement();
                    Object propertyValue = mapMessage.getObject(propertyName);
                    Property xmlProperty = factory.createProperty();
                    if (propertyValue != null) {
                        xmlProperty.setValue(propertyValue.toString());
                        xmlProperty.setType(propertyValue.getClass().getName());
                    }
                    xmlProperty.setName(propertyName);
                    mapRval.getBodyProperty().add(xmlProperty);
                }
            } else if (message instanceof BytesMessage) {
                rval = factory.createXMLBytesMessage();
                XMLBytesMessage bytesRval = (XMLBytesMessage) rval;
                BytesMessage bytesMessage = (BytesMessage) message;
                ByteArrayOutputStream bosream = new ByteArrayOutputStream();
                bytesMessage.reset();
                try {
                    for (; ; ) {
                        bosream.write(bytesMessage.readByte());
                    }
                } catch (MessageEOFException ex) {
                }
                bytesRval.setBytes(new String(getBase64().encode(bosream.toByteArray())));
            } else if (message instanceof ObjectMessage) {
                rval = factory.createXMLObjectMessage();
                XMLObjectMessage objectRval = (XMLObjectMessage) rval;
                ObjectMessage objectMessage = (ObjectMessage) message;
                ByteArrayOutputStream bostream = new ByteArrayOutputStream();
                ObjectOutputStream oostream = new ObjectOutputStream(bostream);
                oostream.writeObject(objectMessage.getObject());
                oostream.flush();
                byte b[] = getBase64().encode(bostream.toByteArray());
                String s = new String(b, "ASCII");
                objectRval.setObject(s);
            }
            if (message.getJMSReplyTo() != null) {
                rval.setJMSReplyTo(JMSUtils.getDestinationName(message.getJMSReplyTo()));
                rval.setJMSReplyToDomain(Domain.getDomain(message.getJMSReplyTo()).getId());
            }
            try {
                rval.setJMSDeliveryMode(message.getJMSDeliveryMode());
            } catch (JMSException ex) {
                log.error(ex.getMessage(), ex);
            }
            try {
                rval.setJMSExpiration(message.getJMSExpiration());
            } catch (JMSException ex) {
                log.error(ex.getMessage(), ex);
            }
            try {
                rval.setJMSMessageID(message.getJMSMessageID());
            } catch (JMSException ex) {
                log.error(ex.getMessage(), ex);
            }
            try {
                rval.setJMSPriority(message.getJMSPriority());
            } catch (JMSException ex) {
                log.error(ex.getMessage(), ex);
            }
            try {
                rval.setJMSRedelivered(message.getJMSRedelivered());
            } catch (JMSException ex) {
                log.error(ex.getMessage(), ex);
            } catch (IllegalStateException ex) {
                log.error(ex.getMessage(), ex);
            }
            try {
                rval.setJMSTimestamp(message.getJMSTimestamp());
            } catch (JMSException ex) {
                log.error(ex.getMessage(), ex);
            }
            try {
                rval.setJMSType(message.getJMSType());
            } catch (JMSException ex) {
                log.error(ex.getMessage(), ex);
            }
            try {
                rval.setJMSCorrelationID(message.getJMSCorrelationID());
            } catch (JMSException ex) {
                log.error(ex.getMessage(), ex);
            }
            try {
                if (message.getJMSDestination() != null) {
                    rval.setJMSDestination(JMSUtils.getDestinationName(message.getJMSDestination()));
                    rval.setFromQueue(JMSUtils.isQueue(message.getJMSDestination()));
                }
            } catch (JMSException ex) {
                log.error(ex.getMessage(), ex);
            }
            for (final Enumeration iter = message.getPropertyNames(); iter.hasMoreElements(); ) {
                String propertyName = (String) iter.nextElement();
                if (!propertyName.startsWith("JMS")) {
                    Object propertyValue = message.getObjectProperty(propertyName);
                    Property property = factory.createProperty();
                    property.setName(propertyName);
                    if (propertyValue != null) {
                        property.setValue(StringEscapeUtils.escapeXml(propertyValue.toString()));
                        property.setType(propertyValue.getClass().getName());
                    }
                    rval.getHeaderProperty().add(property);
                }
            }
            return rval;
        } catch (Exception ex) {
            throw new HermesException(ex);
        }
    }
