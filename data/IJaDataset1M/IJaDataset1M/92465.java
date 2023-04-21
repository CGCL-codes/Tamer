package net.sf.beanshield.aop;

import net.sf.beanshield.session.ShieldSession;
import net.sf.beanshield.session.spi.ShieldSessionFactory;
import net.sf.beanshield.test.AbstractBeanshieldTests;
import net.sf.beanshield.test.Company;
import net.sf.beanshield.test.Department;
import net.sf.beanshield.test.Person;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * @author: Manfred Geiler
 */
@SuppressWarnings({ "WhileLoopReplaceableByForEach" })
public class ProxyTests extends AbstractBeanshieldTests {

    public void testSetIterator_simpleIteration() throws Exception {
        ShieldSession session = ShieldSessionFactory.getInstance().createShieldSession();
        StringBuilder sb1 = new StringBuilder();
        Iterator<Department> it1 = snpp.getDepartments().iterator();
        while (it1.hasNext()) {
            Department department = it1.next();
            sb1.append(department).append('\n');
        }
        StringBuilder sb2 = new StringBuilder();
        Company companyProxy = session.getShieldProxy(snpp);
        Iterator<Department> it2 = companyProxy.getDepartments().iterator();
        while (it2.hasNext()) {
            Department department = it2.next();
            sb2.append(department).append('\n');
        }
        assertEquals(sb1.toString(), sb2.toString());
    }

    public void testListIterator_simpleIteration() throws Exception {
        ShieldSession session = ShieldSessionFactory.getInstance().createShieldSession();
        StringBuilder sb1 = new StringBuilder();
        ListIterator<Person> it1 = homer.getChildren().listIterator();
        while (it1.hasNext()) {
            Person child = it1.next();
            sb1.append(child).append('\n');
        }
        StringBuilder sb2 = new StringBuilder();
        ListIterator<Person> it2 = session.getShieldProxy(homer).getChildren().listIterator();
        while (it2.hasNext()) {
            Person child = it2.next();
            sb2.append(child).append('\n');
        }
        assertEquals(sb1.toString(), sb2.toString());
    }

    public void testIndexedListIterator_simpleIteration() throws Exception {
        ShieldSession session = ShieldSessionFactory.getInstance().createShieldSession();
        StringBuilder sb1 = new StringBuilder();
        ListIterator<Person> it1 = homer.getChildren().listIterator(1);
        while (it1.hasNext()) {
            Person child = it1.next();
            sb1.append(child).append('\n');
        }
        StringBuilder sb2 = new StringBuilder();
        ListIterator<Person> it2 = session.getShieldProxy(homer).getChildren().listIterator(1);
        while (it2.hasNext()) {
            Person child = it2.next();
            sb2.append(child).append('\n');
        }
        assertEquals(sb1.toString(), sb2.toString());
    }

    public void testIndexedListIterator_backwardsIteration() throws Exception {
        ShieldSession session = ShieldSessionFactory.getInstance().createShieldSession();
        StringBuilder sb1 = new StringBuilder();
        ListIterator<Person> it1 = homer.getChildren().listIterator(2);
        while (it1.hasPrevious()) {
            Person child = it1.previous();
            sb1.append(child).append('\n');
        }
        StringBuilder sb2 = new StringBuilder();
        ListIterator<Person> it2 = session.getShieldProxy(homer).getChildren().listIterator(2);
        while (it2.hasPrevious()) {
            Person child = it2.previous();
            sb2.append(child).append('\n');
        }
        assertEquals(sb1.toString(), sb2.toString());
    }

    public void testListIterator_remove() throws Exception {
        ShieldSession session = ShieldSessionFactory.getInstance().createShieldSession();
        ListIterator<Person> it = session.getShieldProxy(homer).getChildren().listIterator();
        assertEquals("Bart", it.next().getFirstname());
        assertEquals("Lisa", it.next().getFirstname());
        assertEquals("Maggie", it.next().getFirstname());
        assertEquals("Maggie", it.previous().getFirstname());
        assertEquals("Lisa", it.previous().getFirstname());
        it.remove();
        assertEquals("child count before commit", 3, homer.getChildren().size());
        session.commit();
        assertEquals("child count after commit", 2, homer.getChildren().size());
        for (Person person : homer.getChildren()) {
            if (person.getFirstname().equals("Lisa")) {
                fail("Lisa should have been removed");
            }
        }
    }

    public void testListIterator_set() throws Exception {
        ShieldSession session = ShieldSessionFactory.getInstance().createShieldSession();
        ListIterator<Person> it = session.getShieldProxy(homer).getChildren().listIterator();
        while (it.hasNext()) {
            it.next();
        }
        assertEquals("Maggie", it.previous().getFirstname());
        assertEquals("Lisa", it.previous().getFirstname());
        it.set(new Person("Simpson", "Marie", null, null));
        boolean foundLisa = false;
        boolean foundMarie = false;
        for (Person person : homer.getChildren()) {
            if (person.getFirstname().equals("Lisa")) {
                foundLisa = true;
            }
            if (person.getFirstname().equals("Marie")) {
                foundMarie = true;
            }
        }
        assertTrue("Lisa should be still there", foundLisa);
        assertFalse("Marie not yet committed", foundMarie);
        session.commit();
        foundLisa = false;
        foundMarie = false;
        for (Person person : homer.getChildren()) {
            if (person.getFirstname().equals("Lisa")) {
                foundLisa = true;
            }
            if (person.getFirstname().equals("Marie")) {
                foundMarie = true;
            }
        }
        assertFalse("Lisa should no longer be there", foundLisa);
        assertTrue("Marie should now be in list", foundMarie);
    }
}
