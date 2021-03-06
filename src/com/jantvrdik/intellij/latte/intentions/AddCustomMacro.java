package com.jantvrdik.intellij.latte.intentions;

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.codeInsight.intention.impl.BaseIntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import com.jantvrdik.intellij.latte.LatteLanguage;
import com.jantvrdik.intellij.latte.config.LatteMacro;
import com.jantvrdik.intellij.latte.settings.LatteCustomMacroSettings;
import com.jantvrdik.intellij.latte.settings.LatteSettings;
import org.jetbrains.annotations.NotNull;

/**
 * Base class for intentions which will register a custom macro.
 */
abstract public class AddCustomMacro extends BaseIntentionAction {

	/** custom macro which will be registered on invocation */
	protected final LatteCustomMacroSettings macro;

	public AddCustomMacro(String macroName) {
		this.macro = new LatteCustomMacroSettings(macroName, getMacroType());
	}

	@NotNull
	protected abstract LatteMacro.Type getMacroType();

	@NotNull
	@Override
	public String getFamilyName() {
		return "Latte";
	}

	@Override
	public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
		return file.getLanguage() == LatteLanguage.INSTANCE;
	}

	@Override
	public void invoke(@NotNull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
		LatteSettings.getInstance(project).customMacroSettings.add(macro);
		DaemonCodeAnalyzer.getInstance(project).restart(); // force re-analyzing
	}
}
