package dk.pervasive.jcaf.examples;

import java.rmi.*;
import dk.pervasive.jcaf.ContextEvent;
import dk.pervasive.jcaf.ContextService;
import dk.pervasive.jcaf.DiscoveryEvent;
import dk.pervasive.jcaf.DiscoveryListener;
import dk.pervasive.jcaf.Entity;
import dk.pervasive.jcaf.EntityListener;
import dk.pervasive.jcaf.entity.Person;
import dk.pervasive.jcaf.entity.Place;
import dk.pervasive.jcaf.impl.ContextServiceImpl;
import dk.pervasive.jcaf.impl.RemoteDiscoveryListenerImpl;
import dk.pervasive.jcaf.impl.RemoteEntityListenerImpl;
import dk.pervasive.jcaf.item.Location;
import dk.pervasive.jcaf.relationship.Located;
import dk.pervasive.jcaf.util.AbstractContextClient;

/**
 * @author <a href="mailto:bardram@daimi.au.dk">Jakob Bardram </a>
 *  
 */
public class PeerTester extends AbstractContextClient implements EntityListener, DiscoveryListener {

    private static int NO = 10;

    private ContextService cs[] = new ContextServiceImpl[NO];

    private static ContextService server;

    private RemoteEntityListenerImpl listener;

    private RemoteDiscoveryListenerImpl dl;

    public PeerTester(String service_uri) {
        super(service_uri);
        try {
            listener = new RemoteEntityListenerImpl();
            listener.addEntityListener(this);
        } catch (RemoteException e) {
        }
        try {
            dl = new RemoteDiscoveryListenerImpl();
            dl.addDiscoveryListener(this);
        } catch (RemoteException e) {
        }
        peers();
        load();
        listen();
        test();
    }

    private void peers() {
        String n[] = new String[NO];
        String uri[] = new String[NO];
        for (int i = 0; i < NO; i++) {
            n[i] = "cs" + i;
            uri[i] = "rmi://localhost/" + n[i];
        }
        try {
            cs[0] = new ContextServiceImpl(n[0]);
            for (int i = 1; i < NO; i++) {
                cs[i] = new ContextServiceImpl(n[i], uri[i - 1]);
            }
            cs[5].addContextService(cs[2]);
            cs[8].addContextService(cs[6]);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    Person p1 = new Person("hansen@acm.org", "Viggo Hansen");

    Person p2 = new Person("pedersen@acm.org", "Hans Pedersen");

    Person p3 = new Person("knudsen@acm.org", "Ole Knudsen");

    Place place = new Place("hopper.333");

    Located located = new Located(this.getClass().getName());

    private void load() {
        try {
            cs[0].addEntity(p1);
            cs[0].addEntity(p2);
            cs[1].addEntity(p3);
            cs[5].addEntity(place);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void listen() {
        try {
            cs[0].addEntityListener(listener, Person.class);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void test() {
        try {
            cs[5].lookupEntity("hansen@acm.org", 3, dl);
            cs[9].lookupEntity("hopper.333", 3, dl);
            cs[7].lookupEntity("hopper.333", 1, dl);
            cs[9].lookupEntity("knudsen@acm.org", 5, dl);
            cs[0].addContextItem(p1.getId(), located, new Location("loc://daimi.au.dk/hopper.333"));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            PeerTester tester = new PeerTester(args[0]);
        }
    }

    public void contextChanged(ContextEvent event) {
        System.out.println("context changed: ");
        Entity entity = event.getEntity();
        System.out.println(entity.toXML());
    }

    public void discovered(DiscoveryEvent event) {
        System.out.println("entity discovered in service : " + event.getServiceURI());
        Entity entity = event.getEntity();
        System.out.println(entity.toXML());
    }

    public void run() {
    }
}
