package jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer;

import java.io.File;
import java.io.IOException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor.ClassInfoAccessor;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.METRIC_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer.graph_builder.SimpleGraphBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer.graph_builder.GraphvizWriter;

/**
 * �v���O�����v�f�Ԃ̊֌W���o�͂���v���O�C��.
 * 
 * ����̑Ή������ Java �̂�.
 * ���\�b�h�i�֐��j�͕K���N���X�̒��Œ�`���ꂽ����݂̂��������Ȃ�����.
 * 
 * �����v�����Ȃ�.
 * 
 * @author rniitani
 */
public class RelationVisualizerPlugin extends AbstractPlugin {

    @Override
    protected void execute() {
        final ClassInfoAccessor classAccessor = this.getClassInfoAccessor();
        final int ADD_VERTEX_PHASE_PROGRESS = 45;
        final int ADD_EDGE_PHASE_PROGRESS = 90;
        final int WRITE_FILE_PHASE_PROGRESS = 100;
        SimpleGraphBuilder builder = new SimpleGraphBuilder();
        for (final TargetClassInfo targetClass : classAccessor) {
            builder.addClass(targetClass);
            for (final TargetMethodInfo targetMethod : targetClass.getDefinedMethods()) {
                builder.addMethod(targetClass, targetMethod);
            }
            for (final TargetFieldInfo targetField : targetClass.getDefinedFields()) {
                builder.addField(targetClass, targetField);
            }
        }
        this.reportProgress(ADD_VERTEX_PHASE_PROGRESS);
        for (final TargetClassInfo targetClass : classAccessor) {
            for (final ClassInfo subclass : targetClass.getSubClasses()) {
                builder.addSuperclassRelation(targetClass, subclass);
            }
            for (final ClassInfo inner : targetClass.getInnerClasses()) {
                builder.addSuperclassRelation(targetClass, inner);
            }
            for (final TargetMethodInfo targetMethod : targetClass.getDefinedMethods()) {
                for (final MethodInfo callee : targetMethod.getCallees()) {
                    builder.addCallRelation(targetMethod, callee);
                }
            }
        }
        this.reportProgress(ADD_EDGE_PHASE_PROGRESS);
        try {
            GraphvizWriter.write(new File(getPluginRootDir(), "graph.dot"), builder.getResult());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.reportProgress(WRITE_FILE_PHASE_PROGRESS);
    }

    /**
     * ���g���N�X����Ԃ��D
     * 
     * @return ���g���N�X��
     */
    @Override
    protected String getMetricName() {
        return "RV";
    }

    /**
     * �Ƃ肠�����N���X�̃��g���N�X���v�����Ă��邱�Ƃɂ���.
     */
    @Override
    protected METRIC_TYPE getMetricType() {
        return METRIC_TYPE.CLASS_METRIC;
    }
}
