package com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.visualisation.BaseVisitor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FieldAppearanceAnalyser extends BaseVisitor {

    Map<Field, Integer> fieldAppearances = new HashMap<>();

    @Override
    public ConstraintNode visit(ConstraintNode constraintNode){
        constraintNode.getAtomicConstraints().stream()
            .map(AtomicConstraint::getField)
            .distinct()
            .forEach(this::countField);
        return constraintNode;
    }

    private void countField(Field field) {
        if (fieldAppearances.containsKey(field)){
            fieldAppearances.put(field, fieldAppearances.get(field) + 1);
        }
        else {
            fieldAppearances.put(field, 1);
        }
    }
}
