    public boolean saveCurrentOntology(String ontology_name) {
        if (!ontology_name.equals(DescriptionEditor.DUBLIN_CORE_DEFAULT_NAME)) {
            String ont_uri = "http://lionshare.its.psu.edu/" + ontology_name;
            String ontology = "<ontology name=\"" + ontology_name + "\" " + "uri=\"" + ont_uri + "\" " + "display=\"" + this.getClass().getName() + "\">";
            for (int i = 0; i < displays.size(); i++) {
                ontology += "<component schema_uri=\"" + displays.get(i).getSchemaURI() + "\"" + " element=\"" + displays.get(i).getElementName() + "\"/>";
            }
            ontology += "</ontology>";
            try {
                manager.addOntology(ontology_name, ontology, false);
            } catch (IOException ioex) {
                LOG.trace("CustomizableMetadataDisplay.saveCurrentOntology(): " + "Ontology already exists", ioex);
                int nChoice = JOptionPane.showConfirmDialog(GUIMediator.getAppFrame(), "<html>The ontology with that name already exists, would you like to " + "overwrite it?<br>Either click Yes to overwrite the file or No to go " + "back to the editor to<br>enter a different ontology name.</html>", "Ontology Exists", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (nChoice == JOptionPane.YES_OPTION) {
                    try {
                        manager.addOntology(ontology_name, ontology, true);
                    } catch (IOException ioex2) {
                        LOG.trace("CustomizableMetadataDisplay.saveCurrentOntology(): " + "Error overwriting ontology", ioex2);
                    }
                } else {
                    return false;
                }
            }
            try {
                MetadataType meta = manager.getMetadata(document_id);
                meta.setOntologyURI(ont_uri);
                manager.updateMetadata(meta, document_id);
            } catch (Exception ex) {
                LOG.trace("CustomizableMetadataDisplay.saveCurrentOntology(): " + "Error updating metadata.", ex);
            }
            return true;
        } else {
            JOptionPane.showMessageDialog(GUIMediator.getAppFrame(), "<html>The default dublic core ontology cannot be overwritten.</html>", "Cannot Overwrite Default Dublin Core Ontology", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }
