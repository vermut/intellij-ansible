package lv.kid.vermut.intellij.yaml.reference;

import com.intellij.lang.documentation.AbstractDocumentationProvider;
import com.intellij.psi.PsiElement;
import lv.kid.vermut.intellij.yaml.psi.NeonKey;
import lv.kid.vermut.intellij.yaml.psi.NeonKeyValPair;
import org.apache.http.client.fluent.Request;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import static com.intellij.patterns.PlatformPatterns.psiElement;
import static lv.kid.vermut.intellij.yaml.reference.AnsibleReferenceContributor.jinjaRefPattern;

/**
 * Created by Pavels.Veretennikovs on 2015.05.22..
 */
public class AnsibleVariableValuesDocumentationProvider extends AbstractDocumentationProvider {
    @Nullable
    public String getQuickNavigateInfo(PsiElement element, PsiElement originalElement) {
        if (jinjaRefPattern().accepts(originalElement)) {
            // Find all values of jinja var
            List<NeonKey> properties = AnsibleUtil.findAllProperties(originalElement.getProject(), originalElement.getText());
            StringBuilder result = new StringBuilder();

            for (NeonKey neonKey : properties) {
                if (neonKey.getParent() instanceof NeonKeyValPair) {
                    NeonKeyValPair keyValPair = (NeonKeyValPair) neonKey.getParent();
                    result.append(keyValPair.getValueText()).append("<br>");
                }
            }

            return result.toString();
        }

        // Try documentation lookup for key
        if (psiElement(NeonKeyValPair.class).accepts(originalElement)) {
            String url = MessageFormat.format("https://docs.ansible.com/ansible/latest/modules/{0}_module.html", ((NeonKeyValPair) originalElement).getKeyText());
            try {
                String pageData = Request.Get(url).execute().returnContent().asString();
                return pageData.substring(pageData.indexOf("<div role=\"main\""));
            } catch (StringIndexOutOfBoundsException e) {
                return String.format("Unexpected page structure at %s. Probably plugins needs an update", url);
            } catch (IOException e) {
                return "No documentation found at " + url;
            }
        }
        return null;
    }

    @Override
    public String generateDoc(PsiElement element, @Nullable PsiElement originalElement) {
        return getQuickNavigateInfo(element, element);
    }
}
