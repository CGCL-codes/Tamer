    public synchronized void paint(Graphics g) {
        int i, j;
        int bw = img_board.getWidth(), bh = img_board.getHeight();
        int bx = (width - bw) / 2, by = (height - (bh + 2 + img_score.getHeight())) / 2;
        int sw = img_score.getWidth(), sh = img_score.getHeight();
        int tx = (width - img_turns.getWidth()) / 2;
        int cw = img_controls.getWidth(), cw2 = cw / 2, ch = img_controls.getHeight();
        graphics.setClip(0, 0, width, height);
        graphics.setColor(BGCOLOR);
        graphics.fillRect(0, 0, width, height);
        graphics.drawImage(img_board, bx, by, Graphics.TOP | Graphics.LEFT);
        graphics.drawImage(img_score, bx + 3, by + bh + 2, Graphics.TOP | Graphics.LEFT);
        graphics.drawImage(img_score, bx + bw - sw - 3, by + bh + 2, Graphics.TOP | Graphics.LEFT);
        graphics.drawImage(img_coin_score[0], bx + 3 + sw + 3, by + bh + 4, Graphics.TOP | Graphics.LEFT);
        graphics.drawImage(img_coin_score[1], bx + bw - sw - 6 - img_coin_score[1].getWidth(), by + bh + 4, Graphics.TOP | Graphics.LEFT);
        graphics.drawImage(img_turns, tx, by + bh + 2, Graphics.TOP | Graphics.LEFT);
        for (j = 0; j < 8; j++) for (i = 0; i < 8; i++) {
            if (work[i + j * 8] != BLANK) graphics.drawImage(img_coin[work[i + j * 8]], bx + 6 + i * 20, by + 6 + j * 20, Graphics.TOP | Graphics.LEFT); else if (player[color] == PLAYER_HUMAN && check(i, j, color)) graphics.drawImage(img_dot, bx + 12 + i * 20, by + 12 + j * 20, Graphics.TOP | Graphics.LEFT);
        }
        graphics.drawImage(img_cursor, bx + 6 + x * 20, by + 6 + y * 20, Graphics.TOP | Graphics.LEFT);
        if (marker_x != -1 && marker_y != -1) graphics.drawImage(img_marker, bx + 9 + marker_x * 20, by + 9 + marker_y * 20, Graphics.TOP | Graphics.LEFT);
        if (blackcount >= 10) graphics.drawRegion(img_digits, (blackcount / 10) * 12, 0, 12, 21, Sprite.TRANS_NONE, bx + 5, by + bh + 4, Graphics.TOP | Graphics.LEFT);
        graphics.drawRegion(img_digits, (blackcount % 10) * 12, 0, 12, 21, Sprite.TRANS_NONE, bx + 17, by + bh + 4, Graphics.TOP | Graphics.LEFT);
        if (whitecount >= 10) graphics.drawRegion(img_digits, (whitecount / 10) * 12, 0, 12, 21, Sprite.TRANS_NONE, bx + bw - sw + 1, by + bh + 4, Graphics.TOP | Graphics.LEFT);
        graphics.drawRegion(img_digits, (whitecount % 10) * 12, 0, 12, 21, Sprite.TRANS_NONE, bx + bw - sw + 13, by + bh + 4, Graphics.TOP | Graphics.LEFT);
        i = (turn + 1) / 2;
        if (turn >= 100) graphics.drawRegion(img_digits, (i / 100) * 12, 0, 12, 21, Sprite.TRANS_NONE, tx + 2, by + bh + 4, Graphics.TOP | Graphics.LEFT);
        if (turn >= 10) graphics.drawRegion(img_digits, ((i / 10) % 10) * 12, 0, 12, 21, Sprite.TRANS_NONE, tx + 14, by + bh + 4, Graphics.TOP | Graphics.LEFT);
        graphics.drawRegion(img_digits, (i % 10) * 12, 0, 12, 21, Sprite.TRANS_NONE, tx + 26, by + bh + 4, Graphics.TOP | Graphics.LEFT);
        if (menu != MENU_NONE) {
            int mx = width - 107 - 12, my = height - ch - 12 - menus[menu].length * 11;
            int mw = img_menu.getWidth(), mh = img_menu.getHeight();
            graphics.drawRegion(img_menu, 0, 0, 113, 6, Sprite.TRANS_NONE, mx, my, Graphics.TOP | Graphics.LEFT);
            graphics.drawRegion(img_menu, mw - 6, 0, 6, height - my - ch - 6, Sprite.TRANS_NONE, width - 6, my, Graphics.TOP | Graphics.LEFT);
            graphics.drawRegion(img_menu, 0, mh - (height - my - ch - 6), 6, height - my - ch - 6, Sprite.TRANS_NONE, mx, my + 6, Graphics.TOP | Graphics.LEFT);
            graphics.drawRegion(img_menu, mw - 113, mh - 6, 113, 6, Sprite.TRANS_NONE, mx + 6, height - ch - 6, Graphics.TOP | Graphics.LEFT);
            for (i = 0; i < menus[menu].length; i++) {
                j = menus[menu][i];
                if (j == MENUITEM_LEVEL_EASY) j += level; else if (j == MENUITEM_BLACK_HUMAN) j += player[BLACK]; else if (j == MENUITEM_WHITE_HUMAN) j += player[WHITE];
                graphics.drawRegion(img_menu, menu_index[menu] != i ? 6 : 113, 6 + j * 11, 107, 11, Sprite.TRANS_NONE, mx + 6, my + 6 + i * 11, Graphics.TOP | Graphics.LEFT);
            }
        }
        graphics.drawImage(img_turn[color], (width - img_turn[color].getWidth()) / 2, 0, Graphics.TOP | Graphics.LEFT);
        if (flash.showMessage()) graphics.drawImage(flash.image, (width - flash.image.getWidth()) / 2, (height - flash.image.getHeight()) / 2, Graphics.TOP | Graphics.LEFT); else if (flash.counter == 0) {
            graphics.drawRegion(img_controls, 0, 0, cw2, ch, Sprite.TRANS_NONE, 0, height - ch, Graphics.TOP | Graphics.LEFT);
            graphics.drawRegion(img_controls, cw2, 0, cw2, ch, Sprite.TRANS_NONE, width - cw2, height - ch, Graphics.TOP | Graphics.LEFT);
        }
        g.drawImage(image, 0, 0, Graphics.TOP | Graphics.LEFT);
    }
