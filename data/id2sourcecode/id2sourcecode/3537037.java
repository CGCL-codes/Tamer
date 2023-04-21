    @Override
    public String writeToString() throws ConditionThrowable {
        try {
            final LispThread thread = LispThread.currentThread();
            if (typep(SymbolConstants.RESTART) != NIL) {
                Symbol PRINT_RESTART = PACKAGE_SYS.intern("PRINT-RESTART");
                LispObject fun = PRINT_RESTART.getSymbolFunction();
                StringOutputStream stream = new StringOutputStream();
                thread.execute(fun, this, stream);
                return stream.getStringOutputString().getStringValue();
            }
            if (_PRINT_STRUCTURE_.symbolValue(thread) == NIL) return unreadableString(structureClass.getSymbol().writeToString());
            int maxLevel = Integer.MAX_VALUE;
            LispObject printLevel = SymbolConstants.PRINT_LEVEL.symbolValue(thread);
            if (printLevel instanceof Fixnum) maxLevel = printLevel.intValue();
            LispObject currentPrintLevel = _CURRENT_PRINT_LEVEL_.symbolValue(thread);
            int currentLevel = currentPrintLevel.intValue();
            if (currentLevel >= maxLevel && slots.length > 0) return "#";
            FastStringBuffer sb = new FastStringBuffer("#S(");
            sb.append(structureClass.getSymbol().writeToString());
            if (currentLevel < maxLevel) {
                LispObject effectiveSlots = structureClass.getSlotDefinitions();
                LispObject[] effectiveSlotsArray = effectiveSlots.copyToArray();
                Debug.assertTrue(effectiveSlotsArray.length == slots.length);
                final LispObject printLength = SymbolConstants.PRINT_LENGTH.symbolValue(thread);
                final int limit;
                if (printLength instanceof Fixnum) limit = Math.min(slots.length, printLength.intValue()); else limit = slots.length;
                final boolean printCircle = (SymbolConstants.PRINT_CIRCLE.symbolValue(thread) != NIL);
                for (int i = 0; i < limit; i++) {
                    sb.append(' ');
                    SimpleVector slotDefinition = (SimpleVector) effectiveSlotsArray[i];
                    LispObject slotName = slotDefinition.AREF(1);
                    Debug.assertTrue(slotName instanceof Symbol);
                    sb.append(':');
                    sb.append(((Symbol) slotName).getName());
                    sb.append(' ');
                    if (printCircle) {
                        StringOutputStream stream = new StringOutputStream();
                        thread.execute(SymbolConstants.OUTPUT_OBJECT.getSymbolFunction(), slots[i], stream);
                        sb.append(stream.getStringOutputString().getStringValue());
                    } else sb.append(slots[i].writeToString());
                }
                if (limit < slots.length) sb.append(" ...");
            }
            sb.append(')');
            return sb.toString();
        } catch (StackOverflowError e) {
            error(new StorageCondition("Stack overflow."));
            return null;
        }
    }
