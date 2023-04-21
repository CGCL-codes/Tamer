package cunei.corpus;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import cunei.bits.ResizableUnsignedArray;
import cunei.bits.ResizableUnsignedHash;
import cunei.bits.UnsignedArray;
import cunei.bits.UnsignedHash;
import cunei.type.Annotation;
import cunei.type.AnnotationType;
import cunei.type.Type;
import cunei.type.TypeIndex;
import cunei.util.Bounds;
import cunei.util.IntegerBoundIndex;

public class AnnotationIndex implements Serializable {

    private static final long serialVersionUID = 1L;

    private TypeIndex<AnnotationType> typeIndex;

    private IntegerBoundIndex positionBounds;

    private UnsignedArray isReference;

    private UnsignedArray annotationValues;

    private UnsignedHash annotationParents;

    public AnnotationIndex(AnnotationType type) {
        typeIndex = new TypeIndex<AnnotationType>(type);
        positionBounds = new IntegerBoundIndex();
        isReference = new ResizableUnsignedArray();
        annotationValues = new ResizableUnsignedArray();
        annotationParents = new ResizableUnsignedHash();
    }

    protected void set(final int pos, final Set<Annotation> annotations) {
        set(pos, annotations, annotations == null ? null : new IdentityHashMap<Annotation, Integer>());
    }

    protected void set(final int pos, final Set<Annotation> annotations, final Map<Annotation, Integer> annotationsHash) {
        int childLocation = annotationValues.size();
        if (annotations != null) {
            final Set<Annotation> minimalAnnotations = new HashSet<Annotation>(annotations);
            for (Annotation annotation : annotations) {
                annotation = annotation.getParent();
                while (annotation != null) {
                    minimalAnnotations.remove(annotation);
                    annotation = annotation.getParent();
                }
            }
            for (Annotation annotation : minimalAnnotations) {
                final LinkedList<Annotation> stack = new LinkedList<Annotation>();
                do {
                    stack.push(annotation);
                    annotation = annotation.getParent();
                } while (annotation != null && !annotationsHash.containsKey(annotation));
                for (Annotation childAnnotation : stack) {
                    Integer existingLocation = annotationsHash.get(childAnnotation);
                    if (existingLocation == null) {
                        final int typeId = typeIndex.add(childAnnotation.getValue());
                        annotationValues.set(childLocation, typeId);
                        final Annotation parentAnnotation = childAnnotation.getParent();
                        if (parentAnnotation != null) {
                            final Integer parentLocation = annotationsHash.get(parentAnnotation);
                            annotationParents.set(childLocation, childLocation - parentLocation - 1);
                        }
                        annotationsHash.put(childAnnotation, childLocation);
                    } else {
                        isReference.set(childLocation, 1);
                        annotationValues.set(childLocation, childLocation - existingLocation - 1);
                    }
                    childLocation++;
                }
            }
        }
        positionBounds.setUpperBound(pos, childLocation);
    }

    protected Set<Annotation> get(final int pos) {
        return get(pos, new HashMap<Integer, Annotation>());
    }

    protected Set<Annotation> get(int pos, final Map<Integer, Annotation> annotationsHash) {
        if (positionBounds == null) return null;
        final Bounds bounds = positionBounds.getBounds(pos);
        if (!bounds.isActive()) return null;
        final Set<Annotation> result = new HashSet<Annotation>();
        final int lowerBound = bounds.getLower();
        for (int loc = bounds.getUpper() - 1; loc >= lowerBound; loc--) {
            Annotation annotation = getAnnotationsByLocation(loc, annotationsHash);
            while (annotation != null && result.add(annotation)) annotation = annotation.getParent();
        }
        return result;
    }

    private Annotation getAnnotationsByLocation(final int childLocation, final Map<Integer, Annotation> annotationsHash) {
        if (isReference != null && isReference.get(childLocation) == 1) {
            final int childOffset = (int) annotationValues.get(childLocation);
            return getAnnotationsByLocation(childLocation - childOffset - 1, annotationsHash);
        }
        Annotation annotation = annotationsHash.get(childLocation);
        if (annotation == null) {
            final AnnotationType type = typeIndex.getTypeOfTypes();
            final Type value = typeIndex.get((int) annotationValues.get(childLocation));
            if (annotationParents != null) {
                int parentOffset = (int) annotationParents.get(childLocation);
                if (parentOffset >= 0) {
                    annotation = getAnnotationsByLocation(childLocation - parentOffset - 1, annotationsHash);
                    annotation = type.getAnnotation(value, annotation);
                }
            }
            if (annotation == null) annotation = type.getAnnotation(value);
            annotationsHash.put(childLocation, annotation);
        }
        return annotation;
    }

    public void load(String path) {
        if (positionBounds != null) positionBounds.load(path);
        if (isReference != null) isReference.load(path);
        if (annotationValues != null) annotationValues.load(path);
        if (annotationParents != null) annotationParents.load(path);
    }

    public AnnotationIndex save(String path, String name) {
        positionBounds = annotationValues.size() == 0 ? null : positionBounds.save(path, name + "-positions");
        isReference = isReference.size() == 0 ? null : isReference.save(path, name + "-references");
        annotationValues = annotationValues.size() == 0 ? null : annotationValues.save(path, name + "-values");
        annotationParents = annotationParents.size() == 0 ? null : annotationParents.save(path, name + "-parents");
        return this;
    }
}
