    public static void exportScormZip(Content content, ZipOutputStream zipOutStream) {
        try {
            zipOutStream.putNextEntry(new ZipEntry(IMSMANIFEST_FILE_NAME));
            JCRXMLConverter converter = new JCRXMLConverter();
            Node contentNode = JCRUtil.getNodeById(content.getId());
            Node manifestNode = contentNode.getNode(JCRUtil.MANIFEST_PREFIX);
            Document document = converter.convertToDom(manifestNode);
            Element manifestDomElement = (Element) document.getFirstChild();
            appendDefaultManifestAttributes(manifestDomElement);
            Organizations organizations = content.getManifest().getOrganizations();
            if (organizations.size() == 0) {
                createDefaultOrganizationsAndResources(document, content.getName());
            } else {
                addOrganizationIdentifiers(document);
            }
            Element resourcesElement = getElement(document, IMSConstants.RESOURCES);
            if (!resourcesElement.hasChildNodes()) {
                addResourcesFromOrganizations(content, document, organizations, resourcesElement);
            }
            replaceIdentifiers(document);
            addLomNameSpace(document);
            convertStringLanguageValues(document);
            removeUnnecesaryAttributes(document);
            reorganizeManifestChildElements(document);
            JCRXMLConverter.writeXmlDocument(document, zipOutStream);
            zipOutStream.closeEntry();
            DirectoryFolder resourcesFolder = content.getResourcesFolder();
            addDirectoryItemsToZip(resourcesFolder, "", zipOutStream);
            addScormDocumentControlFiles(zipOutStream);
        } catch (ItemNotFoundException e) {
            String errorMessage = "There is no jcr node with the id of the content: " + content.getId();
            log.error(errorMessage, e);
            throw new CMSRuntimeException(errorMessage, e);
        } catch (Exception e) {
            String errorMessage = "Error exporting to scorm zip the content " + content.getName() + " with id " + content.getId();
            log.error(errorMessage);
            throw new CMSRuntimeException(errorMessage, e);
        }
    }
