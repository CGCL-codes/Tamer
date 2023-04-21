    private void processMessage() {
        if (_vm == null || _eventThread == null || !_eventThread.isConnected()) {
            return;
        }
        _vm.suspend();
        try {
            List<ReferenceType> classes = _vm.classesByName("edu.rice.cs.cunit.SyncPointBuffer");
            ClassType bufferClass = null;
            for (ReferenceType cl : classes) {
                if (cl.name().equals("edu.rice.cs.cunit.SyncPointBuffer")) {
                    if (cl instanceof ClassType) {
                        bufferClass = (ClassType) cl;
                        break;
                    }
                }
            }
            if (null == bufferClass) {
                throw new Error("Could not find edu.rice.cs.cunit.SyncPointBuffer class.");
            }
            Field messageField = bufferClass.fieldByName("_message");
            if (null == messageField) {
                throw new Error("Could not find edu.rice.cs.cunit.SyncPointBuffer._message field.");
            }
            Value messageValue = bufferClass.getValue(messageField);
            if (!(messageValue instanceof StringReference)) {
                throw new Error("Unexpected type for edu.rice.cs.cunit.SyncPointBuffer._message.");
            }
            StringReference message = (StringReference) messageValue;
            String msg = message.value();
            _writer.println("// " + msg);
            if (!msg.startsWith("\t")) {
                List<ThreadReference> allThreads = _vm.allThreads();
                for (ThreadReference t : allThreads) {
                    classes = _vm.classesByName("java.lang.Thread");
                    ClassType threadClass = null;
                    for (ReferenceType cl : classes) {
                        if (cl.name().equals("java.lang.Thread")) {
                            if (cl instanceof ClassType) {
                                threadClass = (ClassType) cl;
                                break;
                            }
                        }
                    }
                    if (null == threadClass) {
                        throw new Error("Could not find java.lang.Thread class.");
                    }
                    Field threadIDField = threadClass.fieldByName("$$$threadID$$$");
                    if (null == threadIDField) {
                        throw new Error("Could not find java.lang.Thread.$$$threadID$$$ field.");
                    }
                    Value threadIDValue = t.getValue(threadIDField);
                    if (!(threadIDValue instanceof LongValue)) {
                        throw new Error("Unexpected type for java.lang.Thread.$$$threadID$$$.");
                    }
                    LongValue threadID = (LongValue) threadIDValue;
                    Field oldThreadField = threadClass.fieldByName("$$$oldThread$$$");
                    if (null == oldThreadField) {
                        throw new Error("Could not find java.lang.Thread.$$$oldThread$$$ field.");
                    }
                    Value oldThreadValue = t.getValue(oldThreadField);
                    if (!(oldThreadValue instanceof BooleanValue)) {
                        throw new Error("Unexpected type for java.lang.Thread.$$$oldThread$$$.");
                    }
                    BooleanValue oldThread = (BooleanValue) oldThreadValue;
                    _writer.printf("// \t$$$threadID$$$ = %d, $$$oldThread$$$ = %s: %s%s", threadID.value(), String.valueOf(oldThread.value()), t.name(), System.getProperty("line.separator"));
                    _writer.flush();
                }
            }
        } catch (Error error) {
            throw new Error("Could not read SyncPointBuffer._message.");
        } finally {
            _writer.flush();
            _vm.resume();
        }
    }
