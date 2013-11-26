package pl.edu.pw.elka.pszt.game;

public class MoveTree {
	private Move root;
	private Move currentNode;
	
	
	
	
	
	
	
	public Move getRoot() {
		return root;
	}
	public void setRoot(Move root) {
		this.root = root;
		this.currentNode = this.root;
	}
	public Move getCurrentNode() {
		return currentNode;
	}
	public void setCurrentNode(Move currentNode) {
		this.currentNode = currentNode;
	}
}
