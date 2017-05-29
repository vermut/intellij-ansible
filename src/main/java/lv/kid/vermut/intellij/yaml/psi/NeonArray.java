package lv.kid.vermut.intellij.yaml.psi;

import java.util.HashMap;
import java.util.List;

/**
 * php-like array
 *
 * it can be a list, hash-map or a mix of those two
 */
public interface NeonArray extends NeonValue {
	/**
	 * Is it a list-like array? i.e. keys are only numeric
	 */
    boolean isList();

	/**
	 * Is it hash-map-like array? I.e. keys are not-numeric
	 */
    boolean isHashMap();

	/**
	 * Get all item values (ignore keys)
	 */
    List<NeonValue> getValues();

	/**
	 * Get keys as nodes
	 */
    List<NeonKey> getKeys();

	/**
	 * Get all values as a hash-map
	 */
    HashMap<String, NeonValue> getMap();

}
