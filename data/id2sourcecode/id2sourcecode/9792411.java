    public AttachmentResult(long retCode, String retCodeDescription, String action, String mime_type, String object_id, String object_id_on_storage, String storage_system_id, long bytes_write, long bytes_read, String[] meta_on_db, String[] meta_on_storage) {
        this.retCode = retCode;
        this.retCodeDescription = retCodeDescription;
        this.action = action;
        this.mime_type = mime_type;
        this.object_id = object_id;
        this.object_id_on_storage = object_id_on_storage;
        this.storage_system_id = storage_system_id;
        this.bytes_write = bytes_write;
        this.bytes_read = bytes_read;
        this.meta_on_db = meta_on_db;
        this.meta_on_storage = meta_on_storage;
    }
