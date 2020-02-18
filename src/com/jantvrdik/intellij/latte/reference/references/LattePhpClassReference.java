package com.jantvrdik.intellij.latte.reference.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.jantvrdik.intellij.latte.psi.LattePhpClass;
import com.jantvrdik.intellij.latte.utils.LattePhpUtil;
import com.jantvrdik.intellij.latte.utils.LatteUtil;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LattePhpClassReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {
    private String className;
    private Collection<PhpClass> phpClasses;

    public LattePhpClassReference(@NotNull LattePhpClass element, TextRange textRange) {
        super(element, textRange);
        className = element.getClassName();
        phpClasses = element.getPhpType().getPhpClasses(element.getProject());
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean b) {
        if (phpClasses.size() == 0) {
            return new ResolveResult[0];
        }

        List<ResolveResult> results = new ArrayList<ResolveResult>();
        for (PhpClass phpClass : ((LattePhpClass) getElement()).getPhpType().getPhpClasses(getElement().getProject())) {
            if (LattePhpUtil.isReferenceFor(className, phpClass)) {
                results.add(new PsiElementResolveResult(phpClass));
            }
        }

        for (LattePhpClass method : LatteUtil.findClasses(getElement().getProject(), className)) {
            results.add(new PsiElementResolveResult(method));
        }

        return results.toArray(new ResolveResult[results.size()]);
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        ResolveResult[] resolveResults = multiResolve(false);
        return resolveResults.length > 0 ? resolveResults[0].getElement() : null;
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];
    }

    @Override
    public PsiElement handleElementRename(@NotNull String newName) {
        if (getElement() instanceof LattePhpClass) {
            ((LattePhpClass) getElement()).setName(newName);
        }
        return getElement();
    }

    @Override
    public boolean isReferenceTo(@NotNull PsiElement element) {
        if (element instanceof LattePhpClass) {
            Collection<PhpClass> originalClasses = ((LattePhpClass) element).getPhpType().getPhpClasses(element.getProject());
            if (originalClasses.size() > 0) {
                for (ResolveResult result : multiResolve(false)) {
                    if (!(result.getElement() instanceof LattePhpClass)) {
                        continue;
                    }

                    Collection<PhpClass> targetClasses = ((LattePhpClass) result.getElement()).getPhpType().getPhpClasses(element.getProject());
                    if (targetClasses.size() == 0) {
                        continue;
                    }

                    for (PhpClass targetClass : targetClasses) {
                        String targetFqn = targetClass.getFQN();
                        for (PhpClass originalClass : originalClasses) {
                            if (originalClass.getFQN().equals(targetFqn)) {
                                return true;
                            }
                        }
                    }
                }

                for (PhpClass originalClass : originalClasses) {
                    if (LattePhpUtil.isReferenceTo(originalClass, multiResolve(false), element, ((LattePhpClass) element).getClassName())) {
                        return true;
                    }
                }
            }
        }

        if (!(element instanceof PhpClass)) {
            return false;
        }
        return LattePhpUtil.isReferenceTo((PhpClass) element, multiResolve(false), element, ((PhpClass) element).getFQN());
    }

}