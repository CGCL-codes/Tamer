                    public void filter(Event event) throws Event, Exception {
                        Iterator it = workers.iterator();
                        event.output().println("<pre>workers: {size: " + workers.size() + ", ");
                        while (it.hasNext()) {
                            Worker worker = (Worker) it.next();
                            event.output().print(" worker: {index: " + worker.index() + ", busy: " + worker.busy() + ", lock: " + worker.lock());
                            if (worker.event() != null) {
                                event.output().println(", ");
                                event.output().println("  event: {index: " + worker.event() + ", init: " + worker.event().reply().output.init + ", done: " + worker.event().reply().output.done + "}");
                                event.output().println(" }");
                            } else {
                                event.output().println("}");
                            }
                        }
                        event.output().println("}");
                        event.output().println("events: {size: " + events.size() + ", selected: " + selected + ", valid: " + valid + ", accept: " + accept + ", readwrite: " + readwrite + ", ");
                        it = events.values().iterator();
                        while (it.hasNext()) {
                            Event e = (Event) it.next();
                            event.output().println(" event: {index: " + e + ", last: " + (System.currentTimeMillis() - e.last()) + "}");
                        }
                        event.output().println("}</pre>");
                    }
