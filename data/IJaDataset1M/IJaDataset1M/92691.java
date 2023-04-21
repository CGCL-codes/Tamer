package org.amse.bomberman.client;

import org.amse.bomberman.common.threadfactory.DaemonThreadFactory;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.amse.bomberman.client.control.Controller;
import org.amse.bomberman.client.control.impl.ControllerImpl;
import org.amse.bomberman.client.control.impl.SimpleModelsContainer;
import org.amse.bomberman.client.net.ConnectorFactory;
import org.amse.bomberman.client.net.GenericConnector;
import org.amse.bomberman.client.viewmanager.ViewManager;
import org.amse.bomberman.protocol.impl.ProtocolMessage;

/**
 * Main class of client side part of application.
 *
 * @author Michail Korovkin
 * @author Kirilchuk V.E.
 */
public class Main {

    /**
     * Entry-point of client side part of application.
     * Congigure application and show GUI.
     *
     * @param args the command line arguments. Not supported.
     */
    public static void main(String[] args) {
        DaemonThreadFactory threadFactory = new DaemonThreadFactory();
        ConnectorFactory connectorFactory = new ConnectorFactory();
        GenericConnector<ProtocolMessage> connector = connectorFactory.newInstance();
        SimpleModelsContainer context = new SimpleModelsContainer();
        ExecutorService executors = Executors.newFixedThreadPool(2, threadFactory);
        Controller controller = new ControllerImpl(executors, connector, context);
        connector.setListener(controller);
        ViewManager viewManager = new ViewManager(controller);
        context.getConnectionStateModel().addListener(viewManager);
        Runtime.getRuntime().addShutdownHook(new ShutdowHook(executors, controller));
        viewManager.showGUI();
    }

    private static class ShutdowHook extends Thread {

        private final ExecutorService executors;

        private final Controller controller;

        public ShutdowHook(ExecutorService executors, Controller controller) {
            this.executors = executors;
            this.controller = controller;
        }

        @Override
        public void run() {
            if (controller != null) {
                controller.disconnect();
            }
            executors.shutdown();
        }
    }
}
