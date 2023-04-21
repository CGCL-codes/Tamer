    private void copyOtherFiles(String startfolder) {
        try {
            File folder = new File(startfolder);
            File[] subresources = folder.listFiles();
            for (int i = 0; i < subresources.length; i++) {
                if (subresources[i].isDirectory()) {
                    copyOtherFiles(subresources[i].getAbsolutePath());
                } else {
                    if (!subresources[i].getName().equals(META_PROPERTIES)) {
                        String vfsFileName = (String) m_fileIndex.get(subresources[i].getAbsolutePath().replace('\\', '/'));
                        int type = getFileType(vfsFileName);
                        if (CmsResourceTypePlain.getStaticTypeId() != type) {
                            if (isExternal(vfsFileName)) {
                                m_report.print(Messages.get().container(Messages.RPT_SKIP_EXTERNAL_0), I_CmsReport.FORMAT_NOTE);
                                m_report.print(org.opencms.report.Messages.get().container(org.opencms.report.Messages.RPT_ARGUMENT_1, subresources[i]));
                                m_report.print(org.opencms.report.Messages.get().container(org.opencms.report.Messages.RPT_DOTS_0));
                                m_report.print(Messages.get().container(Messages.RPT_ARROW_RIGHT_0), I_CmsReport.FORMAT_NOTE);
                                m_report.println(org.opencms.report.Messages.get().container(org.opencms.report.Messages.RPT_ARGUMENT_1, vfsFileName));
                            } else {
                                m_report.print(Messages.get().container(Messages.RPT_IMPORT_0), I_CmsReport.FORMAT_NOTE);
                                m_report.print(org.opencms.report.Messages.get().container(org.opencms.report.Messages.RPT_ARGUMENT_1, vfsFileName));
                                m_report.print(org.opencms.report.Messages.get().container(org.opencms.report.Messages.RPT_DOTS_0));
                                byte[] content = getFileBytes(subresources[i]);
                                List properties = new ArrayList();
                                String altText = (String) m_imageInfo.get(subresources[i].getAbsolutePath().replace('\\', '/'));
                                CmsProperty property1 = new CmsProperty(CmsPropertyDefinition.PROPERTY_DESCRIPTION, altText, altText);
                                CmsProperty property2 = new CmsProperty(CmsPropertyDefinition.PROPERTY_TITLE, altText, altText);
                                if (altText != null) {
                                    properties.add(property1);
                                    properties.add(property2);
                                }
                                if (!m_overwrite) {
                                    m_cmsObject.createResource(vfsFileName, type, content, properties);
                                } else {
                                    try {
                                        CmsLock lock = m_cmsObject.getLock(vfsFileName);
                                        if (lock.getType() != CmsLockType.EXCLUSIVE) {
                                            m_cmsObject.lockResource(vfsFileName);
                                        }
                                        m_cmsObject.deleteResource(vfsFileName, CmsResource.DELETE_PRESERVE_SIBLINGS);
                                    } catch (CmsException e) {
                                    } finally {
                                        m_cmsObject.createResource(vfsFileName, type, content, properties);
                                    }
                                    m_report.print(Messages.get().container(Messages.RPT_OVERWRITE_0), I_CmsReport.FORMAT_NOTE);
                                    m_report.print(org.opencms.report.Messages.get().container(org.opencms.report.Messages.RPT_DOTS_0));
                                }
                                m_report.println(org.opencms.report.Messages.get().container(org.opencms.report.Messages.RPT_OK_0), I_CmsReport.FORMAT_OK);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            m_report.println(e);
        }
    }
