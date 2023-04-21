    public void addReference(StringEntry ref) {
        String s = String.valueOf(ref.digest());
        if (!m_refTree.containsKey(s)) m_refTree.put(s, ref);
    }
