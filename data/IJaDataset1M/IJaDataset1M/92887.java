package flattree.xstream;

import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.util.Map;
import java.util.TreeSet;
import com.thoughtworks.xstream.XStream;
import flattree.tree.AbstractNode;
import flattree.tree.DelimitedNode;
import flattree.tree.Node;
import flattree.tree.QuotedDelimitedLeaf;
import flattree.tree.SyntheticNode;

public class FlatHierarchicalStreamWriterTest extends AbstractStreamTest {

    public void testSingle() throws Exception {
        XStream xstream = createXStream(new FlatHierarchicalStreamDriver(getSingleRoot()));
        String string = xstream.toXML(new Foo());
        assertEqual(getSingleFlat(), string);
    }

    public void testMultiple() throws Exception {
        XStream xstream = createXStream(new FlatHierarchicalStreamDriver(getMultipleRoot()));
        StringWriter writer = new StringWriter();
        ObjectOutputStream stream = xstream.createObjectOutputStream(writer);
        stream.writeObject(new Foo());
        stream.writeObject(new Foo());
        stream.close();
        assertEqual(getMultipleFlat(), writer.toString());
    }

    public void testSingleCreateNode() throws Exception {
        XStream xstream = createXStream(new FlatHierarchicalStreamDriver() {

            @Override
            protected Node createNode(Node parent, String name, Map<String, String> values) {
                DelimitedNode node = new DelimitedNode(name);
                for (String key : new TreeSet<String>(values.keySet())) {
                    node.add(new QuotedDelimitedLeaf(key, ';', '"'));
                }
                if (parent != null) {
                    ((AbstractNode) parent).add(node);
                }
                return node;
            }
        });
        String string = xstream.toXML(new Foo());
        System.out.println(string);
    }

    public void testMultipleCreateNode() throws Exception {
        XStream xstream = createXStream(new FlatHierarchicalStreamDriver() {

            @Override
            protected Node createNode(Node parent, String name, Map<String, String> values) {
                if (parent == null) {
                    return new SyntheticNode("object-stream");
                } else {
                    DelimitedNode node = new DelimitedNode(name);
                    for (String key : new TreeSet<String>(values.keySet())) {
                        node.add(new QuotedDelimitedLeaf(key, ';', '"'));
                    }
                    ((AbstractNode) parent).add(node);
                    return node;
                }
            }
        });
        StringWriter writer = new StringWriter();
        ObjectOutputStream stream = xstream.createObjectOutputStream(writer);
        stream.writeObject(new Foo());
        stream.writeObject(new Foo());
        stream.close();
        System.out.println(writer.toString());
    }
}
