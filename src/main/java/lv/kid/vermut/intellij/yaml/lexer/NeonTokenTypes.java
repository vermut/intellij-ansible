package lv.kid.vermut.intellij.yaml.lexer;

import com.google.common.collect.ImmutableMap;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

import java.util.Map;

/**
 * Types of tokens returned form lexer
 *
 * @author Jan Dolecek - juzna.cz@gmail.com
 */
public interface NeonTokenTypes
{
	IElementType NEON_STRING = new NeonTokenType("string");
	IElementType NEON_COMMENT = new NeonTokenType("comment");
	IElementType NEON_INDENT = new NeonTokenType("indent");
	IElementType NEON_LITERAL = new NeonTokenType("literal");
	IElementType NEON_KEYWORD = new NeonTokenType("keyword");
	IElementType NEON_WHITESPACE = TokenType.WHITE_SPACE;
	IElementType NEON_UNKNOWN = TokenType.BAD_CHARACTER;

	IElementType NEON_TAG = new NeonTokenType("tag");
	IElementType NEON_HEADER = new NeonTokenType("header");

	// symbols
	IElementType NEON_COLON = new NeonTokenType(":");
	IElementType NEON_ASSIGNMENT = new NeonTokenType("=");
	IElementType NEON_ARRAY_BULLET = new NeonTokenType("-");
	IElementType NEON_ITEM_DELIMITER = new NeonTokenType(",");
	IElementType NEON_LINE_CONTINUATION = new NeonTokenType(">");

	// braces
	IElementType NEON_LBRACE_CURLY = new NeonTokenType("{");
	IElementType NEON_RBRACE_CURLY = new NeonTokenType("}");
	IElementType NEON_LBRACE_JINJA = new NeonTokenType("{{");
	IElementType NEON_RBRACE_JINJA = new NeonTokenType("}}");
	IElementType NEON_LBRACE_JINJA_CODE = new NeonTokenType("{%");
	IElementType NEON_RBRACE_JINJA_CODE = new NeonTokenType("%}");
	IElementType NEON_LBRACE_SQUARE = new NeonTokenType("[");
	IElementType NEON_RBRACE_SQUARE = new NeonTokenType("]");

	// special tokens
	IElementType NEON_KEY = new NeonTokenType("key");
	IElementType NEON_IDENTIFIER = new NeonTokenType("identifier");

	// sets
	TokenSet WHITESPACES = TokenSet.create(NEON_WHITESPACE);
	TokenSet COMMENTS = TokenSet.create(NEON_COMMENT);
	TokenSet STRING_LITERALS = TokenSet.create(NEON_LITERAL, NEON_STRING, NEON_TAG);
	TokenSet ASSIGNMENTS = TokenSet.create(NEON_ASSIGNMENT, NEON_COLON);
	TokenSet OPEN_BRACKET = TokenSet.create(NEON_LBRACE_CURLY, NEON_LBRACE_SQUARE);
	TokenSet CLOSING_BRACKET = TokenSet.create(NEON_RBRACE_CURLY, NEON_RBRACE_SQUARE);

	TokenSet OPEN_STRING_ALLOWED = TokenSet.create(
            NEON_COLON, NEON_ASSIGNMENT, NEON_ARRAY_BULLET,
			NEON_WHITESPACE, NEON_LITERAL, NEON_STRING, NEON_TAG,

			// Match brackets, as they would be inside the literal
			NEON_LBRACE_CURLY, NEON_LBRACE_SQUARE
    );

	// brackets
	public static final Map<IElementType, IElementType> closingBrackets = ImmutableMap.of(
		NEON_LBRACE_CURLY, NEON_RBRACE_CURLY,
		NEON_LBRACE_SQUARE, NEON_RBRACE_SQUARE
	);

}
