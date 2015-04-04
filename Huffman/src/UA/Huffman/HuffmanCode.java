package UA.Huffman;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Core class where the actual logic of Huffman algorithm resides
 * @author Amit
 *
 */
public class HuffmanCode {

	/**
	 * This method will get the file location and reads the file into a String
	 * 
	 * @return
	 */
	public String inputFile() {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("Enter the file location");

		String fileLocation = null;
		String input = null;

		try {
			fileLocation = br.readLine();
			File f = new File(fileLocation);
			byte[] buffer = new byte[(int) f.length()];
			DataInputStream in = new DataInputStream(new FileInputStream(f));
			in.readFully(buffer);
			input = new String(buffer);
			in.close();

		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			e.printStackTrace();
		}

		return input;

	}

	/**
	 * This method will save the compressed to specified location.
	 * 
	 * @param fileToWrite
	 */
	public void writeFile(String fileToWrite) {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("Enter the file location");

		String fileLocation = null;

		try {
			fileLocation = br.readLine();
			FileOutputStream is = new FileOutputStream(fileLocation);
			OutputStreamWriter osw = new OutputStreamWriter(is);
			Writer bw = new BufferedWriter(osw);

			bw.write(fileToWrite);
			bw.close();
			br.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * This API will construct a Char-frequency map
	 * 
	 * @param input
	 * @return
	 */
	public Map<String, Integer> constructFreqTree(String input) {

		Map<String, Integer> frequencyTree = new TreeMap<String, Integer>();

		if (input != null) {
			// construct the free MAP
			for (char iterator : input.toCharArray()) {

				String sIt = String.valueOf(iterator);

				if (frequencyTree != null) {
					if (!frequencyTree.containsKey(sIt)) {
						frequencyTree.put(sIt, 1);
					} else {
						int value = frequencyTree.get(sIt);
						value += 1;
						frequencyTree.put(sIt, value);
					}
				}

			}
		}

		return frequencyTree;

	}

	/**
	 * Tree structure with frequency and char is created in this API
	 * @param frequencyTree
	 * @return
	 */
	public List<TreeNode> constructTree(Map<String, Integer> frequencyTree) {

		// create a list of treeNode object
		List<TreeNode> treeNodeList = new ArrayList<TreeNode>();

		// define comparator
		TreeNodeComparator comparator = new TreeNodeComparator();

		if (frequencyTree != null && !frequencyTree.isEmpty()) {

			for (Map.Entry<String, Integer> entrySet : frequencyTree.entrySet()) {

				TreeNode treeNodeObject = new TreeNode();

				String key = entrySet.getKey();
				Integer value = entrySet.getValue();

				treeNodeObject.setLetter(key);
				treeNodeObject.setFrequency(value);

				treeNodeList.add(treeNodeObject);

			}

			// sort the list with inc frequency
			Collections.sort(treeNodeList, comparator);

			// construction of tree

			while (treeNodeList.size() > 1) {

				TreeNode tempNode = new TreeNode();
				List<TreeNode> nodeList = new ArrayList<TreeNode>();
				int freq = 0;

				for (int i = 0; !treeNodeList.isEmpty() && i < 2; i++) {
					freq += treeNodeList.get(i).getFrequency();
					tempNode.setFrequency(freq);
					treeNodeList.get(i).setBinaryCode(String.valueOf(i));
					nodeList.add(treeNodeList.get(i));
					tempNode.setNodeList(nodeList);

				}
				treeNodeList.add(tempNode);

				// remove the added nodes
				treeNodeList.removeAll(tempNode.getNodeList());

				Collections.sort(treeNodeList, comparator);
			}

		}

		return treeNodeList;
	}

	/**
	 * This method will traverse the tree to get the binary codes for each character
	 * @param treeNodeList
	 * @return
	 */
	public Map<String, String> traverseTree(List<TreeNode> treeNodeList) {

		Map<String, String> letterBinaryCodeMap = new HashMap<String, String>();

		if (treeNodeList != null && !treeNodeList.isEmpty()) {
			TreeNode singleTreeNode = treeNodeList.get(0);
			if (singleTreeNode != null) {
				if (singleTreeNode.getLetter() == null
						&& singleTreeNode.getNodeList() != null
						&& !singleTreeNode.getNodeList().isEmpty()) {
					// Tree has multiple nodes
					List<TreeNode> recurssiveTreeNodeList = new ArrayList<TreeNode>();

					recurssiveTreeNodeList = singleTreeNode.getNodeList();

					while (recurssiveTreeNodeList != null
							&& !recurssiveTreeNodeList.isEmpty()) {

						List<TreeNode> subTreeNodeList = new ArrayList<TreeNode>();

						for (TreeNode nodeIt : recurssiveTreeNodeList) {

							StringBuffer sb = new StringBuffer();

							if (nodeIt.getLetter() != null) {
								letterBinaryCodeMap.put(nodeIt.getLetter(), sb
										.append(nodeIt.getBinaryCode())
										.toString());
							} else {

								subTreeNodeList.addAll(nodeIt.getNodeList());
								for (TreeNode nodeIt1 : nodeIt.getNodeList()) {
									StringBuffer codeBuffer = new StringBuffer();
									String individualBinaryCode = nodeIt1
											.getBinaryCode();
									String commonBinaryCode = nodeIt
											.getBinaryCode();
									codeBuffer.append(commonBinaryCode).append(
											individualBinaryCode);
									String binaryCodeString = codeBuffer
											.toString();

									nodeIt1.setBinaryCode(binaryCodeString);
								}
							}
						}
						recurssiveTreeNodeList = subTreeNodeList;

					}

				} else if (singleTreeNode.getLetter() != null) {
					// tree has only one node
					letterBinaryCodeMap.put(singleTreeNode.getLetter(), "1");
				}
			}
		}

		return letterBinaryCodeMap;
	}

	/**
	 * This method will print out the values in standard format
	 * @param frequencyTree
	 * @param letterBinaryCodeMap
	 */
	public void printStandardOutput(Map<String, Integer> frequencyTree,
			Map<String, String> letterBinaryCodeMap) {

		System.out.println("----------------Binary Code value-----------");
		System.out.println("Letter(Frequency)" + "BinaryCode");

		for (Map.Entry<String, Integer> entrySet : frequencyTree.entrySet()) {

			String key = entrySet.getKey();
			String binaryCode = letterBinaryCodeMap.get(key);
			int lengthOfBinaryCode = binaryCode.length();
			System.out.println(key + "(" + lengthOfBinaryCode + ")" + "\t\t"
					+ binaryCode);

		}

	}

	/**
	 * This is a recursive method which traverses the tree.
	 * @param singleTreeNode
	 * @return
	 */
	public String postOrderReversalTree(TreeNode singleTreeNode) {

		StringBuffer sb = new StringBuffer();

		if (singleTreeNode.getLetter() != null) {
			sb.append(singleTreeNode.getLetter()).append(
					singleTreeNode.getFrequency());
			return sb.toString();
		} else {
			if (singleTreeNode.getNodeList() != null
					&& !singleTreeNode.getNodeList().isEmpty()) {
				String first = postOrderReversalTree(singleTreeNode
						.getNodeList().get(0));
				String second = postOrderReversalTree(singleTreeNode
						.getNodeList().get(1));
				sb.append(first).append(second).append("@")
						.append(singleTreeNode.getFrequency());
				return sb.toString();
			} else {
				return null;
			}
		}

	}

	public String binaryString(String input,
			Map<String, String> letterBinaryCodeMap) {

		StringBuffer binaryStringBuffer = new StringBuffer();

		if (input != null && letterBinaryCodeMap != null
				&& !letterBinaryCodeMap.isEmpty()) {
			for (char iterator : input.toCharArray()) {
				binaryStringBuffer.append(letterBinaryCodeMap.get(String
						.valueOf(iterator)));

			}
		}

		return binaryStringBuffer.toString();
	}

	public String convertToASCII(String binaryString) {

		StringBuffer asciiBuffer = new StringBuffer();

		if (binaryString != null) {

			int start = 0;
			int stop = 0;
			double lengthOfString = binaryString.length();
			double numberOfIterations = Math.ceil(lengthOfString / 8);

			for (int i = 0; i < numberOfIterations; i++) {
				stop += 8;
				String subString;
				if (stop < lengthOfString) {
					subString = binaryString.substring(start, stop);
				} else {
					subString = binaryString.substring(start,
							(int) lengthOfString);
				}
				int decimalValue = binaryToDecimal(subString);
				char asciiValue = (char) decimalValue;
				asciiBuffer.append(asciiValue);
				start += 8;
			}
		}

		return asciiBuffer.toString();
	}

	/**
	 * This API is used to convert binary value to decimal.
	 * 
	 * @param binaryValue
	 * @return
	 */
	public int binaryToDecimal(String binaryValue) {

		int lengthOfBinaryNumber = binaryValue.length();

		// initialzing it to 0
		BigInteger binaryIntVal = new BigInteger("0");

		// base integer
		BigInteger base = new BigInteger("2");

		for (int i = 0; i <= (lengthOfBinaryNumber - 1); i++) {

			if (binaryValue.charAt(i) == '1') {

				int power = lengthOfBinaryNumber - 1 - i;

				binaryIntVal = binaryIntVal.add(base.pow(power));
			}

		}

		return binaryIntVal.intValue();
	}

	public static void main(String[] args) {

		HuffmanCode huffmanObject = new HuffmanCode();
		String inputFile = huffmanObject.inputFile();
		Map<String, Integer> frequencyTree = huffmanObject
				.constructFreqTree(inputFile);
		List<TreeNode> treeNodeList = huffmanObject
				.constructTree(frequencyTree);
		Map<String, String> letterBinaryCodeMap = huffmanObject
				.traverseTree(treeNodeList);

		String s = huffmanObject.postOrderReversalTree(treeNodeList.get(0));
		System.out.println("Post order traversal :- " + s);

		huffmanObject.printStandardOutput(frequencyTree, letterBinaryCodeMap);
		String binaryString = huffmanObject.binaryString(inputFile,
				letterBinaryCodeMap);
		String asciiString = huffmanObject.convertToASCII(binaryString);
		/* System.out.println(asciiString); */
		huffmanObject.writeFile(asciiString);

	}
}
