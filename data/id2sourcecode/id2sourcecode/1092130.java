    public TreeNode getChild(final Object key, final int[] stack, int stackPos) {
        if (OLD_SOLUTION) {
            if (pos.isWordContinued()) {
                LinkedListPosition childPosition = pos.goToNextLevel();
                if (childPosition != null) {
                    int keyValue = ((Integer) key).intValue();
                    while (childPosition != null) {
                        int childValueCode = childPosition.getValueCode();
                        if (childValueCode == keyValue) {
                            LinkedListTreeNode child = childPosition.isWordEnd() ? new LinkedListTreeLeaf(childPosition) : new LinkedListTreeNode(childPosition);
                            return child;
                        }
                        childPosition = childValueCode < keyValue ? childPosition.goToNextChild() : null;
                    }
                }
            }
            return null;
        } else {
            final LinkedListTreeUnitArray units = pos.getUnitArray();
            final int unitsSize = units.size();
            final int startStackPos = stackPos;
            int p = pos.getPos();
            int fp = units.getFastIndex(p);
            int nested = pos.getNested() ? 1 : 0;
            int unitsToRead = pos.getUnitsToRead();
            LinkedListPosition returnPos = pos.getReturnPos();
            while (p < unitsSize && units.isAbsolutePointerFast(fp)) {
                if (nested == 0 || unitsToRead > 1) {
                    stack[++stackPos] = p + 1;
                    stack[++stackPos] = nested;
                    stack[++stackPos] = unitsToRead - 1;
                }
                unitsToRead = units.getValueCodeFast(fp);
                nested = unitsToRead != 0 ? 1 : 0;
                p = units.getDistanceFast(fp);
                fp = units.getFastIndex(p);
            }
            if (units.isWordContinuedFast(fp)) {
                int cint = ((Integer) key).intValue();
                if (nested == 1 && unitsToRead <= 1) {
                    if (stackPos <= startStackPos) {
                        if (returnPos != null) {
                            unitsToRead = returnPos.getUnitsToRead();
                            nested = returnPos.getNested() ? 1 : 0;
                            p = returnPos.getPos();
                            returnPos = returnPos.getReturnPos();
                        } else {
                            return null;
                        }
                    } else {
                        unitsToRead = stack[stackPos--];
                        nested = stack[stackPos--];
                        p = stack[stackPos--];
                    }
                } else {
                    ++p;
                    --unitsToRead;
                }
                fp = units.getFastIndex(p);
                boolean found = false;
                while (!found) {
                    while (p < unitsSize && units.isAbsolutePointerFast(fp)) {
                        if (nested == 0 || unitsToRead > 1) {
                            stack[++stackPos] = p + 1;
                            stack[++stackPos] = nested;
                            stack[++stackPos] = unitsToRead - 1;
                        }
                        unitsToRead = units.getValueCodeFast(fp);
                        nested = unitsToRead != 0 ? 1 : 0;
                        p = units.getDistanceFast(fp);
                        fp = units.getFastIndex(p);
                    }
                    int vc = units.getValueCodeFast(fp);
                    if (vc == cint) {
                        LinkedListPosition retPos = returnPos == null ? null : new LinkedListPosition(returnPos);
                        LinkedListPosition position = new LinkedListPosition(units, p, retPos, unitsToRead, nested == 1);
                        LinkedListTreeNode child = units.isWordEndFast(fp) ? new LinkedListTreeLeaf(position) : new LinkedListTreeNode(position);
                        position.setAbsProcessed(true);
                        while (stackPos > startStackPos) {
                            unitsToRead = stack[stackPos--];
                            nested = stack[stackPos--];
                            p = stack[stackPos--];
                            LinkedListPosition tmp = new LinkedListPosition(units, p, retPos, unitsToRead, nested == 1);
                            position.setReturnPos(tmp);
                            position = tmp;
                        }
                        return child;
                    } else if (vc > cint) {
                        return null;
                    } else {
                        int d = units.getDistanceFast(fp);
                        if (d > 0) {
                            int target = p + d;
                            if (nested == 1 && unitsToRead > 0 && target >= p + unitsToRead) {
                                if (stackPos <= startStackPos) {
                                    if (returnPos != null) {
                                        unitsToRead = returnPos.getUnitsToRead();
                                        nested = returnPos.getNested() ? 1 : 0;
                                        p = returnPos.getPos();
                                        returnPos = returnPos.getReturnPos();
                                    } else {
                                        return null;
                                    }
                                } else {
                                    unitsToRead = stack[stackPos--];
                                    nested = stack[stackPos--];
                                    p = stack[stackPos--];
                                }
                            } else {
                                p = target;
                                unitsToRead = unitsToRead - d;
                            }
                            fp = units.getFastIndex(p);
                        } else {
                            return null;
                        }
                    }
                }
            }
            return null;
        }
    }
