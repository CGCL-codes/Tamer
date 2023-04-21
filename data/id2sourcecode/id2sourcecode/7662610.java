    @SuppressWarnings("static-access")
    public void modifyGroupInPermissionList(String path, Listitem item, boolean read, boolean write) {
        if (this.checkRevision()) {
            path = preparePath(path);
            AccessGroup tempTarget = new AccessGroup();
            tempTarget.setName(item.getLabel());
            DirAccess dirA = new DirAccess();
            dirA.setReadPermission(read);
            dirA.setWritePermission(write);
            dirA.setTargets(tempTarget);
            try {
                ras.setUserOrGroupRights(path, dirA);
                RepositoryAccessService.updateRevision();
                Sessions.getCurrent().setAttribute("user_rev", RepositoryAccessService.getRevision());
            } catch (ModifyPermissionException e) {
                String message = e.getClass().getCanonicalName() + " " + e.getLocalizedMessage();
                for (StackTraceElement element : e.getStackTrace()) {
                    message += element.toString();
                }
                GlobalProperties.getMyLogger().severe(message);
                log.info(e);
            }
        }
    }
