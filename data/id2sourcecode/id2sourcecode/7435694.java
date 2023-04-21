        public long getClassProfileTotal(final Object target, final boolean read) {
            long val = -1;
            if (next == null) {
                return val;
            }
            val = next.getClassProfileTotal(target, read);
            if (type.isInstance(target)) {
                if (type == target.getClass()) {
                    return read ? readProfile & val : writeProfile & val;
                } else {
                    return read ? readProfile & val : writeProfile & val;
                }
            }
            return -1 & val;
        }
