    public RestrictedAccessRealModeMemory(RealModeMemory memory, RealModeMemoryRegion[] readAccessRegions, RealModeMemoryRegion[] writeAccessRegions, RealModeMemoryRegion[] executeAccessRegions) {
        m_memory = memory;
        m_readAccessRegions = readAccessRegions;
        m_writeAccessRegions = writeAccessRegions;
        m_executeAccessRegions = executeAccessRegions;
    }
