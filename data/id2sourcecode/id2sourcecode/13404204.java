    public String getMessage(int permissionID) {
        String message = "Wifi Music Sync recieved permissionID: " + permissionID;
        switch(permissionID) {
            case ApplicationPermissions.PERMISSION_FILE_API:
                message = "Wifi Music Sync needs this permission to read and write files.";
                break;
            case ApplicationPermissions.PERMISSION_INTERNET:
                message = "Wifi Music Sync needs this permission to send and recieve data with the sync server.";
                break;
            case ApplicationPermissions.PERMISSION_WIFI:
                message = "Wifi Music Sync needs this permission to send and recieve data with the sync server.";
                break;
            case ApplicationPermissions.PERMISSION_CROSS_APPLICATION_COMMUNICATION:
                message = "Wifi Music Sync needs this permission to lauch the browser for various online links.";
                break;
            case ApplicationPermissions.PERMISSION_DEVICE_SETTINGS:
                message = "Wifi Music Sync needs this permission to read your device PIN to generate a sync client id.";
                break;
        }
        return message;
    }
