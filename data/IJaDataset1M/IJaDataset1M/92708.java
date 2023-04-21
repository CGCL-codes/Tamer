package org.openremote.controller.agent.proxy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import org.apache.log4j.Logger;
import org.openremote.controller.Constants;

public class ControllerProxy extends Proxy {

    private static Logger logger = Logger.getLogger(Constants.AGENT_LOG_CATEGORY);

    private int controllerPort;

    private String controllerIP;

    public ControllerProxy(SocketChannel srcSocket, String controllerIP, int controllerPort, int timeout) throws IOException {
        super(srcSocket, timeout);
        this.controllerIP = controllerIP;
        this.controllerPort = controllerPort;
    }

    public static SocketChannel makeClientSocket(String urlString, String token, int timeout) throws IOException {
        logger.info("Opening socket to beehive at " + urlString);
        URL url = new URL(urlString);
        SocketChannel socket = SocketChannel.open();
        socket.configureBlocking(false);
        try {
            logger.info("Trying to connect non-blocking");
            if (socket.connect(new InetSocketAddress(url.getHost(), url.getPort()))) {
                logger.info("Got socket to beehive");
                return socket;
            }
            logger.info("No luck, selecting");
            Selector selector = Selector.open();
            SelectionKey key = socket.register(selector, SelectionKey.OP_CONNECT);
            ByteBuffer buffer = ByteBuffer.wrap(token.getBytes("ASCII"));
            while (selector.select(timeout) > 0) {
                logger.info("Out of select");
                selector.selectedKeys().clear();
                if (key.isConnectable()) {
                    logger.info("Connectable");
                    if (socket.finishConnect()) {
                        logger.info("Got socket to beehive");
                        key.interestOps(SelectionKey.OP_WRITE);
                    }
                }
                if (key.isWritable()) {
                    logger.info("Writing token handshake to beehive");
                    socket.write(buffer);
                    if (!buffer.hasRemaining()) {
                        return socket;
                    }
                    logger.info("More to write, let's loop");
                }
                logger.info("Back in select");
            }
            throw new IOException("Connection timed out");
        } catch (IOException x) {
            logger.info("Got an exception while connecting: " + x.getMessage());
            try {
                socket.close();
            } catch (IOException x2) {
            }
            throw x;
        }
    }

    @Override
    protected SocketChannel openDestinationSocket() throws IOException {
        return SocketChannel.open(new InetSocketAddress(controllerIP, controllerPort));
    }
}
