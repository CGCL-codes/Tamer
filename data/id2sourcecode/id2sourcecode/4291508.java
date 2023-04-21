        public Address hash(K key, List<Address> members) {
            int hash = Math.abs(key.hashCode());
            int index = hash % HASH_SPACE;
            if (members != null && !members.isEmpty()) {
                Object[] tmp = new Object[nodes.length];
                System.arraycopy(nodes, 0, tmp, 0, nodes.length);
                for (int i = 0; i < tmp.length; i += 2) {
                    if (!members.contains(tmp[i + 1])) {
                        tmp[i] = tmp[i + 1] = null;
                    }
                }
                return findFirst(tmp, index);
            }
            return findFirst(nodes, index);
        }
