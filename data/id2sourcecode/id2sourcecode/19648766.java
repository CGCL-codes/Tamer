    void doMoveItem(Fleet src, Fleet dst, ResearchType type, int mode, int startIndex) {
        int srcCount = src.inventoryCount(type);
        if (srcCount > 0) {
            int transferCount = 1;
            if (mode == 2) {
                transferCount = (srcCount + 1) / 2;
            } else if (mode == 3) {
                transferCount = srcCount;
            }
            transferCount = Math.min(transferCount, dst.getAddLimit(type));
            if (type.category == ResearchSubCategory.SPACESHIPS_CRUISERS || type.category == ResearchSubCategory.SPACESHIPS_BATTLESHIPS) {
                for (Iterator<InventoryItem> it = src.inventory.iterator(); it.hasNext(); ) {
                    InventoryItem ii = it.next();
                    if (ii.type == type) {
                        if (startIndex == 0) {
                            it.remove();
                            dst.inventory.add(ii);
                            transferCount--;
                        } else {
                            startIndex--;
                        }
                    }
                    if (transferCount == 0) {
                        break;
                    }
                }
                FleetStatistics fs = src.getStatistics();
                if (fs.vehicleCount > fs.vehicleMax) {
                    int delta = fs.vehicleCount - fs.vehicleMax;
                    for (int j = src.inventory.size() - 1; j >= 0; j--) {
                        InventoryItem ii = src.inventory.get(j);
                        if (ii.type.category == ResearchSubCategory.WEAPONS_TANKS || ii.type.category == ResearchSubCategory.WEAPONS_VEHICLES) {
                            int toremove = delta > ii.count ? ii.count : delta;
                            dst.changeInventory(ii.type, toremove);
                            src.changeInventory(ii.type, -toremove);
                            delta -= toremove;
                            if (delta == 0) {
                                break;
                            }
                        }
                    }
                }
                updateInventory(null, fleet(), leftList);
                updateInventory(null, secondary, rightList);
            } else {
                src.changeInventory(type, -transferCount);
                dst.changeInventory(type, transferCount);
            }
        }
    }
