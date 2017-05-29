package lv.kid.vermut.intellij.yaml.psi;

import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiFile;

import java.util.HashMap;

/**
 * Yaml file - just a wrapper for the whole file
 */
public interface NeonFile extends PsiFile, NavigatablePsiElement {
	/**
	 * get sections of config file
	 * (assumes first level hash-map consists of *config section*)
	 */
    HashMap<String, NeonSection> getSections();
}
