    public int periph(int PC, int S, int res) {
        switch(PC) {
            case 0xF42A:
                int x = mem.getX();
                int y = mem.getY();
                mem.write(0x60D6, y >> 8);
                mem.write(0x60D7, y & 0xFF);
                mem.write(0x60D8, x >> 8);
                mem.write(0x60D9, x & 0xFF);
                mem.mouse_upd();
                return res;
            case 0xE0FF:
                InitDiskCtrl();
                return res;
            case 0xE3A8:
                ReadSector();
                break;
            case 0xE178:
                break;
            case 0xFCF5:
                micro.Fetch(0xD4);
                mem.write(0xE7DA, mem.read(0xE7DA));
                return 0;
            case 0x315A:
                micro.Fetch(0x8E);
                break;
            case 0x337E:
            case 0x3F97:
                micro.setX(mem.getX());
                micro.setY(mem.getY());
                res = 0xff;
                break;
        }
        return res;
    }
