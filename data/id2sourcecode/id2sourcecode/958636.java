    @Override
    public void run() {
        myLogger.debug("Executing At: " + this.evTime() + " event type:  " + this.getEventType());
        poisson = Poisson(5.0);
        while (poisson == 0) poisson = Poisson(5.0);
        t = this.eventTime + poisson;
        car = new Car(this.myStreetControllerAgent.n, 0.0, "car " + this.myStreetControllerAgent.n, 2.0, 40000.0, 2.0);
        this.myStreetControllerAgent.putEvent(this.segment.getId(), t, AE, car, 0, AMBAR, 0);
        this.myStreetControllerAgent.n++;
        car = (Car) this.objectId();
        if (!this.segment.isFull(this.evTime())) {
            try {
                FileOutputStream out = new FileOutputStream("c:/temp/log.txt", true);
                PrintWriter miArchivo = new PrintWriter(out);
                miArchivo.write(this.objectId() + ",");
                miArchivo.write(this.evTime() + ",");
                miArchivo.write(segment.getId() + ",");
                miArchivo.write(this.evPosition() + ",");
                miArchivo.write(this.getEventType() + ",");
                miArchivo.println();
                miArchivo.close();
            } catch (IOException ioe) {
                myLogger.error("Error en la creacion del archivo log.txt");
                throw new UnsupportedOperationException("The output file log cant be created !!");
            }
            this.segment.putCar((Car) objectId(), new Double(0.0));
            nextEvent = root.siguiente;
            evaluatedPosition = segment.getSize();
            myLogger.debug("Size of the street> " + this.segment.getId() + "is > " + segment.getSize());
            while (nextEvent != root) {
                if (nextEvent.getEventType() != LCE && nextEvent.getLane() == getLane()) {
                    if (nextEvent.evPosition() <= evaluatedPosition) {
                        if (nextEvent.evPosition() > this.evPosition()) {
                            if (nextEvent.getEventType() == BE || nextEvent.getEventType() == WBE || nextEvent.getEventType() == CE) {
                                myLogger.debug(car.getId() + " Estoy viendo un " + nextEvent.getEventType() + "a las " + nextEvent.evTime() + " en la pos " + nextEvent.evPosition());
                                arrivalPosition = nextEvent.evPosition() - car.getDisMinSafe();
                                deltaP = arrivalPosition - this.evPosition();
                                deltaT = (deltaP * 3600) / car.getVelocity();
                                arrivalTime = deltaT + this.evTime();
                                if (arrivalTime >= nextEvent.evTime()) {
                                    newEvent = WSE;
                                    startTime = arrivalTime;
                                } else {
                                    if (nextEvent.getEventType() == BE) {
                                        startTime = nextEvent.evTime() + car.getDelayToStart();
                                        newEvent = SE;
                                    } else {
                                        startTime = nextEvent.evTime();
                                        newEvent = WSE;
                                    }
                                }
                                evaluatedPosition = nextEvent.evPosition();
                            }
                        }
                    }
                } else {
                    PosiblearrivalPosition = nextEvent.evPosition();
                    PosibledeltaP = PosiblearrivalPosition - this.evPosition();
                    PosibledeltaT = (PosibledeltaP * 3600) / car.getVelocity();
                    PosiblearrivalTime = PosibledeltaT + this.evTime();
                    if (newEvent == EMPTY) {
                        if (PosiblearrivalTime < nextEvent.evTime()) {
                            if (nextEvent.getDesiredStatus() == VERDE) {
                                newEvent = SE;
                                arrivalPosition = PosiblearrivalPosition - car.getDisMinSafe();
                                deltaP = arrivalPosition - this.evPosition();
                                deltaT = (deltaP * 3600) / car.getVelocity();
                                arrivalTime = deltaT + this.evTime();
                                startTime = nextEvent.evTime() + car.getDelayToStart();
                            } else {
                                if (nextEvent.getDesiredStatus() == ROJO) {
                                    newEvent = CE;
                                    arrivalPosition = PosiblearrivalPosition;
                                    deltaP = PosibledeltaP;
                                    deltaT = PosibledeltaT;
                                    arrivalTime = deltaT + this.evTime();
                                    startTime = arrivalTime;
                                } else {
                                    if (nextEvent.getDesiredStatus() == AMBAR) {
                                        newEvent = CE;
                                        arrivalPosition = PosiblearrivalPosition;
                                        deltaP = PosibledeltaP;
                                        deltaT = PosibledeltaT;
                                        arrivalTime = deltaT + this.evTime();
                                        startTime = arrivalTime;
                                    }
                                }
                            }
                        } else {
                            if (nextEvent.getDesiredStatus() == VERDE) {
                                newEvent = CE;
                                arrivalPosition = PosiblearrivalPosition;
                                deltaP = PosibledeltaP;
                                deltaT = PosibledeltaT;
                                arrivalTime = deltaT + this.evTime();
                                startTime = arrivalTime;
                            } else {
                                if (nextEvent.getDesiredStatus() == ROJO) {
                                    newEvent = SE;
                                    arrivalPosition = PosiblearrivalPosition - car.getDisMinSafe();
                                    deltaP = arrivalPosition - this.evPosition();
                                    deltaT = (deltaP * 3600) / car.getVelocity();
                                    arrivalTime = deltaT + this.evTime();
                                    startTime = arrivalTime;
                                } else {
                                    newEvent = CE;
                                    arrivalPosition = PosiblearrivalPosition;
                                    deltaP = PosibledeltaP;
                                    deltaT = PosibledeltaT;
                                    arrivalTime = deltaT + this.evTime();
                                    startTime = arrivalTime;
                                }
                            }
                        }
                    } else {
                        if (newEvent == CE) {
                            if (PosiblearrivalTime > nextEvent.evTime()) {
                                if (nextEvent.getDesiredStatus() == ROJO) {
                                    arrivalPosition = nextEvent.evPosition() - car.getDisMinSafe();
                                    deltaP = arrivalPosition - this.evPosition();
                                    deltaT = (deltaP * 3600) / car.getVelocity();
                                    arrivalTime = deltaT + this.evTime();
                                    startTime = arrivalTime;
                                    newEvent = SE;
                                }
                            }
                        } else {
                            if ((newEvent == SE)) {
                                if (PosiblearrivalTime > nextEvent.evTime()) {
                                    if (nextEvent.getDesiredStatus() == VERDE || nextEvent.getDesiredStatus() == AMBAR) {
                                        arrivalPosition = nextEvent.evPosition();
                                        deltaP = arrivalPosition - this.evPosition();
                                        deltaT = (deltaP * 3600) / car.getVelocity();
                                        arrivalTime = deltaT + this.evTime();
                                        if (newEvent == SE) {
                                            startTime = arrivalTime + car.getDelayToStart();
                                        } else {
                                            startTime = arrivalTime;
                                        }
                                        newEvent = CE;
                                    }
                                } else {
                                    if (startTime < PosiblearrivalTime) {
                                        if (nextEvent.getDesiredStatus() == ROJO || nextEvent.getDesiredStatus() == AMBAR) {
                                            arrivalPosition = nextEvent.evPosition();
                                            deltaP = arrivalPosition - this.evPosition();
                                            deltaT = (deltaP * 3600) / car.getVelocity();
                                            arrivalTime = deltaT + this.evTime();
                                            if (newEvent == SE) {
                                                startTime = arrivalTime + car.getDelayToStart();
                                            } else {
                                                startTime = arrivalTime;
                                            }
                                            newEvent = CE;
                                        } else {
                                            startTime = nextEvent.evTime();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                nextEvent = nextEvent.siguiente;
            }
            if (newEvent == EMPTY) {
                arrivalPosition = segment.getSize();
                deltaP = arrivalPosition - this.evPosition();
                deltaT = (deltaP * 3600) / car.getVelocity();
                arrivalTime = deltaT + this.evTime();
                car.setTypeSeq(CE);
                car.setBegEvTime(this.evTime());
                car.setEndEvTime(arrivalTime);
                car.setCrossDeltaP(deltaP);
                car.setStopTime(arrivalTime);
                startTime = arrivalTime;
                newEvent = CE;
            }
            if (newEvent == CE && !this.segment.isPozo()) {
                if (false) {
                    newEvent = WSE;
                    arrivalPosition = segment.getSize() - car.getDisMinSafe();
                    deltaP = arrivalPosition - this.evPosition();
                    deltaT = (deltaP * 3600) / car.getVelocity();
                    arrivalTime = deltaT + this.evTime();
                    startTime = arrivalTime;
                    myLogger.debug(car.getId() + " coloco un  Warning Stop Event, ya que hiba a cruzar pero estaba lleno");
                } else {
                    car.setTypeSeq(CE);
                    car.setBegEvTime(this.evTime());
                    car.setEndEvTime(arrivalTime);
                    car.setCrossDeltaP(deltaP);
                    car.setStopTime(arrivalTime);
                    myLogger.debug(car.getId() + " coloco un  " + CE + " Y UN " + ALE + "a las " + arrivalTime + " y  " + startTime + " en pos " + arrivalPosition);
                    this.myStreetControllerAgent.putEvent(this.segment.getId(), arrivalTime, CE, car, arrivalPosition, AMBAR, this.lane);
                    this.myStreetControllerAgent.putEvent(this.segment.getConnections(), startTime, ALE, car, 0.0, AMBAR, this.lane);
                }
            }
            if ((newEvent == CE) && this.segment.isPozo()) {
                arrivalPosition = segment.getSize();
                deltaP = arrivalPosition - this.evPosition();
                deltaT = (deltaP * 3600) / car.getVelocity();
                arrivalTime = deltaT + this.evTime();
                car.setTypeSeq(CE);
                car.setBegEvTime(evTime());
                car.setEndEvTime(arrivalTime);
                car.setCrossDeltaP(deltaP);
                car.setStopTime(arrivalTime);
                startTime = arrivalTime;
                this.myStreetControllerAgent.putEvent(this.segment.getId(), arrivalTime, CE, car, arrivalPosition, 4, this.lane);
            }
            if (newEvent == SE || newEvent == WSE) {
                if (newEvent == SE) {
                    car.setTypeSeq(SE);
                } else {
                    car.setTypeSeq(WSE);
                }
                car.setBegEvTime(evTime());
                car.setEndEvTime(startTime);
                car.setStopDeltaP(deltaP);
                car.setStopTime(arrivalTime);
                if (newEvent == SE) {
                    myLogger.debug(car.getId() + " coloco un  " + SE + " Y UN " + BE + "a las " + arrivalTime + " y  " + startTime + " en pos " + arrivalPosition);
                    this.myStreetControllerAgent.putEvent(this.segment.getId(), arrivalTime, SE, car, arrivalPosition, AMBAR, this.lane);
                    this.myStreetControllerAgent.putEvent(this.segment.getId(), startTime, BE, car, arrivalPosition, AMBAR, this.lane);
                } else {
                    myLogger.debug(car.getId() + " coloco un  " + WSE + " Y UN " + WBE + "a las " + arrivalTime + " y  " + startTime + " en pos " + arrivalPosition);
                    this.myStreetControllerAgent.putEvent(this.segment.getId(), arrivalTime, WSE, car, arrivalPosition, AMBAR, this.lane);
                    this.myStreetControllerAgent.putEvent(this.segment.getId(), startTime, WBE, car, arrivalPosition, AMBAR, this.lane);
                }
            }
        } else {
            myLogger.debug("Esta lleno el segmento " + this.segment.getId() + "El carro " + car.getId() + " se ha desechado");
        }
        myLogger.debug("finishing At: " + this.evTime() + " event type:  " + this.getEventType());
        myStreetControllerAgent.setReady();
    }
