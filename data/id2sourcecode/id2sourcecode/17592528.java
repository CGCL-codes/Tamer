        public int handler(int offset) {
            int offs;
            offs = taito_gfxpointer.read(0) + taito_gfxpointer.read(1) * 256;
            taito_gfxpointer.write(0, taito_gfxpointer.read(0) + 1 & 0xFF);
            if (taito_gfxpointer.read(0) == 0) taito_gfxpointer.write(1, taito_gfxpointer.read(1) + 1 & 0xFF);
            if (offs < 0x8000) return Machine.memory_region[2][offs];
            return 0;
        }
