package jcomplexity;

import java.lang.reflect.Method;
import jcomplexity.algorithm.BubbleSort;
import jcomplexity.gui.MainWindow;
import jcomplexity.parameter.IntegerParameter;
import jcomplexity.task.Runner;
import jcomplexity.task.Task;
import com.trolltech.qt.gui.QApplication;

public class Launcher {

    public static void main(String[] args) {
        new Launcher(args);
    }

    public Launcher(String[] args) {
        QApplication.initialize(args);
        Runner runner = new Runner();
        Method bubbleSort;
        try {
            bubbleSort = BubbleSort.class.getMethod("bubbleSort", int[].class);
            runner.getTasks().add(new Task("bubbeltjes", bubbleSort));
            runner.getTasks().add(new Task("sorteren", bubbleSort));
            runner.getParameters().add(new IntegerParameter("randInt"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        MainWindow mainWindow = new MainWindow(runner);
        mainWindow.show();
        QApplication.exec();
    }
}
