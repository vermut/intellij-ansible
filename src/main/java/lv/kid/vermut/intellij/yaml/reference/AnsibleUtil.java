package lv.kid.vermut.intellij.yaml.reference;

import com.intellij.openapi.fileTypes.FileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.indexing.FileBasedIndex;
import lv.kid.vermut.intellij.yaml.file.YamlFileType;
import lv.kid.vermut.intellij.yaml.psi.NeonFile;
import lv.kid.vermut.intellij.yaml.psi.NeonKey;
import lv.kid.vermut.intellij.yaml.psi.NeonKeyValPair;
import lv.kid.vermut.intellij.yaml.psi.NeonValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.PatternSyntaxException;

/**
 * Created by Vermut on 16/05/2015.
 */
public class AnsibleUtil {
    public static final String ALL = ".*";

    public static List<String> findRoleNames(Project project, String key) {
        List<String> result = new ArrayList<String>();
        Collection<PsiFile> virtualFiles = findFiles(project, "/" + key + "/tasks/main.yml$");
        for (PsiFile virtualFile : virtualFiles) {
            result.add(virtualFile.getParent().getParent().getName());
            result.add(virtualFile.getParent().getParent().getParent().getName());
        }
        return result;
    }

    public static List<PsiFile> findRoles(Project project, String key) {
        // Need to find also variants like role/subrole/tasks/main.yml
        return findFiles(project, "/" + key + ".*/tasks/main.yml$");
    }

    public static List<NeonKeyValPair> findAllProperties(Project project) {
        return searchKeyPairs(project, null, null);

    }

    public static List<NeonKey> findAllProperties(Project project, String key) {
        List<NeonKeyValPair> names = searchKeyPairs(project, key, null);
        List<NeonKey> result = new ArrayList<NeonKey>();
        for (NeonKeyValPair name : names) {
            result.add(name.getKey());
        }
        return result;
    }

    public static List<NeonKeyValPair> findNames(Project project) {
        return searchKeyPairs(project, "name", null);
    }

    public static List<NeonValue> findNames(Project project, String key) {
        List<NeonKeyValPair> names = searchKeyPairs(project, "name", key);
        List<NeonValue> result = new ArrayList<NeonValue>();
        for (NeonKeyValPair name : names) {
            result.add(name.getValue());
        }
        return result;
    }

    public static List<PsiFile> findFiles(Project project, String pattern) {
        List<PsiFile> result = new ArrayList<PsiFile>();
        try {
            Collection<VirtualFile> virtualFiles = FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, YamlFileType.INSTANCE,
                    GlobalSearchScope.allScope(project));
            for (VirtualFile virtualFile : virtualFiles) {
                if (virtualFile.getCanonicalPath() != null &&
                        virtualFile.getCanonicalPath().matches(".*" + pattern))
                    result.add(PsiManager.getInstance(project).findFile(virtualFile));
            }

            // Include .j2 - plaintexts
            virtualFiles = FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, FileTypes.PLAIN_TEXT,
                    GlobalSearchScope.allScope(project));
            for (VirtualFile virtualFile : virtualFiles) {
                if (virtualFile.getCanonicalPath() != null &&
                        virtualFile.getCanonicalPath().matches(".*" + pattern))
                    result.add(PsiManager.getInstance(project).findFile(virtualFile));
            }
        } catch (PatternSyntaxException ignored) {

        }
        return result;
    }

    private static List<NeonKeyValPair> searchKeyPairs(Project project, String key, String value) {
        List<NeonKeyValPair> result = new ArrayList<NeonKeyValPair>();
        Collection<VirtualFile> virtualFiles = FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, YamlFileType.INSTANCE,
                GlobalSearchScope.allScope(project));
        for (VirtualFile virtualFile : virtualFiles) {
            NeonFile yamlFile = (NeonFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (yamlFile != null) {
                Collection<NeonKeyValPair> properties = PsiTreeUtil.findChildrenOfType(yamlFile, NeonKeyValPair.class);
                if (!properties.isEmpty()) {
                    if (key == null) {
                        for (NeonKeyValPair property : properties)
                            result.add(property);
                    } else {
                        for (NeonKeyValPair property : properties) {
                            if (key.equals(property.getKeyText())) {
                                if (value == null)
                                    result.add(property);
                                else if (value.equals(property.getValueText()))
                                    result.add(property);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
}
