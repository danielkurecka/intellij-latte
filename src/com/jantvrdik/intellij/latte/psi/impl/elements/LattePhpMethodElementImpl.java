package com.jantvrdik.intellij.latte.psi.impl.elements;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import com.jantvrdik.intellij.latte.psi.elements.LattePhpMethodElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class LattePhpMethodElementImpl extends ASTWrapperPsiElement implements LattePhpMethodElement {

	public LattePhpMethodElementImpl(@NotNull ASTNode node) {
		super(node);
	}

	@Override
	public String getPhpElementName()
	{
		return getMethodName();
	}

	@Nullable
	public PsiReference getReference() {
		PsiReference[] references = getReferences();
		return references.length == 0 ? null : references[0];
	}

	@NotNull
	public PsiReference[] getReferences() {
		return ReferenceProvidersRegistry.getReferencesFromProviders(this);
	}
}