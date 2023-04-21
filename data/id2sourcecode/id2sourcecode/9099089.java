    @Inline
    public void collectionPhase(short phaseId, boolean primary) {
        if (phaseId == Sapphire.PREPARE) {
            super.collectionPhase(phaseId, primary);
            if (Sapphire.currentTrace == 0) {
            } else if (Sapphire.currentTrace == 1) {
                assertRemsetsFlushed();
            } else {
                VM.assertions.fail("Unknown Sapphire.currentTrace value");
            }
            return;
        }
        if (phaseId == Sapphire.PRE_TRACE_LINEAR_SCAN) {
            if (Sapphire.currentTrace == 0) {
                Log.writeln("Mutator # running preFirstPhaseFromSpaceLinearSanityScan and preFirstPhaseToSpaceLinearSanityScan ", getId());
                fromSpaceLocal.linearScan(Sapphire.preFirstPhaseFromSpaceLinearSanityScan);
                toSpaceLocal.linearScan(Sapphire.preFirstPhaseToSpaceLinearSanityScan);
                return;
            }
            if (Sapphire.currentTrace == 2) {
                Log.writeln("Mutator # running preSecondPhaseFromSpaceLinearSanityScan and preSecondPhaseToSpaceLinearSanityScan ", getId());
                fromSpaceLocal.linearScan(Sapphire.preSecondPhaseFromSpaceLinearSanityScan);
                toSpaceLocal.linearScan(Sapphire.preSecondPhaseToSpaceLinearSanityScan);
                return;
            }
        }
        if (phaseId == Sapphire.POST_TRACE_LINEAR_SCAN) {
            if (Sapphire.currentTrace == 1) {
                Log.writeln("Mutator # running postFirstPhaseFromSpaceLinearSanityScan and postFirstPhaseToSpaceLinearSanityScan ", getId());
                fromSpaceLocal.linearScan(Sapphire.postFirstPhaseFromSpaceLinearSanityScan);
                toSpaceLocal.linearScan(Sapphire.postFirstPhaseToSpaceLinearSanityScan);
                return;
            }
            if (Sapphire.currentTrace == 2) {
                Log.writeln("Mutator # running postSecondPhaseFromSpaceLinearSanityScan and postSecondPhaseToSpaceLinearSanityScan ", getId());
                fromSpaceLocal.linearScan(Sapphire.postSecondPhaseFromSpaceLinearSanityScan);
                toSpaceLocal.linearScan(Sapphire.postSecondPhaseToSpaceLinearSanityScan);
                return;
            }
        }
        if (phaseId == Simple.PREPARE_STACKS) {
            if (Options.verbose.getValue() >= 8) Log.writeln("Deferring preparing stack until we want to scan thread");
            return;
        }
        if (phaseId == Sapphire.RELEASE) {
            super.collectionPhase(phaseId, primary);
            if (Sapphire.currentTrace == 1) {
                mutatorMustReplicate = globalViewMutatorMustReplicate = true;
                mutatorMustDoubleAllocate = globalViewMutatorMustDoubleAllocate = true;
                insertionBarrier = globalViewInsertionBarrier = false;
                assertRemsetsFlushed();
            } else if (Sapphire.currentTrace == 2) {
                fromSpaceLocal.rebind(Sapphire.toSpace());
                toSpaceLocal.rebind(Sapphire.fromSpace());
                assertRemsetsFlushed();
            }
            return;
        }
        if (phaseId == Sapphire.COMPLETE) {
            super.collectionPhase(phaseId, primary);
            mutatorMustReplicate = globalViewMutatorMustReplicate = false;
            mutatorMustDoubleAllocate = globalViewMutatorMustDoubleAllocate = false;
            insertionBarrier = globalViewInsertionBarrier = false;
            assertRemsetsFlushed();
            return;
        }
        super.collectionPhase(phaseId, primary);
    }
