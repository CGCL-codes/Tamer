    private void writeBarrierAssertionFailure(Address slot, ObjectReference src) {
        Log.write("Thread #", getId());
        Log.write(" writing to slot ", slot);
        Log.write(" of object ");
        Log.write(src);
        Log.write(" which has a FP value of ");
        Log.writeln(ForwardingWord.getReplicaPointer(src));
        Log.write("MutatorContext.globalViewMutatorMustDoubleAllocate is ");
        Log.writeln(MutatorContext.globalViewMutatorMustDoubleAllocate ? 1 : 0);
        Log.write("MutatorContext.globalViewMutatorInsertionBarrier is ");
        Log.writeln(MutatorContext.globalViewInsertionBarrier ? 1 : 0);
        Log.write("MutatorContext.globalViewMutatorMustReplicate is ");
        Log.writeln(MutatorContext.globalViewMutatorMustReplicate ? 1 : 0);
        Log.writeln("Insertion barrier is ", insertionBarrier ? 1 : 0);
        Log.writeln("Double alloc barrier is ", mutatorMustDoubleAllocate ? 1 : 0);
        Log.writeln("Replication barrier is ", mutatorMustReplicate ? 1 : 0);
        VM.assertions.fail("writeBarrierAssertionFailure - look at call site for cause");
    }
