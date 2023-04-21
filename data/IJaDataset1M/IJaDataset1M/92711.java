package nps.event;

import java.util.EmptyStackException;

/**
 * �¼��������,FIFO
 * a new publishing system
 * Copyright (c) 2007
 *
 * @author jialin
 * @version 1.0
 */
public class EventQueue {

    private class Queue {

        NpsEvent queueHead;

        NpsEvent queueTail;
    }

    private Queue queue;

    private EventQueue next;

    private EventQueue prev;

    private NpsEvent currentEvent;

    private EventDispatchThread dispatchThread = null;

    private boolean nativeLoopRunning = false;

    private int threads = 0;

    private int MAX_THREADS = 25;

    private boolean isShutdown() {
        if (nativeLoopRunning) return false;
        if (peekEvent() != null) return false;
        return true;
    }

    public EventQueue() {
        queue = new Queue();
    }

    /**
      * Returns the next event in the queue.  This method will block until
      * an event is available or until the thread is interrupted.
      */
    public synchronized NpsEvent getNextEvent() throws InterruptedException {
        if (next != null) return next.getNextEvent();
        NpsEvent res = getNextEventImpl(true);
        while (res == null) {
            if (isShutdown()) {
                dispatchThread = null;
                threads--;
                throw new InterruptedException();
            }
            wait();
            res = getNextEventImpl(true);
        }
        return res;
    }

    /**
      * Fetches and possibly removes the next event from the internal queues.
      * This method returns immediately. When all queues are empty, this returns null
      */
    private NpsEvent getNextEventImpl(boolean remove) {
        NpsEvent next = null;
        if (queue.queueHead != null) {
            next = queue.queueHead;
            if (remove) {
                queue.queueHead = next.queueNext;
                if (queue.queueHead == null) queue.queueTail = null;
                next.queueNext = null;
            }
        }
        return next;
    }

    /**
      * Returns the next event in the queue without removing it from the queue.
      * This method will block until an event is available or until the thread
      * is interrupted.
      */
    public synchronized NpsEvent peekEvent() {
        if (next != null) return next.peekEvent();
        return getNextEventImpl(false);
    }

    /**
      * Returns the next event in the queue that has the specified id
      * without removing it from the queue.
      * This method will block until an event is available or until the thread
      * is interrupted.
      */
    public synchronized NpsEvent peekEvent(int id) {
        if (next != null) return next.peekEvent(id);
        NpsEvent evt = queue.queueHead;
        while (evt != null && evt.id != id) evt = evt.queueNext;
        return evt;
    }

    /**
      * Posts a new event to the queue.
      * Actually performs the event posting. This is needed because the
      * RI doesn't use the public postEvent() method when transferring events
      * between event queues in push() and pop().
      */
    public synchronized void postEvent(NpsEvent evt) {
        if (evt == null) throw new NullPointerException();
        if (!EventSubscriber.GetSubscriber().ContainsListener(evt)) return;
        if (next != null) {
            next.postEvent(evt);
            return;
        }
        if (queue.queueHead == null) {
            queue.queueHead = evt;
            queue.queueTail = evt;
        } else {
            queue.queueTail.queueNext = evt;
            queue.queueTail = evt;
        }
        if (dispatchThread == null || !dispatchThread.isAlive()) {
            if (threads <= MAX_THREADS) {
                dispatchThread = new EventDispatchThread(this);
                dispatchThread.start();
                threads++;
            }
        }
        notify();
    }

    /**
      * Allows a custom EventQueue implementation to replace this one.
      * All pending events are transferred to the new queue. Calls to postEvent,
      * getNextEvent, and peekEvent and others are forwarded to the pushed queue
      * until it is removed with a pop().
      */
    public synchronized void push(EventQueue newEventQueue) {
        if (newEventQueue == null) throw new NullPointerException();
        if (next != null) {
            next.push(newEventQueue);
            return;
        }
        if (dispatchThread == null) dispatchThread = new EventDispatchThread(this);
        synchronized (newEventQueue) {
            while (peekEvent() != null) {
                try {
                    newEventQueue.postEvent(getNextEvent());
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            newEventQueue.prev = this;
        }
        next = newEventQueue;
    }

    /** Transfer any pending events from this queue back to the parent queue that
       * was previously push()ed. Event dispatch from this queue is suspended.
       */
    protected void pop() throws EmptyStackException {
        EventQueue previous = prev;
        if (previous == null) throw new EmptyStackException();
        synchronized (previous) {
            synchronized (this) {
                EventQueue nextQueue = next;
                if (nextQueue != null) {
                    nextQueue.pop();
                } else {
                    previous.next = null;
                    while (peekEvent() != null) {
                        try {
                            previous.postEvent(getNextEvent());
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                    prev = null;
                    if (dispatchThread != null) {
                        dispatchThread.interrupt();
                        dispatchThread = null;
                        threads--;
                    }
                }
            }
        }
    }

    /**
      * Dispatches an event. The manner in which the event is dispatched depends
      * upon the type of the event and the type of the event.
      */
    protected void dispatchEvent(NpsEvent evt) {
        currentEvent = evt;
        if (evt instanceof InsertEvent) {
            EventSubscriber.GetSubscriber().FireNewEvent((InsertEvent) evt);
        } else if (evt instanceof UpdateEvent) {
            EventSubscriber.GetSubscriber().FireUpdateEvent((UpdateEvent) evt);
        } else if (evt instanceof DeleteEvent) {
            EventSubscriber.GetSubscriber().FireDeleteEvent((DeleteEvent) evt);
        } else if (evt instanceof Ready2PublishEvent) {
            EventSubscriber.GetSubscriber().FireReady2PublishEvent((Ready2PublishEvent) evt);
        } else if (evt instanceof PublishEvent) {
            EventSubscriber.GetSubscriber().FirePublishEvent((PublishEvent) evt);
        } else if (evt instanceof CancelEvent) {
            EventSubscriber.GetSubscriber().FireCancelEvent((CancelEvent) evt);
        }
    }
}
