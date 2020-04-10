package com.jantvrdik.intellij.latte.syntaxHighlighter;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import com.jantvrdik.intellij.latte.lexer.LatteLexer;
import com.jantvrdik.intellij.latte.psi.LatteTypes;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class LatteSyntaxHighlighter extends SyntaxHighlighterBase {

	public static final TextAttributesKey HTML_NATTR_NAME = createTextAttributesKey("LATTE_HTML_NATTR_NAME", DefaultLanguageHighlighterColors.KEYWORD);
	public static final TextAttributesKey HTML_NATTR_VALUE = createTextAttributesKey("LATTE_HTML_NATTR_VALUE", DefaultLanguageHighlighterColors.STRING);
	public static final TextAttributesKey MACRO_DELIMITERS = createTextAttributesKey("LATTE_MACRO_DELIMITERS", DefaultLanguageHighlighterColors.BRACKETS);
	public static final TextAttributesKey MACRO_NAME = createTextAttributesKey("LATTE_MACRO_NAME", DefaultLanguageHighlighterColors.KEYWORD);
	public static final TextAttributesKey MACRO_ARGS_VAR = createTextAttributesKey("LATTE_MACRO_ARGS_VAR", DefaultLanguageHighlighterColors.INSTANCE_FIELD);
	public static final TextAttributesKey MACRO_ARGS_STRING = createTextAttributesKey("LATTE_MACRO_ARGS_STRING", DefaultLanguageHighlighterColors.STRING);
	public static final TextAttributesKey MACRO_ARGS_NUMBER = createTextAttributesKey("LATTE_MACRO_ARGS_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
	public static final TextAttributesKey MACRO_MODIFIERS = createTextAttributesKey("LATTE_MACRO_MODIFIERS", DefaultLanguageHighlighterColors.METADATA);
	public static final TextAttributesKey MACRO_COMMENT = createTextAttributesKey("LATTE_MACRO_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT);
	public static final TextAttributesKey PHP_KEYWORD = createTextAttributesKey("LATTE_PHP_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
	public static final TextAttributesKey PHP_CLASS_NAME = createTextAttributesKey("LATTE_PHP_CLASS_NAME", DefaultLanguageHighlighterColors.CLASS_REFERENCE);
	public static final TextAttributesKey PHP_METHOD = createTextAttributesKey("LATTE_PHP_METHOD", DefaultLanguageHighlighterColors.INSTANCE_METHOD);
	public static final TextAttributesKey PHP_IDENTIFIER = createTextAttributesKey("LATTE_PHP_IDENTIFIER", DefaultLanguageHighlighterColors.INSTANCE_FIELD);
	public static final TextAttributesKey PHP_CONTENT_TYPE = createTextAttributesKey("LATTE_PHP_CONTENT_TYPE", DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL);
	public static final TextAttributesKey PHP_CAST = createTextAttributesKey("LATTE_PHP_CAST", DefaultLanguageHighlighterColors.LABEL);
	public static final TextAttributesKey PHP_TYPE = createTextAttributesKey("LATTE_PHP_TYPE", DefaultLanguageHighlighterColors.KEYWORD);
	public static final TextAttributesKey PHP_NULL = createTextAttributesKey("LATTE_PHP_NULL", DefaultLanguageHighlighterColors.KEYWORD);

	@NotNull
	@Override
	public Lexer getHighlightingLexer() {
		return new LatteLexer();
	}

	@NotNull
	@Override
	public TextAttributesKey[] getTokenHighlights(IElementType token) {
		if (token == LatteTypes.T_HTML_TAG_NATTR_NAME) {
			return pack(HTML_NATTR_NAME);

		} else if (token == LatteTypes.T_MACRO_OPEN_TAG_OPEN || token == LatteTypes.T_MACRO_CLOSE_TAG_OPEN || token == LatteTypes.T_MACRO_TAG_CLOSE || token == LatteTypes.T_MACRO_TAG_CLOSE_EMPTY) {
			return pack(MACRO_DELIMITERS);

		} else if (token == LatteTypes.T_MACRO_NAME || token == LatteTypes.T_MACRO_SHORTNAME || token == LatteTypes.T_MACRO_NOESCAPE) {
			return pack(MACRO_NAME);

		} else if (token == LatteTypes.T_PHP_CONTENT_TYPE) {
			return pack(PHP_CONTENT_TYPE);

		} else if (token == LatteTypes.T_MACRO_ARGS_VAR) {
			return pack(MACRO_ARGS_VAR);

		} else if (
				token == LatteTypes.T_MACRO_ARGS_STRING
				|| token == LatteTypes.T_PHP_SINGLE_QUOTE_LEFT
				|| token == LatteTypes.T_PHP_SINGLE_QUOTE_RIGHT
				|| token == LatteTypes.T_PHP_DOUBLE_QUOTE_LEFT
				|| token == LatteTypes.T_PHP_DOUBLE_QUOTE_RIGHT) {
			return pack(MACRO_ARGS_STRING);

		} else if (token == LatteTypes.T_PHP_KEYWORD || token == LatteTypes.T_PHP_AS) {
			return pack(PHP_KEYWORD);

		} else if (token == LatteTypes.T_PHP_CLASS_NAME) {
			return pack(PHP_CLASS_NAME);

		} else if (token == LatteTypes.T_PHP_METHOD) {
			return pack(PHP_METHOD);

		} else if (token == LatteTypes.T_PHP_TYPE) {
			return pack(PHP_TYPE);

		} else if (token == LatteTypes.T_PHP_CAST) {
			return pack(PHP_CAST);

		} else if (token == LatteTypes.T_PHP_NULL) {
			return pack(PHP_NULL);

		} else if (token == LatteTypes.T_PHP_IDENTIFIER) {
			return pack(PHP_IDENTIFIER);

		} else if (token == LatteTypes.T_HTML_TAG_ATTR_DQ || token == LatteTypes.T_HTML_TAG_ATTR_SQ || token == LatteTypes.T_HTML_TAG_ATTR_EQUAL_SIGN) {
			return pack(HTML_NATTR_VALUE);

		} else if (token == LatteTypes.T_MACRO_FILTERS) {
			return pack(MACRO_MODIFIERS);

		} else if (token == LatteTypes.T_MACRO_ARGS_NUMBER) {
			return pack(MACRO_ARGS_NUMBER);

		} else if (token == LatteTypes.T_MACRO_COMMENT) {
			return pack(MACRO_COMMENT);

		} else {
			return EMPTY; // todo: after plugin will be for minimum 192.4147 replace with TextAttributesKey.EMPTY_ARRAY
		}
	}
}
