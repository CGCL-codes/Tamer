        @Override
        public LispObject execute(LispObject object) throws ConditionThrowable {
            final LispThread thread = LispThread.currentThread();
            thread.setSpecialVariable(SymbolConstants.MINUS, object);
            LispObject result;
            try {
                result = thread.execute(SymbolConstants.EVAL.getSymbolFunction(), object);
            } catch (OutOfMemoryError e) {
                Debug.trace(e);
                return error(new LispError("Out of memory."));
            } catch (StackOverflowError e) {
                thread.setSpecialVariable(_SAVED_BACKTRACE_, thread.backtrace(0));
                return error(new StorageCondition("Stack overflow."));
            } catch (Go go) {
                throw go;
            } catch (Throw t) {
                return error(new ControlError("Attempt to throw to the nonexistent tag " + t.tag.writeToString() + " ret=" + safeWriteToString(t.getResult(thread)) + "."));
            } catch (Return r) {
                Debug.trace(r);
                String str = "Attempt to return to the nonexistent tag " + r.tag.writeToString() + " ret=" + safeWriteToString(r.result) + ".";
                Debug.trace(str);
                return error(new ControlError(str));
            } catch (Throwable t) {
                Debug.trace(t);
                thread.setSpecialVariable(_SAVED_BACKTRACE_, thread.backtrace(0));
                return error(new LispError("Caught " + t + "."));
            }
            Debug.assertTrue(result != null);
            thread.setSpecialVariable(SymbolConstants.STAR_STAR_STAR, thread.safeSymbolValue(SymbolConstants.STAR_STAR));
            thread.setSpecialVariable(SymbolConstants.STAR_STAR, thread.safeSymbolValue(SymbolConstants.STAR));
            thread.setSpecialVariable(SymbolConstants.STAR, result);
            thread.setSpecialVariable(SymbolConstants.PLUS_PLUS_PLUS, thread.safeSymbolValue(SymbolConstants.PLUS_PLUS));
            thread.setSpecialVariable(SymbolConstants.PLUS_PLUS, thread.safeSymbolValue(SymbolConstants.PLUS));
            thread.setSpecialVariable(SymbolConstants.PLUS, thread.safeSymbolValue(SymbolConstants.MINUS));
            LispObject[] values = thread._values;
            thread.setSpecialVariable(SymbolConstants.SLASH_SLASH_SLASH, thread.safeSymbolValue(SymbolConstants.SLASH_SLASH));
            thread.setSpecialVariable(SymbolConstants.SLASH_SLASH, thread.safeSymbolValue(SymbolConstants.SLASH));
            if (values != null) {
                LispObject slash = NIL;
                for (int i = values.length; i-- > 0; ) slash = makeCons(values[i], slash);
                thread.setSpecialVariable(SymbolConstants.SLASH, slash);
            } else thread.setSpecialVariable(SymbolConstants.SLASH, makeCons(result));
            return result;
        }
