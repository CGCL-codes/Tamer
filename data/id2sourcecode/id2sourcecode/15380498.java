    public ContentDescriptor() {
        super();
        nsURI = "http://jakarta.apache.org/jetspeed/xml/jetspeed-portal-content";
        xmlName = "content";
        XMLFieldDescriptorImpl desc = null;
        XMLFieldHandler handler = null;
        FieldValidator fieldValidator = null;
        desc = new XMLFieldDescriptorImpl(java.lang.String.class, "_version", "version", NodeType.Attribute);
        desc.setImmutable(true);
        handler = (new XMLFieldHandler() {

            public Object getValue(Object object) throws IllegalStateException {
                Content target = (Content) object;
                return target.getVersion();
            }

            public void setValue(Object object, Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    Content target = (Content) object;
                    target.setVersion((java.lang.String) value);
                } catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public Object newInstance(Object parent) {
                return null;
            }
        });
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://jakarta.apache.org/jetspeed/xml/jetspeed-portal-content");
        addFieldDescriptor(desc);
        fieldValidator = new FieldValidator();
        {
            StringValidator sv = new StringValidator();
            sv.setWhiteSpace("preserve");
            fieldValidator.setValidator(sv);
        }
        desc.setValidator(fieldValidator);
        desc = new XMLFieldDescriptorImpl(Channel.class, "_channel", "channel", NodeType.Element);
        handler = (new XMLFieldHandler() {

            public Object getValue(Object object) throws IllegalStateException {
                Content target = (Content) object;
                return target.getChannel();
            }

            public void setValue(Object object, Object value) throws IllegalStateException, IllegalArgumentException {
                try {
                    Content target = (Content) object;
                    target.setChannel((Channel) value);
                } catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }

            public Object newInstance(Object parent) {
                return new Channel();
            }
        });
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://jakarta.apache.org/jetspeed/xml/jetspeed-portal-content");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        fieldValidator = new FieldValidator();
        fieldValidator.setMinOccurs(1);
        desc.setValidator(fieldValidator);
    }
