    void updatePermissions() {
        java.io.File mappedLocalDirectory = new java.io.File(this._absoluteDirectoryName);
        boolean canRead = mappedLocalDirectory.canRead();
        boolean canWrite = mappedLocalDirectory.canWrite();
        if (canRead && canWrite) {
            this._permissions = "read/write";
        }
        if (canRead && !canWrite) {
            this._permissions = "read-only";
        }
        if (!canRead && canWrite) {
            this._permissions = "write-only";
        }
        if (!canRead && !canWrite) {
            this._permissions = "no access";
        }
    }
