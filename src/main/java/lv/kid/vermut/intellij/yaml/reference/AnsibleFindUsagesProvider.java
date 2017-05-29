package lv.kid.vermut.intellij.yaml.reference;

import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.TokenSet;
import lv.kid.vermut.intellij.yaml.lexer.NeonLexer;
import lv.kid.vermut.intellij.yaml.lexer.NeonTokenTypes;
import lv.kid.vermut.intellij.yaml.psi.NeonKey;
import lv.kid.vermut.intellij.yaml.psi.NeonKeyValPair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Pavels.Veretennikovs on 2015.05.20..
 */
public class AnsibleFindUsagesProvider implements FindUsagesProvider {

    private static final DefaultWordsScanner WORDS_SCANNER =
            new DefaultWordsScanner(new NeonLexer(),
                    TokenSet.create(NeonTokenTypes.NEON_LITERAL), TokenSet.create(NeonTokenTypes.NEON_COMMENT), TokenSet.create(NeonTokenTypes.NEON_STRING));

    @Nullable
    @Override
    public WordsScanner getWordsScanner() {
        return WORDS_SCANNER;
    }

    @Override
    public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
        return psiElement instanceof NeonKeyValPair;
    }

    @Nullable
    @Override
    public String getHelpId(@NotNull PsiElement psiElement) {
        return null;
    }

    @NotNull
    @Override
    public String getType(@NotNull PsiElement element) {
        if (element instanceof NeonKey) {
            return "NeonKey";
        } else {
            return "";
        }
    }

    @NotNull
    @Override
    public String getDescriptiveName(@NotNull PsiElement element) {
        if (element instanceof NeonKey) {
            return ((NeonKey) element).getKeyText();
        } else {
            return "getDescriptiveName";
        }
    }

    @NotNull
    @Override
    public String getNodeText(@NotNull PsiElement element, boolean useFullName) {
        if (element instanceof NeonKey) {
            return element.getParent().getText();
        } else {
            return "hosts";
        }
    }
}
