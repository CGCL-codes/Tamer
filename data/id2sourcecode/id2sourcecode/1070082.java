    @Override
    public void run() {
        myLogger.debug("Executing At: " + this.evTime() + " event type:  " + this.getEventType());
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
            throw new UnsupportedOperationException("The output file log cant be created !!");
        }
        Car car = (Car) objectId;
        if (!this.segment.isFull(evTime())) {
            this.segment.putCar(car, new Double(0.0));
            nextEvent = root.siguiente;
            evaluatedPosition = segment.getSize();
            while (nextEvent != root) {
                if (nextEvent.getEventType() != LCE && nextEvent.getLane() == getLane()) {
                    if (nextEvent.evPosition() <= evaluatedPosition) {
                        if (nextEvent.evPosition() > this.evPosition()) {
                            if (nextEvent.getEventType() == BE || nextEvent.getEventType() == WBE || nextEvent.getEventType() == CE) {
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
                    PosiblearrivalTime = PosibledeltaT + evTime();
                    if (newEvent == EMPTY) {
                        if (PosiblearrivalTime < nextEvent.evTime()) {
                            if (nextEvent.getDesiredStatus() == VERDE) {
                                newEvent = SE;
                                arrivalPosition = PosiblearrivalPosition - car.getDisMinSafe();
                                deltaP = arrivalPosition - this.evPosition();
                                deltaT = (deltaP * 3600) / car.getVelocity();
                                arrivalTime = deltaT + evTime();
                                startTime = nextEvent.evTime() + car.getDelayToStart();
                            } else {
                                if (nextEvent.getDesiredStatus() == ROJO) {
                                    newEvent = CE;
                                    arrivalPosition = PosiblearrivalPosition;
                                    deltaP = PosibledeltaP;
                                    deltaT = PosibledeltaT;
                                    arrivalTime = deltaT + evTime();
                                    startTime = arrivalTime;
                                } else {
                                    if (nextEvent.getDesiredStatus() == AMBAR) {
                                        newEvent = CE;
                                        arrivalPosition = PosiblearrivalPosition;
                                        deltaP = PosibledeltaP;
                                        deltaT = PosibledeltaT;
                                        arrivalTime = deltaT + evTime();
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
                                arrivalTime = deltaT + evTime();
                                startTime = arrivalTime;
                            } else {
                                if (nextEvent.getDesiredStatus() == ROJO) {
                                    newEvent = SE;
                                    arrivalPosition = PosiblearrivalPosition - car.getDisMinSafe();
                                    deltaP = arrivalPosition - this.evPosition();
                                    deltaT = (deltaP * 3600) / car.getVelocity();
                                    arrivalTime = deltaT + evTime();
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
                    if (LOG) {
                        System.out.println(car.getId() + " coloco un  Warning Stop Event, ya que hiba a cruzar pero estaba lleno");
                    }
                } else {
                    car.setTypeSeq(CE);
                    car.setBegEvTime(this.evTime());
                    car.setEndEvTime(arrivalTime);
                    car.setCrossDeltaP(deltaP);
                    car.setStopTime(arrivalTime);
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
                car.setBegEvTime(this.evTime());
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
                car.setBegEvTime(this.evTime());
                car.setEndEvTime(startTime);
                car.setStopDeltaP(deltaP);
                car.setStopTime(arrivalTime);
                if (newEvent == SE) {
                    this.myStreetControllerAgent.putEvent(this.segment.getId(), arrivalTime, SE, car, arrivalPosition, AMBAR, this.lane);
                    this.myStreetControllerAgent.putEvent(this.segment.getId(), startTime, BE, car, arrivalPosition, AMBAR, this.lane);
                } else {
                    this.myStreetControllerAgent.putEvent(this.segment.getId(), arrivalTime, WSE, car, arrivalPosition, AMBAR, this.lane);
                    this.myStreetControllerAgent.putEvent(this.segment.getId(), startTime, WBE, car, arrivalPosition, AMBAR, this.lane);
                }
            }
        } else {
            throw new UnsupportedOperationException("The street is full and is executing an arrivalLink is a BIG ERROR!!!, because in past time must be evaluated this, and dont allow happens this situations");
        }
        myLogger.debug("Finishing At: " + this.evTime() + " event type:  " + this.getEventType());
        myStreetControllerAgent.setReady();
    }
