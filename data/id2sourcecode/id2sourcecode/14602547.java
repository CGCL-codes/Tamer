        @Override
        public int getAccessRights() {
            boolean read = file.canRead();
            boolean write = file.canWrite();
            return ACCESS_EVERYTHING ^ ((read ? 0 : ACCESS_OWNER_READ | ACCESS_GROUP_READ | ACCESS_ALL_READ | ACCESS_UA_READ) | (write ? 0 : ACCESS_OWNER_WRITE | ACCESS_GROUP_WRITE | ACCESS_ALL_WRITE | ACCESS_UA_WRITE));
        }
