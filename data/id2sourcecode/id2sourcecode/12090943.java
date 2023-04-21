    public long findNameExport(long exportAddress, long exportsSize, String name, int hint) {
        loadExports(exportAddress);
        if (hint >= 0 && hint < exports.NumberOfFunctions) {
            int address = (int) (baseAddress + exports.AddressOfNames + 4 * hint);
            int nameAddress = Memory.mem_readd(address) + baseAddress;
            String possibleMatch = new LittleEndianFile(nameAddress).readCString();
            if (possibleMatch.equalsIgnoreCase(name)) {
                int ordinal = Memory.mem_readw((int) (baseAddress + exports.AddressOfNameOrdinals + 2 * hint));
                return findOrdinalExport(exportAddress, exportsSize, ordinal + (int) exports.Base);
            }
        }
        int min = 0;
        int max = (int) exports.NumberOfFunctions - 1;
        while (min <= max) {
            int res, pos = (min + max) / 2;
            int address = (int) (baseAddress + exports.AddressOfNames + 4 * pos);
            int nameAddress = Memory.mem_readd(address) + baseAddress;
            String possibleMatch = new LittleEndianFile(nameAddress).readCString();
            if ((res = possibleMatch.compareTo(name)) == 0) {
                int ordinal = Memory.mem_readw((int) (baseAddress + exports.AddressOfNameOrdinals + 2 * pos));
                return findOrdinalExport(exportAddress, exportsSize, ordinal + (int) exports.Base);
            }
            if (res > 0) max = pos - 1; else min = pos + 1;
        }
        return 0;
    }
