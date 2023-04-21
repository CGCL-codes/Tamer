    public void keyPressed(KeyEvent env) {
        history.beginAtom();
        try {
            mshift = env.isShiftDown();
            int ocx = cx;
            int ocy = cy;
            int kc = env.getKeyCode();
            if (kc == KeyEvent.VK_F1) {
                U.showHelp(ui, uiComp);
            } else if (kc == KeyEvent.VK_F2) {
                U.saveAs(this);
            } else if (kc == KeyEvent.VK_F3) {
                ptFind.findNext();
            } else if (kc == KeyEvent.VK_F5) {
                U.reloadWithEncodingByUser(fn, this);
            }
            if (env.isAltDown()) {
                if (kc == KeyEvent.VK_LEFT) {
                    ptEdit.moveLineLeft(cy);
                    focusCursor();
                } else if (kc == KeyEvent.VK_RIGHT) {
                    ptEdit.moveLineRight(cy);
                    focusCursor();
                } else if (kc == KeyEvent.VK_BACK_SLASH) {
                    rectSelectMode = !rectSelectMode;
                } else if (kc == KeyEvent.VK_N) {
                    ui.noise = !ui.noise;
                    if (ui.noise) {
                        U.startNoiseThread(ui, uiComp);
                    }
                } else if (kc == KeyEvent.VK_S) {
                    if (lineSep.equals("\n")) lineSep = "\r\n"; else lineSep = "\n";
                } else if (kc == KeyEvent.VK_W) {
                    ptEdit.wrapLines(cx);
                    focusCursor();
                } else if (kc == KeyEvent.VK_J) {
                    U.runScript(this);
                } else if (kc == KeyEvent.VK_D) {
                    U.runScriptOnDir(this.workPath);
                } else if (kc == KeyEvent.VK_PAGE_UP) {
                    cx = Math.max(0, cx - uiComp.getWidth() / 10);
                    focusCursor();
                } else if (kc == KeyEvent.VK_PAGE_DOWN) {
                    cx = cx + uiComp.getWidth() / 10;
                    focusCursor();
                } else if (kc == KeyEvent.VK_C) {
                    ui.setNextColorMode();
                    ui.applyColorMode(ui.colorMode);
                }
            } else if (env.isControlDown()) {
                if (kc == KeyEvent.VK_C) {
                    ptSelection.copySelected();
                } else if (kc == KeyEvent.VK_V) {
                    if (ptSelection.isSelected()) {
                        ptEdit.deleteRect(ptSelection.getSelectRect());
                    }
                    ptEdit.insertString(U.getClipBoard());
                } else if (kc == KeyEvent.VK_X) {
                    ptSelection.cutSelected();
                } else if (kc == KeyEvent.VK_A) {
                    ptSelection.selectAll();
                } else if (kc == KeyEvent.VK_D) {
                    if (ptSelection.isSelected()) {
                        ptEdit.deleteRect(ptSelection.getSelectRect());
                    } else {
                        ptEdit.deleteLine(cy);
                    }
                    focusCursor();
                } else if (kc == KeyEvent.VK_O) {
                    U.openFile(this);
                } else if (kc == KeyEvent.VK_N) {
                    EditPanel ep = new EditPanel("empty");
                    ep.openWindow();
                    ep.page.workPath = this.workPath;
                    ep.page.ptSelection.selectAll();
                    ep.page.ui.applyColorMode(ui.colorMode);
                } else if (kc == KeyEvent.VK_S) {
                    if (U.saveFile(this)) {
                        System.out.println("saved");
                        ui.message("saved");
                    }
                } else if (kc == KeyEvent.VK_L) {
                    cursor.gotoLine();
                } else if (kc == KeyEvent.VK_Z) {
                    history.undo();
                } else if (kc == KeyEvent.VK_F) {
                    ptFind.showFindDialog();
                } else if (kc == KeyEvent.VK_Y) {
                    history.redo();
                } else if (kc == KeyEvent.VK_W) {
                    U.closePage(this);
                } else if (kc == KeyEvent.VK_E) {
                    U.setEncodingByUser(this, "Set Encoding:");
                } else if (kc == KeyEvent.VK_PAGE_UP) {
                    cy = 0;
                    cx = 0;
                    focusCursor();
                } else if (kc == KeyEvent.VK_PAGE_DOWN) {
                    cy = roLines.getLinesize() - 1;
                    cx = 0;
                    focusCursor();
                } else if (kc == KeyEvent.VK_R) {
                    U.removeTrailingSpace(PlainPage.this);
                } else if (kc == KeyEvent.VK_LEFT) {
                    cursor.moveLeftWord();
                    focusCursor();
                } else if (kc == KeyEvent.VK_RIGHT) {
                    cursor.moveRightWord();
                    focusCursor();
                } else if (kc == KeyEvent.VK_UP) {
                    sy = Math.max(0, sy - 1);
                } else if (kc == KeyEvent.VK_DOWN) {
                    sy = Math.min(sy + 1, roLines.getLinesize() - 1);
                } else if (kc == KeyEvent.VK_0) {
                    ui.scalev = 1;
                } else if (kc == KeyEvent.VK_G) {
                    if (cy < lines.size()) U.gotoFileLine(roLines.getline(cy).toString());
                } else if (kc == KeyEvent.VK_H) {
                    U.openFileHistory();
                } else if (kc == KeyEvent.VK_P) {
                    new U.Print(PlainPage.this).printPages();
                } else if (kc == KeyEvent.VK_ENTER) {
                    cursor.moveEnd();
                    focusCursor();
                }
            } else {
                if (kc == KeyEvent.VK_LEFT) {
                    cursor.moveLeft();
                    focusCursor();
                } else if (kc == KeyEvent.VK_RIGHT) {
                    cursor.moveRight();
                    focusCursor();
                } else if (kc == KeyEvent.VK_UP) {
                    cursor.moveUp();
                    focusCursor();
                } else if (kc == KeyEvent.VK_DOWN) {
                    cursor.moveDown();
                    focusCursor();
                } else if (kc == KeyEvent.VK_HOME) {
                    cursor.moveHome();
                    focusCursor();
                } else if (kc == KeyEvent.VK_END) {
                    cursor.moveEnd();
                    focusCursor();
                } else if (kc == KeyEvent.VK_PAGE_UP) {
                    cursor.movePageUp();
                    focusCursor();
                } else if (kc == KeyEvent.VK_PAGE_DOWN) {
                    cursor.movePageDown();
                    focusCursor();
                } else if (kc == KeyEvent.VK_CONTROL || kc == KeyEvent.VK_SHIFT || kc == KeyEvent.VK_ALT) {
                    return;
                }
            }
            boolean cmoved = !(ocx == cx && ocy == cy);
            if (cmoved) {
                if (env.isShiftDown()) {
                    selectstopx = cx;
                    selectstopy = cy;
                } else {
                    if (saveSelectionCancel) {
                        saveSelectionCancel = false;
                    } else {
                        ptSelection.cancelSelect();
                    }
                }
            }
            uiComp.repaint();
        } catch (Exception e) {
            ui.message("err:" + e);
            uiComp.repaint();
            e.printStackTrace();
        }
        history.endAtom();
    }
