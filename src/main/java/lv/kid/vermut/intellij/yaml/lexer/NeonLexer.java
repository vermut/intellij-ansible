package lv.kid.vermut.intellij.yaml.lexer;


import com.intellij.lexer.MergingLexerAdapter;
import com.intellij.psi.tree.TokenSet;

import java.io.Reader;


public class NeonLexer extends MergingLexerAdapter {
	// To be merged
	private static final TokenSet TOKENS_TO_MERGE = TokenSet.create(
			NeonTokenTypes.NEON_COMMENT,
			NeonTokenTypes.NEON_WHITESPACE
	);

	public NeonLexer() {
		super(new FlexAdapter(new _NeonLexer((Reader) null)), TOKENS_TO_MERGE);
	}
}
