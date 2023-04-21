    public SimpleChannel(String name, PortArity readPortArity, int readPortLimit, PortArity writePortArity, int writePortLimit, int capacity) {
        this.name = name;
        if (readPortArity == null) {
            throw new IllegalArgumentException("Read port arity may not be null.");
        }
        this.readArity = readPortArity;
        if (readPortLimit == 0) {
            throw new IllegalArgumentException("Read port limit may not be zero.");
        }
        this.readPortLimit = readPortLimit;
        this.readPorts = new HashSet<ReadPort<T>>(this.readArity == PortArity.ONE ? 1 : 10);
        if (writePortArity == null) {
            throw new IllegalArgumentException("Write port arity may not be null.");
        }
        this.writeArity = writePortArity;
        if (writePortLimit == 0) {
            throw new IllegalArgumentException("Write port limit may not be zero.");
        }
        this.writePortLimit = writePortLimit;
        this.writePorts = new HashSet<WritePort<T>>(this.writeArity == PortArity.ONE ? 1 : 10);
        this.capacity = capacity;
        if (this.capacity == 0) {
            this.values = new SynchronousQueue<Item<T>>(true);
        } else {
            this.values = new LinkedBlockingQueue<Item<T>>(this.capacity == BUFFER_CAPACITY_UNLIMITED ? Integer.MAX_VALUE : this.capacity);
        }
    }
