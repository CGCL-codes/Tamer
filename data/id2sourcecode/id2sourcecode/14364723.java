    public ResponseObject moveVocabularyNode(Identification id, String requestId, MedicalNode node, String destNodePath, Options options) {
        ResponseObject response = new ResponseObject(requestId);
        response.setStatusCode(StatusCode.ok);
        MedicalNode destNode = null;
        try {
            destNode = getNodeByPath(destNodePath, requestId);
            if (hasNamespaceWriteAccess(node.getNamespaceId(), id.getProfileId(), requestId) && hasNamespaceWriteAccess(destNode.getNamespaceId(), id.getProfileId(), requestId)) {
                if (options != null && options.synonymize()) {
                    try {
                        this.enrichNodeWithSynonyms(requestId, node);
                    } catch (Exception ex) {
                        log.warn(MessageFormat.format("{0}:{1}: Error communicating with Lemmatisation service", requestId, StatusCode.unknown_error.code()));
                    }
                }
                medicalTaxonomyService.moveNode(node, destNode);
                medicalTaxonomyService.setLastChangeNow(destNode.getNamespaceId());
            } else {
                response.setErrorMessage("The profile is invalid or does not have read or write privileges to target namespace");
                response.setStatusCode(StatusCode.insufficient_namespace_privileges);
                log.warn(MessageFormat.format("{0}:{1}: The profileId {2} is invalid or does not have read privileges to namespace with id {3}", requestId, StatusCode.insufficient_namespace_privileges.code(), id.getProfileId(), node.getNamespaceId()));
            }
        } catch (DTSException ex) {
            response.setStatusCode(StatusCode.error_editing_taxonomy);
            response.setErrorMessage(requestId);
        } catch (KeywordsException ex) {
            response.setStatusCode(StatusCode.error_editing_taxonomy);
            response.setErrorMessage("Error editing taxonomy: Node could not be moved");
            log.error(MessageFormat.format("{0}:{1}: Node ({2}) could not be moved to parent {{3}}", requestId, StatusCode.error_editing_taxonomy.code(), node.getInternalId(), destNode.getInternalId()), ex);
        } catch (NodeNotFoundException ex) {
            response.setStatusCode(StatusCode.error_editing_taxonomy);
            response.setErrorMessage("Error editing taxonomy: Node could not be found");
            log.error(MessageFormat.format("{0}:{1}:{2}", requestId, StatusCode.error_editing_taxonomy.code(), ex.getMessage()), ex);
        } catch (Exception ex) {
            response.setStatusCode(StatusCode.error_editing_taxonomy);
            response.setErrorMessage("Error editing taxonomy: Node could not be found");
            log.error(MessageFormat.format("{0}:{1}:{2}", requestId, StatusCode.error_editing_taxonomy.code(), ex.getMessage()), ex);
        }
        return response;
    }
