    void updatePermissions() {
        boolean canRead = this.getMappedLocalFile().canRead();
        boolean canWrite = this.getMappedLocalFile().canWrite();
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
