package UA.Huffman;

import java.util.List;

/**
 * Java class which represents a tree node
 * 
 * @author Amit
 * 
 */
public class TreeNode {
	private String letter;
	private Integer frequency;
	
	// this list denotes the child tree nodes
	private List<TreeNode> nodeList;
	private String binaryCode;

	public String getBinaryCode() {
		return binaryCode;
	}

	public void setBinaryCode(String binaryCode) {
		this.binaryCode = binaryCode;
	}

	public String getLetter() {
		return letter;
	}

	public void setLetter(String letter) {
		this.letter = letter;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	public List<TreeNode> getNodeList() {
		return nodeList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((binaryCode == null) ? 0 : binaryCode.hashCode());
		result = prime * result
				+ ((frequency == null) ? 0 : frequency.hashCode());
		result = prime * result + ((letter == null) ? 0 : letter.hashCode());
		result = prime * result
				+ ((nodeList == null) ? 0 : nodeList.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TreeNode other = (TreeNode) obj;
		if (binaryCode == null) {
			if (other.binaryCode != null)
				return false;
		} else if (!binaryCode.equals(other.binaryCode))
			return false;
		if (frequency == null) {
			if (other.frequency != null)
				return false;
		} else if (!frequency.equals(other.frequency))
			return false;
		if (letter == null) {
			if (other.letter != null)
				return false;
		} else if (!letter.equals(other.letter))
			return false;
		if (nodeList == null) {
			if (other.nodeList != null)
				return false;
		} else if (!nodeList.equals(other.nodeList))
			return false;
		return true;
	}

	public void setNodeList(List<TreeNode> nodeList) {
		this.nodeList = nodeList;
	}

}
