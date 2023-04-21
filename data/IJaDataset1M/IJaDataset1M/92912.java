package org.norecess.nolatte.primitives;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertSame;
import java.util.ArrayList;
import java.util.List;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.norecess.nolatte.ast.Datum;
import org.norecess.nolatte.ast.Identifier;
import org.norecess.nolatte.environments.IEnvironment;
import org.norecess.nolatte.interpreters.IInterpreter;
import org.norecess.nolatte.primitives.group.IOneArgumentPrimitive;

public class CompositionOfPrimitivesTest {

    private List<IOneArgumentPrimitive> myPrimitives;

    private IOneArgumentPrimitive myPrimitive0;

    private IOneArgumentPrimitive myPrimitive1;

    private IOneArgumentPrimitive myPrimitive2;

    private IInterpreter myInterpreter;

    private IEnvironment myEnvironment;

    private Datum myRawDatum;

    private Datum myDatum0;

    private Datum myDatum1;

    private Datum myAnswer;

    private CompositionOfPrimitives myCompositionOfPrimitives;

    @Before
    public void setUp() {
        myPrimitives = new ArrayList<IOneArgumentPrimitive>();
        myPrimitive0 = createMock(IOneArgumentPrimitive.class);
        myPrimitive1 = createMock(IOneArgumentPrimitive.class);
        myPrimitive2 = createMock(IOneArgumentPrimitive.class);
        myInterpreter = createMock(IInterpreter.class);
        myEnvironment = createMock(IEnvironment.class);
        expectFreeAccessToInterpretersAccessors();
        myRawDatum = createMock(Datum.class);
        myDatum0 = createMock(Datum.class);
        myDatum1 = createMock(Datum.class);
        myAnswer = createMock(Datum.class);
        myCompositionOfPrimitives = new CompositionOfPrimitives(myPrimitives);
    }

    public void replay() {
        EasyMock.replay(myPrimitive0, myPrimitive1, myPrimitive2, myInterpreter, myEnvironment, myRawDatum, myDatum0, myDatum1, myAnswer);
    }

    public void verify() {
        EasyMock.verify(myPrimitive0, myPrimitive1, myPrimitive2, myInterpreter, myEnvironment, myRawDatum, myDatum0, myDatum1, myAnswer);
    }

    @Test
    public void should_do_nothing_for_no_primitives() {
        expect(myEnvironment.get(Identifier.createParameter("datum"))).andReturn(myRawDatum);
        replay();
        assertSame(myRawDatum, myCompositionOfPrimitives.apply(myInterpreter));
        verify();
    }

    @Test
    public void should_apply_one_primitive() {
        myPrimitives.add(myPrimitive0);
        expect(myEnvironment.get(Identifier.createParameter("datum"))).andReturn(myRawDatum);
        expect(myPrimitive0.apply(myRawDatum)).andReturn(myAnswer);
        replay();
        assertSame(myAnswer, myCompositionOfPrimitives.apply(myInterpreter));
        verify();
    }

    @Test
    public void should_apply_many_primitives() {
        myPrimitives.add(myPrimitive0);
        myPrimitives.add(myPrimitive1);
        myPrimitives.add(myPrimitive2);
        expect(myEnvironment.get(Identifier.createParameter("datum"))).andReturn(myRawDatum);
        expect(myPrimitive0.apply(myRawDatum)).andReturn(myDatum0);
        expect(myPrimitive1.apply(myDatum0)).andReturn(myDatum1);
        expect(myPrimitive2.apply(myDatum1)).andReturn(myAnswer);
        replay();
        assertSame(myAnswer, myCompositionOfPrimitives.apply(myInterpreter));
        verify();
    }

    private void expectFreeAccessToInterpretersAccessors() {
        expect(myInterpreter.getEnvironment()).andReturn(myEnvironment).anyTimes();
    }
}
