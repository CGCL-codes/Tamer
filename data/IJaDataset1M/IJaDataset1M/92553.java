package lu.ng.urlchecker.designtesting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import lu.ng.urlchecker.commands.CheckUrlsCommand;
import lu.ng.urlchecker.commands.Command;
import lu.ng.urlchecker.commands.Context;
import lu.ng.urlchecker.commands.Result;
import lu.ng.urlchecker.commands.StandardContext;
import lu.ng.urlchecker.commands.URIValidateCommand;
import lu.ng.urlchecker.commands.URLMatchCommand;
import lu.ng.urlchecker.communication.CommunicationBuilder;
import lu.ng.urlchecker.events.ChainEvent;
import lu.ng.urlchecker.events.ChainListener;
import lu.ng.urlchecker.events.EventTypes;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DesignTest implements ChainListener {

    private static final Logger LOGGER = Logger.getLogger(DesignTest.class.getName());

    private Command resolver;

    private Command resolver2;

    private CommunicationBuilder builder;

    private Set<ChainEvent> chainListenerMonitor;

    public void fireEvent(final ChainEvent event) {
        chainListenerMonitor.add(event);
    }

    @Before
    public void setUp() throws Exception {
        builder = new CommunicationBuilder();
        resolver = new URLMatchCommand();
        resolver.setSuccessor(new URIValidateCommand());
        resolver2 = new URLMatchCommand();
        resolver2.setSuccessor(new URIValidateCommand());
        chainListenerMonitor = new LinkedHashSet<ChainEvent>();
    }

    @After
    public void tearDown() throws Exception {
        resolver = null;
        builder.shutdownCommunication();
    }

    @Test
    public void testChainListeners() {
        final InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("dummyData.xml");
        final Context context = new StandardContext();
        context.setSource(inStream);
        context.addListener(this);
        try {
            context.setClient(builder.getMultithreadedClient());
            resolver.getSuccessor().setSuccessor(new CheckUrlsCommand());
            final long t = System.currentTimeMillis();
            resolver.process(context);
            final long tt = System.currentTimeMillis();
            LOGGER.info("Total Time:" + (tt - t) + "ms");
            LOGGER.info("Size of Check:" + ObjectScale.sizeOf(CheckUrlsCommand.class) + " Bytes");
            assertFalse(chainListenerMonitor.isEmpty());
            assertEquals(19, chainListenerMonitor.size());
            Iterator<ChainEvent> iter = chainListenerMonitor.iterator();
            final EventTypes[] type = new EventTypes[] { EventTypes.START, EventTypes.END, EventTypes.START, EventTypes.END, EventTypes.START, EventTypes.FORK, EventTypes.FORK, EventTypes.FORK, EventTypes.FORK, EventTypes.FORK, EventTypes.FORK, EventTypes.FORK, EventTypes.FORK, EventTypes.FORK, EventTypes.FORK, EventTypes.FORK, EventTypes.FORK, EventTypes.FORK, EventTypes.END };
            final EventTypes[] collectedTypes = (EventTypes[]) CollectionUtils.collect(iter, TransformerUtils.invokerTransformer("getEventType")).toArray(new EventTypes[19]);
            for (int i = 0; i < type.length; i++) {
                assertEquals(type[i], collectedTypes[i]);
            }
            iter = chainListenerMonitor.iterator();
            while (iter.hasNext()) {
                final ChainEvent event = iter.next();
                LOGGER.debug("Event: " + event);
            }
        } catch (final ConfigurationException e) {
            LOGGER.error("Error occured in testGeneral :", e);
            fail();
        }
    }

    @Test
    public void testGeneralMultiThreaded() {
        final InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("dummyData.xml");
        final Context context = new StandardContext();
        context.setSource(inStream);
        Set<Result> results;
        try {
            context.setClient(builder.getMultithreadedClient());
            resolver.getSuccessor().setSuccessor(new CheckUrlsCommand());
            final long t = System.currentTimeMillis();
            results = resolver.process(context);
            final long tt = System.currentTimeMillis();
            LOGGER.info("Total Time:" + (tt - t) + "ms");
            LOGGER.info("Size of Check:" + ObjectScale.sizeOf(CheckUrlsCommand.class) + " Bytes");
            int validResults = 0;
            int invalidResults = 0;
            for (final Result r : results) {
                LOGGER.info("Result:" + r.toString());
                if (r.isValid()) {
                    validResults++;
                } else {
                    invalidResults++;
                }
            }
            assertFalse(null == results);
            assertFalse(results.isEmpty());
            assertEquals(14, results.size());
            assertEquals(10, validResults);
            assertEquals(4, invalidResults);
        } catch (final ConfigurationException e) {
            LOGGER.error("Error occured in testGeneral :", e);
            fail();
        }
    }

    public void testGeneralMultiThreaded1000() {
        for (int i = 0; i < 1000; i++) {
            final InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("dummyData.xml");
            final Context context = new StandardContext();
            context.setSource(inStream);
            Set<Result> results;
            try {
                context.setClient(builder.getMultithreadedClient());
                resolver.getSuccessor().setSuccessor(new CheckUrlsCommand());
                final long t = System.currentTimeMillis();
                results = resolver.process(context);
                final long tt = System.currentTimeMillis();
                LOGGER.info("Total Time:" + (tt - t) + "ms");
                LOGGER.info("Size of Check:" + ObjectScale.sizeOf(CheckUrlsCommand.class) + " Bytes");
                int validResults = 0;
                int invalidResults = 0;
                for (final Result r : results) {
                    LOGGER.info("Result:" + r.toString());
                    if (r.isValid()) {
                        validResults++;
                    } else {
                        invalidResults++;
                    }
                }
                assertFalse(null == results);
                assertFalse(results.isEmpty());
                assertEquals(14, results.size());
            } catch (final ConfigurationException e) {
                LOGGER.error("Error occured in testGeneral :", e);
                fail();
            }
        }
    }

    @Test
    public void testGeneralSinglethreaded() {
        final InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("dummyData.xml");
        final Context context = new StandardContext();
        context.setSource(inStream);
        Set<Result> results;
        try {
            context.setClient(builder.getSinglethreadedClient());
            resolver2.getSuccessor().setSuccessor(new CheckUrlsCommand());
            final long t = System.currentTimeMillis();
            results = resolver2.process(context);
            final long tt = System.currentTimeMillis();
            LOGGER.info("Total Time:" + (tt - t) + "ms");
            int validResults = 0;
            int invalidResults = 0;
            for (final Result r : results) {
                LOGGER.info("Result:" + r.toString());
                if (r.isValid()) {
                    validResults++;
                } else {
                    invalidResults++;
                }
            }
            assertFalse(null == results);
            assertFalse(results.isEmpty());
            assertEquals(14, results.size());
            assertEquals(10, validResults);
            assertEquals(4, invalidResults);
        } catch (final ConfigurationException e) {
            LOGGER.error("Error occured in testGeneral :", e);
            fail();
        }
    }
}
