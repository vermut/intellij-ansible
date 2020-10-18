package lv.kid.vermut.intellij.yaml.reference;

import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import lv.kid.vermut.intellij.yaml.YamlIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pavels.Veretennikovs on 2015.05.19..
 */
public class AnsibleFileReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {
    private String key;

    public AnsibleFileReference(PsiElement element, TextRange rangeInElement) {
        super(element, rangeInElement);
        key = element.getText(); // .substring(rangeInElement.getStartOffset(), rangeInElement.getEndOffset());
        // QUICK-HACK to fix the key containing linebreaks and comments - better would be to fix the parser/lexer
        String stripped = key.replaceAll("(?m)(#.*$|\\s*$)\\n", "");
        key = stripped;
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        Project project = myElement.getProject();
        final List<PsiFile> properties = AnsibleUtil.findFiles(project, "/" + key);
        List<ResolveResult> results = new ArrayList<ResolveResult>();
        for (PsiFile property : properties) {
            results.add(new PsiElementResolveResult(property));
        }
        return results.toArray(new ResolveResult[results.size()]);
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        try {
            ResolveResult[] resolveResults = multiResolve(false);
            return resolveResults.length == 1 ? resolveResults[0].getElement() : guessBestMatch(resolveResults);
        } catch (NullPointerException ignored) {
            return null;
        }
    }

    // HACK to return files in the same module as best match
    @Nullable
    private PsiElement guessBestMatch(ResolveResult[] resolveResults) {
        String parentPath = getElement().getContainingFile().getParent().getVirtualFile().getCanonicalPath();
        PsiElement bestMatch = null;
        for (ResolveResult resolveResult : resolveResults) {
            if (resolveResult.getElement().getContainingFile().getVirtualFile().getCanonicalPath().startsWith(parentPath)) {
                // make sure we only return a bestMatch, if there is only one!
                if (bestMatch != null) {
                    bestMatch = null;
                    break;
                }
                bestMatch = resolveResult.getElement();
            }
        }
        return bestMatch;
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        Project project = myElement.getProject();
        final List<PsiFile> properties = AnsibleUtil.findFiles(project, "/" + key);
        List<LookupElementBuilder> variants = new ArrayList<LookupElementBuilder>();
        for (PsiFile property : properties) {
            variants.add(LookupElementBuilder.create(property).
                    withIcon(YamlIcons.FILETYPE_ICON).
                    withTypeText(property.getContainingFile().getName())
            );
        }
        return variants.toArray();
    }
}
