    void debugDump(final StringBuffer out) {
        if (out != null) {
            out.append(getClass().getName().concat("@").concat(Integer.toHexString(System.identityHashCode(this))));
            out.append(EOL);
            out.append("size = " + m_size + ", bucket table size = " + m_buckets.length + ", load factor = " + m_loadFactor + EOL);
            out.append("size threshold = " + m_sizeThreshold + ", get clear frequency = " + m_readClearCheckFrequency + ", put clear frequency = " + m_writeClearCheckFrequency + EOL);
            out.append("get count: " + m_readAccessCount + ", put count: " + m_writeAccessCount + EOL);
        }
    }
