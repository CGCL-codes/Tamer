    public synchronized ISheet addNewSheet(String sheetName, SheetType type) throws AlreadyExistsException, StorageException, NotFoundException {
        getAclService().checkPermission(id, Permission.write);
        if ((sheetName == null) || sheetName.isEmpty()) {
            throw new IllegalArgumentException("The sheet has to have a non-empty name");
        }
        if (sheetsByName.containsKey(sheetName)) {
            throw new AlreadyExistsException("A sheet with the name [" + sheetName + "] already exists in this workbook");
        }
        SheetData sheetData = new SheetData(new SheetFullName(id, sheetName), type);
        ISheet sheet = createSheetActorRef(sheetData);
        sheetsByName.put(sheetName, sheet);
        getAclService().setPermissions(sheetData.getFullName(), getUserService().getCurrentUser(), Permission.read, Permission.write);
        eventSupport.fireEvent(new SheetEvent(SheetEventType.inserted, sheetData, Collections.<SheetData.Property>emptyList()));
        return sheet;
    }
