    @Override
    public void visit(IProjectManager projectManager) throws IOException {
        Assert.parameterNotNull(projectManager, "Parameter 'projectManager' must not be null");
        if (getDirection() == IPackagingVisitor.DIRECTION_OUT) {
            if (!projectManager.isProjectOpen()) {
                throw new IllegalStateException("Cannot export a project when no project is open");
            }
            IProjectDescriptor projectDescriptor = projectManager.getCurrentProject();
            try {
                ZipEntry ze = new ZipEntry(PROJECT_DESCRIPTOR_ENTRY);
                ze.setComment((null == projectDescriptor.getDescription()) ? Namespace.NULL_TOKEN : projectDescriptor.getDescription());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(baos);
                dos.writeInt(SerializationHeader.ProjectDescriptor.ordinal());
                dos.writeInt(SerializationHeader.Namespace.ordinal());
                dos.writeUTF(Namespace.toCanonical(projectDescriptor, true));
                dos.writeInt(SerializationHeader.DisplayableName.ordinal());
                dos.writeUTF(projectDescriptor.getName());
                dos.writeUTF((null == projectDescriptor.getDescription()) ? Namespace.NULL_TOKEN : projectDescriptor.getDescription());
                dos.writeInt(SerializationHeader.PropertyGroups.ordinal());
                dos.writeInt(projectDescriptor.listPropertyGroups().size());
                for (String groupKey : projectDescriptor.listPropertyGroups()) {
                    dos.writeInt(SerializationHeader.PropertyGroup.ordinal());
                    dos.writeUTF(groupKey);
                    List<String> groupKeys = projectDescriptor.listPropertyGroupKeys(groupKey);
                    dos.writeInt(SerializationHeader.Properties.ordinal());
                    dos.writeInt(groupKeys.size());
                    for (String propertyName : groupKeys) {
                        dos.writeUTF(propertyName);
                        dos.writeUTF(projectDescriptor.getProperty(groupKey, propertyName));
                    }
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
                    logger.warning("Unable to serialize project descriptor " + projectDescriptor.toString() + ": " + e.getMessage());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Enumeration<? extends ZipEntry> zipFiles = this.zipFile.entries();
            ZipEntry projectDescriptorZipEntry = null;
            while (zipFiles.hasMoreElements()) {
                ZipEntry entry = zipFiles.nextElement();
                if (entry.getName().equals(IProjectManagerVisitor.PROJECT_DESCRIPTOR_ENTRY)) {
                    projectDescriptorZipEntry = entry;
                    break;
                }
            }
            if (null == projectDescriptorZipEntry) {
                throw new IOException("Invalid package file; no project descriptor entry found");
            }
            InputStream is = zipFile.getInputStream(projectDescriptorZipEntry);
            DataInputStream dis = new DataInputStream(is);
            try {
                String currentGroupKey = null;
                do {
                    int headerID = dis.readInt();
                    SerializationHeader header = SerializationHeader.values()[headerID];
                    switch(header) {
                        case ProjectDescriptor:
                            break;
                        case Namespace:
                            INamespace projectNamespace = Namespace.fromCanonical(dis.readUTF());
                            IProjectDescriptor projectDescriptor = new ProjectDescriptor(projectNamespace);
                            projectManager.create(projectDescriptor);
                            break;
                        case DisplayableName:
                            dis.readUTF();
                            String description = dis.readUTF();
                            String projectDescription = (description.equals(Namespace.NULL_TOKEN) ? null : description);
                            projectManager.setCurrentProjectDescription(projectDescription);
                            break;
                        case PropertyGroups:
                            dis.readInt();
                            break;
                        case PropertyGroup:
                            currentGroupKey = dis.readUTF();
                            break;
                        case Properties:
                            String propertyKey = dis.readUTF();
                            String propertyValue = dis.readUTF();
                            projectManager.setCurrentProjectProperty(currentGroupKey, propertyKey, propertyValue);
                            break;
                    }
                } while (true);
            } catch (EOFException ignore) {
            } catch (IOException e) {
                e.printStackTrace();
            } catch (org.proteusframework.platformservice.persistence.project.ProjectInitializationException e) {
                e.printStackTrace();
            }
            IProjectDescriptor curDescriptor = projectManager.getCurrentProject();
            logger.info("New project created from package: " + curDescriptor);
        }
    }
