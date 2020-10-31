package AiHex.gameMechanics;

public class Move {
	private int colour, x, y;

	public Move(int colour, int x, int y) {
		this.colour = colour;
		this.x = x;
		this.y = y;
	}

	public void setX(int temp) {
		this.x = temp;
	}

	public void setY(int temp) {
		this.y = temp;
	}

	public int getColour() {
		return this.colour;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}
}
