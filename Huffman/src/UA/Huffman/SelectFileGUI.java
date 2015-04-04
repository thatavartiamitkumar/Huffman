package UA.Huffman;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * This is GUI to choose and compress a file
 * 
 * @author Amit
 * 
 */
public class SelectFileGUI extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JButton openButton, compressButton;
	JTextArea textArea;
	JFileChooser fileChooser;
	String inputFile = null;

	// constructor to initialize the screen size and call action listener
	public SelectFileGUI() {

		// defining the size of text area
		textArea = new JTextArea(30, 60);
		textArea.setMargin(new Insets(5, 5, 5, 5));
		textArea.setEditable(false);
		JScrollPane logScrollPane = new JScrollPane(textArea);

		// Create a file chooser
		fileChooser = new JFileChooser();

		// open button and close button images are created
		openButton = new JButton("Open a File",
				getImageIcon("images/Open.gif"));
		compressButton = new JButton("Compress & Save",
				getImageIcon("images/zip.gif"));

		// calling the action listener
		openButton.addActionListener(this);
		compressButton.addActionListener(this);
		// Setting the UI part
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(openButton);

		JPanel compressPanel = new JPanel();
		compressPanel.add(compressButton);

		add(buttonPanel, BorderLayout.LINE_START);
		add(compressPanel, BorderLayout.LINE_END);
		add(logScrollPane, BorderLayout.CENTER);
	}

	/*
	 * Action listener which performs action when open/compress button is
	 * clicked
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent ae) {

		// when user selects open button and select file that file is read
		if (ae.getSource() == openButton) {
			int selectionValue = fileChooser.showOpenDialog(SelectFileGUI.this);

			if (selectionValue == JFileChooser.APPROVE_OPTION) {
				// get the path of selected file
				File file = fileChooser.getSelectedFile();

				// Print the name on screen
				textArea.append("Selected File: " + file.getName() + "." + "\n");

				// Using the DataInputStream read the input file
				byte[] buffer = new byte[(int) file.length()];
				DataInputStream in;
				try {
					in = new DataInputStream(new FileInputStream(file));
					in.readFully(buffer);
					inputFile = new String(buffer);
					in.close();

				} catch (FileNotFoundException e1) {
					textArea.append("File not found" + "\n");
				} catch (IOException e2) {
					e2.printStackTrace();
				}

			} else {
				textArea.append("Open command cancelled by user." + "\n");
			}

		} else
		// when user selects compress button
		if (ae.getSource() == compressButton) {

			try {

				// Create a Huffman Object
				HuffmanCode huffmanObject = new HuffmanCode();

				Map<String, Integer> frequencyTree = huffmanObject
						.constructFreqTree(inputFile);
				List<TreeNode> treeNodeList = huffmanObject
						.constructTree(frequencyTree);
				Map<String, String> letterBinaryCodeMap = huffmanObject
						.traverseTree(treeNodeList);

				String s = huffmanObject.postOrderReversalTree(treeNodeList
						.get(0));

				// Printing the values on Text area
				textArea.append("Post-Order Traversal :-" + s + "\n");
				textArea.append("Huffman Binary Codes" + "\n");

				for (Map.Entry<String, Integer> entrySet : frequencyTree
						.entrySet()) {

					String key = entrySet.getKey();
					String binaryCode = letterBinaryCodeMap.get(key);
					int lengthOfBinaryCode = binaryCode.length();

					if (key.compareTo(" ") == 0) {
						key = "space";
					} else if (key.compareTo("\n") == 0) {
						key = "\n";
					}
					textArea.append(key + "(" + lengthOfBinaryCode + ")"
							+ ":\t" + binaryCode + "\n");

				}

				String binaryString = huffmanObject.binaryString(inputFile,
						letterBinaryCodeMap);
				String asciiString = huffmanObject.convertToASCII(binaryString);
				/* System.out.println(asciiString); */

				// save file
				int returnVal1 = fileChooser.showSaveDialog(SelectFileGUI.this);
				if (returnVal1 == JFileChooser.APPROVE_OPTION) {
					File fileWrite = fileChooser.getSelectedFile();

					// This is where a real application would save the
					// file.
					textArea.append("Saved: " + fileWrite.getName() + "."
							+ "\n");
					textArea.setCaretPosition(textArea.getDocument()
							.getLength());

					FileOutputStream is = new FileOutputStream(fileWrite);
					OutputStreamWriter osw = new OutputStreamWriter(is);
					Writer bw = new BufferedWriter(osw);

					bw.write(asciiString);
					bw.close();

				} else {
					textArea.append("Save command cancelled by user." + "\n");
				}

			} catch (FileNotFoundException e1) {
				textArea.append("File not found" + "\n");
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}
	}

	/**
	 * 
	 * This method will return the image icon
	 * 
	 * @param path
	 * @return
	 */
	public ImageIcon getImageIcon(String path) {

		// get the full path of the image
		URL imgFullPath = SelectFileGUI.class.getResource(path);
		if (imgFullPath != null) {
			return new ImageIcon(imgFullPath);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	public static void main(String[] args) {

		// Create and set up the window.
		JFrame frame = new JFrame("Choose a File to Compress");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add class to the frame
		frame.add(new SelectFileGUI());

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}
}
