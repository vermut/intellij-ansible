package lv.kid.vermut.intellij.yaml.psi;

import com.intellij.psi.PsiNamedElement;

/**
 * Key-value pair, part of NeonHash
 */
public interface NeonKeyValPair extends PsiNamedElement {
	// key
    NeonKey getKey();
	String getKeyText();

	// value
    NeonValue getValue();
	String getValueText();
}
