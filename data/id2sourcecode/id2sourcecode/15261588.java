    private void printTestList() {
        VerbosePrint.outPrintln("Usage: java -jar " + RTMB.benchmarkName() + ".jar [-t|--test] [<tests>]*");
        VerbosePrint.outPrintln("Where <tests> could be one or more of:");
        VerbosePrint.outPrintln("IntegralRateTest\tMeasures the integral rate of " + "the underlying system");
        VerbosePrint.outPrintln("FloatRateTest\t\tMeasures the floating point rate" + " of the underlying system");
        VerbosePrint.outPrintln("MemCacheTest\t\tVerifies that random page accesses" + " and possible page cache misses will not cause indeterminism");
        VerbosePrint.outPrintln("DiskIOReadTest\t\tVerifies that disk read requests" + " are deterministic");
        VerbosePrint.outPrintln("DiskIOWriteTest\t\tVerifies that disk write requests" + " are deterministic");
        VerbosePrint.outPrintln("NetIOTest\t\tVerifies that read and write requests" + " using TCP/IP sockets are deterministic");
        VerbosePrint.outPrintln("TimeAccuracyTest\t\tVerifies that the " + "clock and timer work consistently.");
        VerbosePrint.outPrintln("EventDispatchTest\t\tVerifies that events are " + "dispatched with consistent latencies.");
        VerbosePrint.outPrintln("ThreadPriorityTest\t\tVerifies that higher " + "priority threads preempt lower priority threads.");
        VerbosePrint.outPrintln("LockConsistencyTest\t\tVerifies that " + "uncontended locks are acquired with consistent overhead.");
        VerbosePrint.outPrintln("PeriodicEvents\t\tVerifies that " + "periodic real-time events are dispatched consistently (RTSJ specific test).");
        VerbosePrint.outPrintln("GarbageGenerationTest\tVerifies that garbage generation " + "(high object allocation rates) does not cause unexpected delays or inconsistent " + "application performance.");
        VerbosePrint.outPrintln("CompilationTest\t\tVerifies that dynamic compilation does " + "not cause unexpected delays or result in application performance to degrade.");
        VerbosePrint.outPrintln("BytecodeConsistencyTest\tVerifies that all byte-codes " + "run with consistent performance.");
        VerbosePrint.outPrintln("MultiThreadTest\t\tVerifies that systems with multiple " + "CPUs can scale reasonably across multiple application threads in a single process.");
        VerbosePrint.outPrintln("MultiCPU\t\tVerifies that systems with multiple CPUs " + "can scale reasonably across multiple runtime environments without loss of determinism.");
        VerbosePrint.outPrintln("NHRTSupportTest\t\tVerifies that NHRTs are not " + "pre-empted by lower priority threads including the system garbage collector " + "(RTSJ specific test).");
        VerbosePrint.outPrintln("ScopedMemoryTest\tVerifies that access to scoped and " + "immortal memory is consistent (RTSJ specific test).");
        ok = false;
    }
