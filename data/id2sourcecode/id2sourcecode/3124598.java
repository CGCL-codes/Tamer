        public void handler(osd_bitmap bitmap) {
            int offs, pom;
            int scrollx, scrolly;
            int i, col;
            for (pom = 0; pom < 0x50; pom++) {
                if (pal_dirty[pom] != 0) {
                    Machine.gfx[0].colortable.write(pom * 16, 0);
                    for (i = 1; i < 16; i++) {
                        col = (r_palram[pom * 32 + i * 2 + 1] >> 2) & 0x07;
                        col |= (r_palram[pom * 32 + i * 2 + 1] >> 4) & 0x08;
                        col |= (r_palram[pom * 32 + i * 2] << 4) & 0x30;
                        col |= (r_palram[pom * 32 + i * 2] << 1) & 0xc0;
                        if (col == 0) col = 1;
                        Machine.gfx[0].colortable.write(pom * 16 + i, Machine.gfx[4].colortable.read(col));
                    }
                }
            }
            for (pom = 0x0; pom < 0x4000; pom += 4) {
                offs = pom >> 2;
                if (dirty1[offs] != 0 || pal_dirty[r_vidram1[pom + 1] & 0x7f] != 0) {
                    int sx, sy;
                    int num, bank;
                    num = ((r_vidram1[pom + 2] << 8) + r_vidram1[pom + 3]) & 0x1fff;
                    bank = (r_vidram1[pom + 2] & 0x20) >> 5;
                    dirty1[offs] = 0;
                    sx = 8 * (offs % 64);
                    sy = 8 * (offs / 64);
                    drawgfx(tmpbitmap2, Machine.gfx[bank], num, r_vidram1[pom + 1] & 0x7f, r_vidram1[pom] & 0x40, r_vidram1[pom] & 0x80, sx, sy, visiblearea, TRANSPARENCY_NONE, 0);
                }
            }
            for (pom = 0x0; pom < 0x4000; pom += 4) {
                offs = pom >> 2;
                if (dirty3[offs] != 0 || pal_dirty[r_vidram3[pom + 1] & 0x7f] != 0) {
                    int sx, sy;
                    int num, bank;
                    num = ((r_vidram3[pom + 2] << 8) + r_vidram3[pom + 3]) & 0x1fff;
                    bank = (r_vidram3[pom + 2] & 0x20) >> 5;
                    dirty3[offs] = 0;
                    sx = 8 * (offs % 64);
                    sy = 8 * (offs / 64);
                    drawgfx(tmpbitmap3, Machine.gfx[bank], num, r_vidram3[pom + 1] & 0x7f, r_vidram3[pom] & 0x40, r_vidram3[pom] & 0x80, sx, sy, visiblearea, TRANSPARENCY_NONE, 0);
                }
            }
            for (pom = 0; pom < 0x80; pom++) pal_dirty[pom] = 0;
            scrollx = (scrollX[0] << 8) + scrollX[1] - 16;
            scrolly = (scrollY[0] << 8) + scrollY[1] - 8;
            copyscrollbitmap(bitmap, tmpbitmap2, 1, new int[] { scrollx }, 1, new int[] { scrolly }, Machine.drv.visible_area, TRANSPARENCY_NONE, 0);
            scrollx = (scrollX[2] << 8) + scrollX[3] - 16;
            scrolly = (scrollY[2] << 8) + scrollY[3] - 8;
            copyscrollbitmap(bitmap, tmpbitmap3, 1, new int[] { scrollx }, 1, new int[] { scrolly }, Machine.drv.visible_area, TRANSPARENCY_PEN, 0);
            for (offs = 0x800 - 8; offs >= 0; offs -= 8) {
                int sx1, sy1, col1;
                int num1, bank1, pom1;
                sx1 = (r_sprram[offs + 6] << 8) | r_sprram[offs + 7];
                sy1 = (r_sprram[offs + 2] << 8) | r_sprram[offs + 3];
                sx1 = sx1 < 32768 ? sx1 & 0x1ff : -((65536 - sx1) & 0x1ff);
                sy1 = sy1 < 32768 ? sy1 & 0x1ff : -((65536 - sy1) & 0x1ff);
                num1 = (r_sprram[offs + 4] << 8) | r_sprram[offs + 5];
                if (num1 != 0 && (sx1 < 320) && (sy1 < 240)) {
                    num1 &= 0x3ff;
                    bank1 = (r_sprram[offs + 4] >> 3) & 0x01;
                    pom1 = (r_sprram[offs + 4] >> 2) & 0x01;
                    if (pom1 != 0) {
                        num1 = 0x40;
                        bank1 = 0;
                    }
                    col1 = r_sprram[offs + 1];
                    if (col1 < 0x40) col1 = col1 | 0x30;
                    if (offs / 8 >= 0xbe) col1 = ((col1 & 0x3f) | 0x30);
                    drawgfx(bitmap, Machine.gfx[2 + bank1], num1, col1, r_sprram[offs] & 0x40, 0, sx1, sy1 - 8, Machine.drv.visible_area, TRANSPARENCY_PEN, 0);
                }
            }
        }
