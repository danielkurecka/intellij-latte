package com.jantvrdik.intellij.latte.utils;

import com.intellij.openapi.project.Project;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class LattePhpType {

    private final List<TypePart> types = new ArrayList<TypePart>();
    private final String name;
    private final PhpType phpType;
    private final boolean nullable;
    private boolean hasClass = false;

    public LattePhpType(String type) {
        this(null, type, false);
    }

    public LattePhpType(String type, boolean nullable) {
        this(null, type, nullable);
    }

    public LattePhpType(String name, String type) {
        this(name, type, false);
    }

    public LattePhpType(String name, String typeString, boolean nullable) {
        PhpType phpType = new PhpType();
        if (typeString == null || typeString.length() == 0) {
            types.add(new TypePart("mixed"));
            phpType.add("mixed");

        } else {
            String[] parts = typeString.split("\\|");
            for (String part : parts) {
                part = part.trim();
                if (part.length() == 0) {
                    continue;
                }

                if (part.toLowerCase().equals("null")) {
                    nullable = true;
                    continue;
                }

                if (LatteTypesUtil.isNativeTypeHint(part)) {
                    part = part.toLowerCase();
                } else {
                    hasClass = true;
                }

                phpType.add(part);
            }

            if (nullable) {
                phpType.add("null");
            }
        }
        this.name = name == null ? null : LattePhpUtil.normalizePhpVariable(name);
        this.phpType = phpType;
        this.nullable = nullable;
    }

    public String getName() {
        return name;
    }

    public boolean containsClasses() {
        return hasClass;
    }

    public boolean hasUndefinedClass(@NotNull Project project) {
        if (!containsClasses()) {
            return false;
        }

        for (String className : findClasses()) {
            if (LattePhpUtil.getClassesByFQN(project, className).size() == 0) {
                return true;
            }
        }
        return false;
    }

    public boolean hasClass(String className) {
        if (!containsClasses()) {
            return false;
        }
        String normalizedName = LattePhpUtil.normalizeClassName(className);
        return types.stream().anyMatch(typePart -> typePart.isClass && typePart.getPart().equals(normalizedName));
    }

    public boolean hasClass(Collection<PhpClass> phpClasses) {
        if (!containsClasses()) {
            return false;
        }
        for (PhpClass phpClass : phpClasses) {
            if (hasClass(phpClass.getFQN())) {
                return true;
            }
        }
        return false;
    }

    public boolean isNullable() {
        return nullable;
    }

    public Collection<PhpClass> getPhpClasses(Project project) {
        List<PhpClass> output = new ArrayList<>();
        for (String wholeType : findClasses()) {
            output.addAll(LattePhpUtil.getClassesByFQN(project, wholeType));
        }
        return output;
    }

    String[] findClasses() {
        if (!containsClasses()) {
            return new String[0];
        }
        return types.stream()
                .filter(typePart -> typePart.isClass)
                .map(TypePart::getPart)
                .toArray(String[]::new);
    }

    @Override
    public String toString() {
        return toReadableString();
    }

    public String toReadableString() {
        return phpType.toStringResolved();
    }

    static class TypePart {
        String part;
        boolean isClass = false;
        boolean isNative = false;
        boolean isArrayOf = false;

        TypePart (@NotNull String part) {
            if (part.endsWith("[]")) {
                part = "array";
                this.isArrayOf = true; //todo: add support for types in array

            } else if (LatteTypesUtil.isNativeTypeHint(part)) {
                part = part.toLowerCase();
                this.isNative = true;

            } else {
                part = LattePhpUtil.normalizeClassName(part);
                this.isClass = true;
            }
            this.part = part;
        }

        String getPart() {
            return part;
        }
    }

}