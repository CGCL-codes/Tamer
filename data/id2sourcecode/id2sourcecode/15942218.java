        public void handler(osd_bitmap bitmap) {
            int offs;
            for (offs = videoram_size[0] - 1; offs >= 0; offs--) {
                if (dirtybuffer[offs] != 0) {
                    int sx, sy;
                    dirtybuffer[offs] = 0;
                    sx = (offs / 32);
                    sy = (31 - offs % 32);
                    drawgfx(tmpbitmap, Machine.gfx[0], (videoram.read(offs) & 0x3f) + 0x40, 0, 0, 0, 8 * (sx + 1), 8 * sy, Machine.drv.visible_area, TRANSPARENCY_NONE, 0);
                }
            }
            copybitmap(bitmap, tmpbitmap, 0, 0, 0, 0, Machine.drv.visible_area, TRANSPARENCY_NONE, 0);
            for (offs = 0; offs < 0x10; offs++) {
                if (spriteram.read(offs + 0x20) < 0xf8) {
                    int spritenum, color;
                    spritenum = spriteram.read(offs) & 0x3f;
                    if ((spritenum & 1) != 0) spritenum = spritenum / 2 + 64; else spritenum = spritenum / 2;
                    color = spriteram.read(offs + 0x30);
                    Machine.gfx[1].colortable.write(3, Machine.pens[15 - centiped_spritepalette.read((color >> 4) & 3)]);
                    Machine.gfx[1].colortable.write(2, Machine.pens[15 - centiped_spritepalette.read((color >> 2) & 3)]);
                    Machine.gfx[1].colortable.write(1, Machine.pens[15 - centiped_spritepalette.read((color >> 0) & 3)]);
                    drawgfx(bitmap, Machine.gfx[1], spritenum, 0, spriteram.read(offs) & 0x80, 0, 248 - spriteram.read(offs + 0x10), 248 - spriteram.read(offs + 0x20), Machine.drv.visible_area, TRANSPARENCY_PEN, 0);
                }
            }
        }
