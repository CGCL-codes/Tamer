    @Override
    public IWorkbook addNewWorkbook(DataSourceConfigurationId dataSourceConfigId, WorkbookId workbookId) throws StorageException, NotFoundException, AlreadyExistsException {
        boolean wasSet = AclPrivilegedMode.set();
        IWorkbook workbook = null;
        synchronized (workbooks) {
            try {
                if (workbooks.get(workbookId) != null) {
                    throw new AlreadyExistsException();
                }
                workbook = storageService.add(dataSourceConfigId, workbookId);
                workbooks.put(workbookId, (Workbook) workbook);
                aclService.setPermissions(workbookId, null, Permission.read, Permission.write);
                aclService.setPermissions(new SheetFullName(workbookId, SheetNames.PERMISSIONS), null, Permission.read, Permission.write);
            } finally {
                if (!wasSet) {
                    AclPrivilegedMode.clear();
                }
            }
        }
        return workbook;
    }
