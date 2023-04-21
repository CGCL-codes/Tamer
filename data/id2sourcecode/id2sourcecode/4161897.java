    public Warrior(String name, int codeSize, RealModeMemory core, RealModeAddress loadAddress, RealModeAddress initialStack, RealModeAddress groupSharedMemory, short groupSharedMemorySize) {
        m_name = name;
        m_codeSize = codeSize;
        m_loadAddress = loadAddress;
        m_state = new CpuState();
        initializeCpuState(loadAddress, initialStack, groupSharedMemory);
        RealModeAddress lowestStackAddress = new RealModeAddress(initialStack.getSegment(), (short) 0);
        RealModeAddress lowestCoreAddress = new RealModeAddress(loadAddress.getSegment(), (short) 0);
        RealModeAddress highestCoreAddress = new RealModeAddress(loadAddress.getSegment(), (short) -1);
        RealModeAddress highestGroupSharedMemoryAddress = new RealModeAddress(groupSharedMemory.getSegment(), (short) (groupSharedMemorySize - 1));
        RealModeMemoryRegion[] readAccessRegions = new RealModeMemoryRegion[] { new RealModeMemoryRegion(lowestStackAddress, initialStack), new RealModeMemoryRegion(lowestCoreAddress, highestCoreAddress), new RealModeMemoryRegion(groupSharedMemory, highestGroupSharedMemoryAddress) };
        RealModeMemoryRegion[] writeAccessRegions = new RealModeMemoryRegion[] { new RealModeMemoryRegion(lowestStackAddress, initialStack), new RealModeMemoryRegion(lowestCoreAddress, highestCoreAddress), new RealModeMemoryRegion(groupSharedMemory, highestGroupSharedMemoryAddress) };
        RealModeMemoryRegion[] executeAccessRegions = new RealModeMemoryRegion[] { new RealModeMemoryRegion(lowestCoreAddress, highestCoreAddress) };
        m_memory = new RestrictedAccessRealModeMemory(core, readAccessRegions, writeAccessRegions, executeAccessRegions);
        m_cpu = new Cpu(m_state, m_memory);
        m_isAlive = true;
    }
