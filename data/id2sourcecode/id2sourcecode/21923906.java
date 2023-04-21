    private static String updateAuthorisation(DataSource dataSource, OMSRights rights, int userId) throws Exception {
        try {
            CallResult result = executeOnResource(dataSource, null, new BoundVariable[] { new BoundVariable("out_int_id", null), new BoundVariable("in_int_aut_id", new Integer(rights.getId())), new BoundVariable("in_string_read", getRight(rights.canRead())), new BoundVariable("in_string_write", getRight(rights.canWrite())), new BoundVariable("in_string_compose", getRight(rights.canCompose())), new BoundVariable("in_string_associate", getRight(rights.canAssociate())), new BoundVariable("in_string_delete", getRight(rights.canDelete())), new BoundVariable("in_int_modifying_elm_id", new Integer(userId)) }, RESOURCE_UPDATE_AUTHORISATION);
            return result.getBoundVariable("out_int_id").getValue().toString();
        } catch (Throwable t) {
            throw new Exception("Error while creating rights " + getRightsDescription(rights) + ": " + t.getMessage(), t);
        }
    }
