    boolean toggleSelection(TableItem item, boolean isCtrlKeyHold, boolean isShiftKeyHold) {
        if (item == null) {
            return false;
        }
        if ((style & SWT.MULTI) != 0 && (isCtrlKeyHold || isShiftKeyHold)) {
            if (isCtrlKeyHold) {
                for (int i = 0; i < selection.length; i++) {
                    if (item == selection[i]) {
                        TableItem[] newSelections = new TableItem[selection.length];
                        for (int j = 0; j < i; j++) {
                            newSelections[j] = selection[j];
                        }
                        for (int j = i; j < selection.length - 1; j++) {
                            newSelections[j] = selection[j + 1];
                        }
                        selection = newSelections;
                        item.showSelection(false);
                        lastSelection = item;
                        return false;
                    }
                }
                selection[selection.length] = item;
                lastSelection = item;
                item.showSelection(true);
            } else {
                for (int i = 0; i < selection.length; i++) {
                    if (selection[i] != null) {
                        selection[i].showSelection(false);
                    }
                }
                if (lastSelection != null) {
                    int idx1 = Math.min(indexOf(lastSelection), indexOf(item));
                    int idx2 = Math.max(indexOf(lastSelection), indexOf(item));
                    selection = new TableItem[0];
                    for (int i = idx1; i <= idx2; i++) {
                        TableItem ti = items[i];
                        selection[selection.length] = ti;
                        ti.showSelection(true);
                    }
                    return true;
                } else {
                    if (selection.length != 1) {
                        selection = new TableItem[1];
                    }
                    selection[0] = item;
                }
            }
        } else {
            item.showSelection(true);
            for (int i = 0; i < selection.length; i++) {
                if (selection[i] != null && selection[i] != item) {
                    selection[i].showSelection(false);
                }
            }
            if (selection.length != 1) {
                selection = new TableItem[1];
            }
            selection[0] = item;
        }
        lastSelection = item;
        return true;
    }
