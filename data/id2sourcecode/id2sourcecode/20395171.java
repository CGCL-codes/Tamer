    @Override
    public void visit(IMessageBeanManager messageBeanManager) throws IOException {
        Assert.parameterNotNull(messageBeanManager, "Parameter 'messageBeanManager' must not be null");
        if (getDirection() == IPackagingVisitor.DIRECTION_OUT) {
            for (IMessageBeanDescriptor messageBeanDescriptor : messageBeanManager.getMessageBeanDescriptors()) {
                try {
                    ZipEntry ze = new ZipEntry(MESSAGE_BEAN_DESCRIPTOR_ENTRY + getRecordCount());
                    ze.setComment((null == messageBeanDescriptor.getDescription()) ? Namespace.NULL_TOKEN : messageBeanDescriptor.getDescription());
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    DataOutputStream dos = new DataOutputStream(baos);
                    dos.writeInt(SerializationHeader.MessageBeanDescriptor.ordinal());
                    dos.writeInt(SerializationHeader.Namespace.ordinal());
                    dos.writeUTF(Namespace.toCanonical(messageBeanDescriptor));
                    dos.writeInt(SerializationHeader.DisplayableName.ordinal());
                    dos.writeUTF(messageBeanDescriptor.getName());
                    dos.writeUTF((null == messageBeanDescriptor.getDescription()) ? Namespace.NULL_TOKEN : messageBeanDescriptor.getDescription());
                    dos.writeInt(SerializationHeader.Properties.ordinal());
                    dos.writeInt(messageBeanDescriptor.properties().size());
                    for (IMessageBeanProperty property : messageBeanDescriptor.properties()) {
                        dos.writeInt(SerializationHeader.Namespace.ordinal());
                        dos.writeUTF(Namespace.toCanonical(property));
                        dos.writeInt(SerializationHeader.DisplayableName.ordinal());
                        dos.writeUTF(property.getName());
                        dos.writeUTF((null == property.getDescription()) ? Namespace.NULL_TOKEN : property.getDescription());
                        dos.writeInt(SerializationHeader.PropertyDefinition.ordinal());
                        dos.writeBoolean(property.isPrimaryKeyMember());
                        dos.writeBoolean(property.isNullable());
                        dos.writeInt(property.getDataType().ordinal());
                        dos.writeInt(property.getLength());
                        dos.writeInt(property.getPrecision());
                        dos.writeInt(SerializationHeader.Metadata.ordinal());
                        dos.writeInt(property.getMetadata().size());
                        for (String propertyName : property.getMetadata().stringPropertyNames()) {
                            dos.writeUTF(propertyName);
                            dos.writeUTF(property.getMetadata().getProperty(propertyName));
                        }
                    }
                    dos.writeInt(SerializationHeader.Indexes.ordinal());
                    dos.writeInt(messageBeanDescriptor.indices().size());
                    for (IMessageBeanIndex index : messageBeanDescriptor.indices()) {
                        dos.writeInt(SerializationHeader.Namespace.ordinal());
                        dos.writeUTF(Namespace.toCanonical(index));
                        dos.writeBoolean(index.isUnique());
                        dos.writeInt(SerializationHeader.IndexMembers.ordinal());
                        dos.writeInt(index.getIndex().size());
                        for (IMessageBeanProperty indexProperty : index.getIndex()) {
                            dos.writeUTF(Namespace.toCanonical(indexProperty));
                        }
                    }
                    dos.writeInt(SerializationHeader.Interfaces.ordinal());
                    dos.writeInt(messageBeanDescriptor.interfaces().size());
                    for (Class interfaceClass : messageBeanDescriptor.interfaces()) {
                        dos.writeUTF(interfaceClass.getPackage().getName());
                        dos.writeUTF(interfaceClass.getSimpleName());
                    }
                    dos.writeInt(SerializationHeader.Metadata.ordinal());
                    dos.writeInt(messageBeanDescriptor.getMetadata().size());
                    for (String propertyName : messageBeanDescriptor.getMetadata().stringPropertyNames()) {
                        dos.writeUTF(propertyName);
                        dos.writeUTF(messageBeanDescriptor.getMetadata().getProperty(propertyName));
                    }
                    try {
                        byte[] data = baos.toByteArray();
                        ze.setSize(data.length);
                        zipStream.putNextEntry(ze);
                        zipStream.write(data, 0, data.length);
                        zipStream.closeEntry();
                        logger.fine("Wrote zip entry " + ze.getName());
                        incrementRecordCount();
                    } catch (Exception e) {
                        logger.warning("Unable to serialize message bean descriptor " + messageBeanDescriptor.toString() + ": " + e.getMessage());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (includeData) {
                    writeRawData(messageBeanManager, messageBeanDescriptor);
                }
            }
        } else {
            Enumeration<? extends ZipEntry> zipFiles = this.zipFile.entries();
            while (zipFiles.hasMoreElements()) {
                ZipEntry entry = zipFiles.nextElement();
                if (entry.getName().startsWith(IMessageBeanDescriptorVisitor.MESSAGE_BEAN_DESCRIPTOR_ENTRY)) {
                    DataInputStream dis = new DataInputStream(zipFile.getInputStream(entry));
                    MessageBeanBuilder builder = null;
                    try {
                        do {
                            int headerID = dis.readInt();
                            SerializationHeader header = SerializationHeader.values()[headerID];
                            switch(header) {
                                case MessageBeanDescriptor:
                                    break;
                                case Namespace:
                                    INamespace beanNamespace = Namespace.fromCanonical(dis.readUTF());
                                    builder = new MessageBeanBuilder(beanNamespace);
                                    break;
                                case DisplayableName:
                                    dis.readUTF();
                                    String description = dis.readUTF();
                                    String beanDescription = (description.equals(Namespace.NULL_TOKEN) ? null : description);
                                    builder.setDescription(beanDescription);
                                    break;
                                case Properties:
                                    int properties = dis.readInt();
                                    for (int i = 0; i < properties; i++) {
                                        dis.readInt();
                                        String ns = dis.readUTF();
                                        INamespace propertyNamespace = Namespace.fromCanonical(ns);
                                        dis.readInt();
                                        dis.readUTF();
                                        String rawPropertyDescription = dis.readUTF();
                                        String propertyDescription = (rawPropertyDescription.equals(Namespace.NULL_TOKEN) ? null : rawPropertyDescription);
                                        dis.readInt();
                                        boolean isPrimaryKeyMember = dis.readBoolean();
                                        boolean isNullable = dis.readBoolean();
                                        int dataTypeOrdinal = dis.readInt();
                                        DataType dataType = DataType.values()[dataTypeOrdinal];
                                        int propLength = dis.readInt();
                                        int propPrecision = dis.readInt();
                                        builder.mapProperty(propertyNamespace.getId(), dataType, propLength, propPrecision, isPrimaryKeyMember, isNullable);
                                        builder.setPropertyDescription(propertyNamespace.getId(), propertyDescription);
                                        dis.readInt();
                                        int metadataEntries = dis.readInt();
                                        for (int j = 0; j < metadataEntries; j++) {
                                            String propKey = dis.readUTF();
                                            String propValue = dis.readUTF();
                                            builder.addPropertyMetadata(propertyNamespace.getId(), propKey, propValue);
                                        }
                                    }
                                    break;
                                case Indexes:
                                    int indexCount = dis.readInt();
                                    logger.warning("Index entry deserialization is not yet supported");
                                    break;
                                case Interfaces:
                                    int intfCount = dis.readInt();
                                    logger.warning("Interface entry deserialization is not yet supported");
                                    break;
                                case Metadata:
                                    int metadataCount = dis.readInt();
                                    for (int k = 0; k < metadataCount; k++) {
                                        String beanPropKey = dis.readUTF();
                                        String beanPropValue = dis.readUTF();
                                        builder.addMetadata(beanPropKey, beanPropValue);
                                    }
                                    break;
                            }
                        } while (true);
                    } catch (EOFException ignore) {
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    IMessageBeanDescriptor messageBeanDescriptor = builder.build();
                    try {
                        messageBeanManager.queueMessageBeanRegistration(messageBeanDescriptor);
                    } catch (UnsupportedMessageBeanException e) {
                        e.printStackTrace();
                    }
                }
            }
            messageBeanManager.processRegistrationQueue();
            if (includeData) {
                readRawData(messageBeanManager);
            }
        }
    }
