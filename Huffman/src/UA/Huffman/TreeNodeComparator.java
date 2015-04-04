package UA.Huffman;

import java.util.Comparator;

/**
 * Comparator to sort the Tree Node object based on frequency
 * @author Amit
 *
 */
public class TreeNodeComparator implements Comparator<TreeNode> {

	public int compare(TreeNode t1, TreeNode t2) {

		if (t1.getFrequency() < t2.getFrequency()) {
			return -1;
		} else if (t1.getFrequency() == t2.getFrequency()) {
			return 0;
		} else {
			return 1;
		}

	}

}
