    @Inline
    public void collectionPhase(short phaseId) {
        if (phaseId == SAPPHIRE_PREPARE_FIRST_TRACE) {
            if (VM.VERIFY_ASSERTIONS) {
                VM.assertions._assert(Sapphire.currentTrace == 0);
                VM.assertions._assert(!globalFirstTrace.hasWork());
                VM.assertions._assert(!globalSecondTrace.hasWork());
            }
            if (Options.verbose.getValue() >= 8) Log.writeln("Switching to 1st trace");
            Sapphire.currentTrace = 1;
            iuTerminationMustCheckRoots = true;
            ;
            MutatorContext.globalViewInsertionBarrier = true;
            MutatorContext.globalViewMutatorMustDoubleAllocate = false;
            MutatorContext.globalViewMutatorMustReplicate = false;
            if (Options.verbose.getValue() >= 8) Log.writeln("Global set insertion barrier about to request handshake");
            VM.collection.requestUpdateBarriers();
            MutatorContext.globalViewMutatorMustDoubleAllocate = true;
            if (Options.verbose.getValue() >= 8) Log.writeln("Global set double allocation barrier about to request handshake");
            VM.collection.requestUpdateBarriers();
            return;
        }
        if (phaseId == INSERTION_BARRIER_TERMINATION_CONDITION) {
            if (Options.verbose.getValue() >= 8) {
                Log.writeln("INSERTION_BARRIER_TERMINATION_CONDITION checking for work");
                Space.printVMMap();
            }
            if (globalFirstTrace.hasWork()) {
                if (Options.verbose.getValue() >= 8) Log.writeln("INSERTION_BARRIER_TERMINATION_CONDITION globalFirstTrace already contains work, mustCheckRoots will be set true");
                Phase.pushScheduledPhase(Phase.scheduleGlobal(INSERTION_BARRIER_TERMINATION_CONDITION));
                Phase.pushScheduledPhase(Phase.scheduleComplex(transitiveClosure));
                iuTerminationMustCheckRoots = true;
                return;
            }
            if (iuTerminationMustCheckRoots) {
                if (Options.verbose.getValue() >= 8) Log.writeln("INSERTION_BARRIER_TERMINATION_CONDITION mustCheckRoots true, will scan roots");
                Phase.pushScheduledPhase(Phase.scheduleGlobal(INSERTION_BARRIER_TERMINATION_CONDITION));
                Phase.pushScheduledPhase(Phase.scheduleCollector(FLUSH_COLLECTOR));
                Phase.pushScheduledPhase(Phase.scheduleOnTheFlyMutator(FLUSH_MUTATOR));
                Phase.pushScheduledPhase(Phase.scheduleComplex(rootScanPhase));
                iuTerminationMustCheckRoots = false;
                return;
            }
            if (Options.verbose.getValue() >= 8) Log.writeln("INSERTION_BARRIER_TERMINATION_CONDITION found no work and did not have to scan roots, done");
            return;
        }
        if (phaseId == PRE_TRACE_LINEAR_SCAN) {
            if (currentTrace == 0) {
                Log.writeln("Global running preFirstPhaseFromSpaceLinearSanityScan and preFirstPhaseToSpaceLinearSanityScan");
                deadFromSpaceBumpPointers.linearScan(Sapphire.preFirstPhaseFromSpaceLinearSanityScan);
                deadToSpaceBumpPointers.linearScan(Sapphire.preFirstPhaseToSpaceLinearSanityScan);
                return;
            }
            if (currentTrace == 2) {
                Log.writeln("Global running preSecondPhaseFromSpaceLinearSanityScan and preSecondPhaseToSpaceLinearSanityScan");
                deadFromSpaceBumpPointers.linearScan(Sapphire.preSecondPhaseFromSpaceLinearSanityScan);
                deadToSpaceBumpPointers.linearScan(Sapphire.preSecondPhaseToSpaceLinearSanityScan);
                return;
            }
        }
        if (phaseId == POST_TRACE_LINEAR_SCAN) {
            if (currentTrace == 1) {
                Log.writeln("Global running postFirstPhaseFromSpaceLinearSanityScan and postFirstPhaseToSpaceLinearSanityScan");
                deadFromSpaceBumpPointers.linearScan(Sapphire.postFirstPhaseFromSpaceLinearSanityScan);
                deadToSpaceBumpPointers.linearScan(Sapphire.postFirstPhaseToSpaceLinearSanityScan);
                return;
            }
            if (currentTrace == 2) {
                Log.writeln("Global running postSecondPhaseFromSpaceLinearSanityScan and postSecondPhaseToSpaceLinearSanityScan");
                deadFromSpaceBumpPointers.linearScan(Sapphire.postSecondPhaseFromSpaceLinearSanityScan);
                deadToSpaceBumpPointers.linearScan(Sapphire.postSecondPhaseToSpaceLinearSanityScan);
                return;
            }
        }
        if (phaseId == Sapphire.PREPARE) {
            if (currentTrace == 0) {
                fromSpace().prepare(false);
                globalFirstTrace.prepareNonBlocking();
            } else if (currentTrace == 1) {
                fromSpace().prepare(true);
                globalSecondTrace.prepareNonBlocking();
            } else {
                VM.assertions.fail("Unknown currentTrace value");
            }
            super.collectionPhase(phaseId);
            return;
        }
        if (phaseId == CLOSURE) {
            return;
        }
        if (phaseId == SAPPHIRE_PREPARE_SECOND_TRACE) {
            if (VM.VERIFY_ASSERTIONS) {
                VM.assertions._assert(Sapphire.currentTrace == 1);
                VM.assertions._assert(!globalFirstTrace.hasWork());
                VM.assertions._assert(!globalSecondTrace.hasWork());
            }
            if (Options.verbose.getValue() >= 8) Log.writeln("Switching to 2nd trace");
            currentTrace = 2;
            return;
        }
        if (phaseId == Sapphire.RELEASE) {
            if (currentTrace == 1) {
                if (Options.verbose.getValue() >= 8) {
                    Space.printVMMap();
                    Space.printUsageMB();
                    Space.printUsagePages();
                }
            } else if (currentTrace == 2) {
                low = !low;
                toSpace().release();
                Sapphire.deadBumpPointersLock.acquire();
                deadFromSpaceBumpPointers.rebind(fromSpace());
                CopyLocal tmp = deadFromSpaceBumpPointers;
                deadFromSpaceBumpPointers = deadToSpaceBumpPointers;
                deadToSpaceBumpPointers = tmp;
                deadToSpaceBumpPointers.rebind(toSpace());
                Sapphire.deadBumpPointersLock.release();
            } else {
                VM.assertions.fail("Unknown currentTrace value");
            }
            super.collectionPhase(phaseId);
            return;
        }
        if (phaseId == Sapphire.COMPLETE) {
            fromSpace().prepare(true);
        }
        if (phaseId == SAPPHIRE_PREPARE_ZERO_TRACE) {
            VM.assertions._assert(Sapphire.currentTrace == 2);
            if (Options.verbose.getValue() >= 8) Log.writeln("Switching to 0th trace");
            Sapphire.currentTrace = 0;
            return;
        }
        if (phaseId == Simple.PREPARE_STACKS) {
            stacksPrepared = false;
            return;
        }
        super.collectionPhase(phaseId);
    }
