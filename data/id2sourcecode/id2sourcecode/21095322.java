    public static FlexotaskGraph validate(FlexotaskTemplate specification, String schedulerKind, FlexotaskSchedulingData schedulingData, Map initializationMap, FlexotaskDistributionContext distributionContext) throws FlexotaskValidationException {
        FlexotaskScheduler scheduler = FlexotaskSchedulerRegistry.getScheduler(schedulerKind);
        if (scheduler == null) {
            throw new FlexotaskValidationException("No " + schedulerKind + " scheduler available");
        }
        FlexotaskDistributer distributer;
        if (distributionContext == null) {
            distributer = new FlexotaskDistributer();
        } else {
            distributer = FlexotaskDistributerRegistry.getDistributer(distributionContext);
            if (distributer == null) {
                throw new FlexotaskValidationException("No distributer factory found for distributer type " + distributionContext.getSystemType());
            }
        }
        tracerFactory.startValidation();
        Object[] toPin;
        Map heapSizes;
        FlexotaskTimerService timer;
        try {
            checkConsistency(specification);
            if (initializationMap == null) {
                initializationMap = new HashMap();
            } else {
                initializationMap = new HashMap(initializationMap);
            }
            for (Iterator iter = specification.getTasks().iterator(); iter.hasNext(); ) {
                FlexotaskTaskTemplate element = (FlexotaskTaskTemplate) iter.next();
                if (element instanceof FlexotaskCommunicatorTemplate) {
                    FlexotaskChannel channel = distributer.getChannel((FlexotaskCommunicatorTemplate) element);
                    initializationMap.put(element.getName(), channel);
                }
            }
            timer = distributer.getTimerService();
            toPin = new CodeValidator(specification).validate(initializationMap, timer);
            if (!scheduler.isSchedulable(specification, schedulingData)) {
                throw new FlexotaskValidationException("Specification rejected by the scheduler: " + scheduler.getRejectionReason());
            }
            heapSizes = scheduler.getHeapSizes(specification, schedulingData);
        } finally {
            tracerFactory.endValidation();
        }
        verbosePinObjects(toPin, "heap objects found during validation");
        Class[] stable = initStableTransient(specification);
        tracerFactory.startInstantiation();
        Map instantiationMap;
        try {
            instantiationMap = instantiate(specification, heapSizes, initializationMap, timer);
        } finally {
            tracerFactory.endInstantiation();
        }
        Map guards = new HashMap();
        for (Iterator iterator = instantiationMap.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry entry = (Map.Entry) iterator.next();
            Object controller = entry.getValue();
            if (controller instanceof AtomicFlexotaskController) {
                FlexotaskTaskTemplate task = (FlexotaskTaskTemplate) entry.getKey();
                try {
                    Class guardClass = CodeValidator.resolveClass((task.getImplementationClass() + "$Guard"), cl);
                    AtomicFlexotaskGuard guard = (AtomicFlexotaskGuard) guardClass.newInstance();
                    AtomicFlexotaskBase atomicTask = (AtomicFlexotaskBase) ((FlexotaskControllerImpl) controller).root.getTask();
                    guard.setDelegate((AtomicFlexotask) atomicTask);
                    atomicTask.lock = guard;
                    guards.put(task.getName(), guard);
                } catch (Exception e) {
                    throw new FlexotaskValidationException("Guard could not be constructed for atomic Flexotask " + task.getName());
                }
            }
        }
        if (guards.size() > 0) {
            Object[] newPinned = guards.values().toArray();
            verbosePinObjects(newPinned, "guard objects");
            Object[] allPinned = new Object[toPin.length + newPinned.length];
            System.arraycopy(toPin, 0, allPinned, 0, toPin.length);
            System.arraycopy(newPinned, 0, allPinned, toPin.length, newPinned.length);
            toPin = allPinned;
        }
        FlexotaskGraphImpl ans = new FlexotaskGraphImpl(distributer, guards, toPin, stable);
        tracerFactory.startScheduling();
        FlexotaskThreadFactory factory = new FlexotaskThreadFactoryImpl(timer, tracerFactory, ans);
        FlexotaskRunner runner;
        try {
            runner = scheduler.schedule(specification, instantiationMap, schedulingData, factory, initializationMap);
        } finally {
            tracerFactory.endScheduling();
        }
        ans.setRunner(runner);
        return ans;
    }
