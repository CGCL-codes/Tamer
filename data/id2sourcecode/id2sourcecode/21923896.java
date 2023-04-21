    private static String getRightsDescription(OMSRights rights) {
        try {
            return "id = '" + rights.getId() + "', granted to = '" + getElementDescription(rights.getGrantedTo()) + "', can associate = '" + rights.canAssociate() + "', can compose = '" + rights.canCompose() + "', can delete = '" + rights.canDelete() + "', can read = '" + rights.canRead() + "', can write = '" + rights.canWrite() + "', ";
        } catch (Exception e) {
            return "Couldn't get description for the rights: " + e.getMessage();
        }
    }
