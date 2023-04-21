        public ClassProfiles addClassProfile(final Class c, final long read, final long write) {
            if (next != null) {
                if (type == c) {
                    readProfile = readProfile | read;
                    writeProfile = writeProfile | write;
                    return this;
                }
                next = next.addClassProfile(c, read, write);
                return this;
            }
            ClassProfiles result = new ClassProfiles();
            result.type = c;
            result.next = this;
            result.readProfile = read;
            result.writeProfile = write;
            return result;
        }
