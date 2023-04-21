package com.springrts.ai.crans.pathing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.vecmath.Point2i;
import com.springrts.ai.crans.pathing.SolveResult.Result;

public class MicroPatherUITest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        JFrame jFrame = new JFrame();
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Map dumps", "mapdump");
        chooser.setFileFilter(filter);
        chooser.setCurrentDirectory(new File("C:\\Spring.83\\AI\\Skirmish\\CransJavaAI\\0.1\\logs-team-1"));
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());
        }
        final String movetilesString;
        final String costtilesString;
        final float baseCost;
        final int mapSizeX;
        final int mapSizeY;
        final Node startNode;
        final Set<Node> endNodes = new HashSet<Node>();
        try {
            final BufferedReader br = new BufferedReader(new FileReader(chooser.getSelectedFile()));
            movetilesString = br.readLine();
            costtilesString = br.readLine();
            baseCost = Float.parseFloat(br.readLine());
            mapSizeX = Integer.parseInt(br.readLine());
            mapSizeY = Integer.parseInt(br.readLine());
            String startPosString = br.readLine();
            startNode = toNode(startPosString, mapSizeX);
            String endPosString = br.readLine();
            while (endPosString != null) {
                endNodes.add(toNode(endPosString, mapSizeX));
                endPosString = br.readLine();
            }
        } catch (final IOException e) {
            e.printStackTrace();
            return;
        }
        final MicroPather micropather = new MicroPather(mapSizeX, mapSizeY);
        final boolean[] movetiles = new boolean[mapSizeX * mapSizeY];
        final float[] costtiles = new float[mapSizeX * mapSizeY];
        int movetilesIndex = 0;
        for (final String moveTileString : movetilesString.split("[, ]+")) {
            movetiles[movetilesIndex++] = Boolean.parseBoolean(moveTileString);
        }
        int costtilesIndex = 0;
        for (final String costTileString : costtilesString.split("[, ]+")) {
            costtiles[costtilesIndex++] = Float.parseFloat(costTileString);
        }
        micropather.setMapData(movetiles, baseCost, costtiles);
        final BufferedImage debugImage = doPathing(startNode, endNodes, micropather);
        final ImagePanel imagePanel = new ImagePanel(debugImage);
        final JLabel label = new JLabel("Status");
        imagePanel.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseMoved(MouseEvent e) {
                final Point realPoint = getMapPoint(imagePanel, e);
                Node node = new Node(realPoint.x + realPoint.y * mapSizeX);
                PathNode pathNode = micropather.getPathNodeMem()[node.index];
                label.setText("Pass: " + movetiles[node.index] + ", Value: " + (baseCost + costtiles[node.index]) + ", Frame: " + pathNode.frame + ", Cost from start: " + +pathNode.costFromStart + ", Total cost: " + pathNode.totalCost);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
            }
        });
        imagePanel.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                final Point realPoint = getMapPoint(imagePanel, e);
                Node node = new Node(realPoint.x + realPoint.y * mapSizeX);
                if (e.getButton() == MouseEvent.BUTTON1) {
                    endNodes.add(node);
                }
                if (e.getButton() == MouseEvent.BUTTON3) {
                    endNodes.remove(node);
                }
                imagePanel.image = doPathing(startNode, endNodes, micropather);
                imagePanel.repaint();
            }
        });
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.add(imagePanel, BorderLayout.CENTER);
        jFrame.add(label, BorderLayout.SOUTH);
        jFrame.pack();
        jFrame.setVisible(true);
    }

    private static Node toNode(String endPosString, final int mapSizeX) {
        String[] values = endPosString.split("[, ]+");
        Node node = new Node(Integer.parseInt(values[0]) + Integer.parseInt(values[1]) * mapSizeX);
        return node;
    }

    private static Point getMapPoint(final ImagePanel imagePanel, MouseEvent e) {
        Point point = e.getPoint();
        Point realPoint = new Point();
        realPoint.x = (int) ((float) point.x * (float) imagePanel.image.getWidth() / (float) imagePanel.getWidth());
        realPoint.y = (int) ((float) point.y * (float) imagePanel.image.getHeight() / (float) imagePanel.getHeight());
        return realPoint;
    }

    private static BufferedImage doPathing(final Node startNode, final Set<Node> endNodes, final MicroPather micropather) {
        final int mapSizeX = micropather.getMapWidth();
        final int mapSizeY = micropather.getMapHeight();
        final SolveResult result = micropather.findBestPathToAnyGivenPoint(startNode, endNodes);
        float maxValue = 0.0f;
        for (final float value : micropather.getCostArray()) {
            maxValue = Math.max(maxValue, value);
        }
        final BufferedImage debugImage = createImage(micropather.getMapWidth(), micropather.getMapHeight(), micropather.getCostArray(), micropather.getBaseCost(), 0.0f, micropather.getBaseCost() + maxValue, true);
        float max = 0.0f;
        for (PathNode pos : micropather.getPathNodeMem()) {
            if (micropather.getFrame() == pos.frame) {
                max = Math.max(max, pos.costFromStart);
            }
        }
        for (PathNode pos : micropather.getPathNodeMem()) {
            final Color drawColor = Color.YELLOW;
            Point2i point = node2XY(pos, mapSizeX, mapSizeY);
            if (micropather.getFrame() == pos.frame) {
                int val = (int) ((pos.costFromStart / max) * 255);
                Color myColor = new Color(val, val, 0);
                debugImage.setRGB(point.x, point.y, myColor.getRGB());
            }
        }
        drawBooleanMap(micropather.getMapWidth(), micropather.getMapHeight(), micropather.getCanMoveArray(), debugImage, null, Color.BLUE.darker());
        for (final Node node : endNodes) {
            final Color drawColor = Color.GREEN.brighter();
            Point2i point = node2XY(node, mapSizeX, mapSizeY);
            debugImage.setRGB(point.x, point.y, drawColor.getRGB());
        }
        if (result.result != Result.NO_SOLUTION) {
            for (Node pos : result.path) {
                final Color drawColor = Color.WHITE;
                Point2i point = node2XY(pos, mapSizeX, mapSizeY);
                debugImage.setRGB(point.x, point.y, drawColor.getRGB());
            }
        }
        return debugImage;
    }

    static class ImagePanel extends JPanel {

        private static final long serialVersionUID = 1L;

        BufferedImage image;

        public ImagePanel(BufferedImage image) {
            this.image = image;
        }

        public void paintComponent(Graphics g) {
            g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension result = new Dimension();
            result.width = image.getWidth();
            result.height = image.getHeight();
            return result;
        }
    }

    public static Point2i node2XY(final PathNode node, final int mapSizeX, final int mapSizeY) {
        final int index = node.index;
        final int y = index / mapSizeX;
        final int x = index - y * mapSizeX;
        return new Point2i(x, y);
    }

    public static Point2i node2XY(final Node node, final int mapSizeX, final int mapSizeY) {
        final int index = node.index;
        final int y = index / mapSizeX;
        final int x = index - y * mapSizeX;
        return new Point2i(x, y);
    }

    public static BufferedImage createImage(final int width, final int height, final float[] values, final float baseValue, final float minValue, final float maxValue, final boolean logaritmic) {
        final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                final float value = baseValue + values[x + y * width];
                if (value > maxValue || value < minValue) {
                    throw new IllegalArgumentException("Value out of range: (" + x + "," + y + "): " + value + ". Min: " + minValue + ", Max: " + maxValue);
                }
                final float offsetValue = value - minValue;
                final float offsetMax = maxValue - minValue;
                final float normalizedValue;
                if (!logaritmic) {
                    normalizedValue = offsetValue / offsetMax;
                } else {
                    normalizedValue = (float) (Math.log(offsetValue + 1.0f) / Math.log(offsetMax + 1.0f));
                }
                int byteValue = (int) ((255.0f * normalizedValue) + 0.5f);
                if (byteValue < 0 || byteValue > 255) {
                    throw new RuntimeException("Byte value out of range: (" + x + "," + y + "): " + byteValue + ". Min: " + minValue + ", Max: " + maxValue + ", In: " + value);
                }
                int rgb = 0xFF000000 | (byteValue << 16);
                bufferedImage.setRGB(x, y, rgb);
            }
        }
        return bufferedImage;
    }

    public static void drawBooleanMap(final int width, final int height, final boolean[] values, final BufferedImage bufferedImage, final Color trueColor, final Color falseColor) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                final Color drawColor = values[x + y * width] ? trueColor : falseColor;
                if (drawColor != null) {
                    bufferedImage.setRGB(x, y, drawColor.getRGB());
                }
            }
        }
    }
}
