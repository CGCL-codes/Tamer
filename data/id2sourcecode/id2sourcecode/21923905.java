    private static String createAuthorisation(DataSource dataSource, OMSRights rights, OMSElement grantedFor, int userId) throws Exception {
        try {
            CallResult result = executeOnResource(dataSource, null, new BoundVariable[] { new BoundVariable("out_int_id", null), new BoundVariable("in_int_source_elm_id", new Integer(rights.getGrantedTo().getId())), new BoundVariable("in_int_target_elm_id", new Integer(grantedFor.getId())), new BoundVariable("in_string_read", getRight(rights.canRead())), new BoundVariable("in_string_write", getRight(rights.canWrite())), new BoundVariable("in_string_compose", getRight(rights.canCompose())), new BoundVariable("in_string_associate", getRight(rights.canAssociate())), new BoundVariable("in_string_delete", getRight(rights.canDelete())), new BoundVariable("in_int_creating_elm_id", new Integer(userId)) }, RESOURCE_CREATE_AUTHORISATION);
            String id = result.getBoundVariable("out_int_id").getValue().toString();
            rights.setId(id);
            return id;
        } catch (Throwable t) {
            throw new Exception("Error while creating rights " + getRightsDescription(rights) + ": " + t.getMessage(), t);
        }
    }
