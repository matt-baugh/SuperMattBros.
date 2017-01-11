package SMB.tools;

import java.io.Serializable;

public class EntityInput implements Serializable{
	public boolean UpKeyDown = false, LeftKeyDown = false, RightKeyDown = false,
			DownKeyDown = false, LAKeyDown = false, HAKeyDown = false,
			GrKeyDown = false;

	public boolean isUpKeyDown() {
		return UpKeyDown;
	}

	public void setUpKeyDown(boolean upKeyDown) {
		UpKeyDown = upKeyDown;
	}

	public boolean isLeftKeyDown() {
		return LeftKeyDown;
	}

	public void setLeftKeyDown(boolean leftKeyDown) {
		LeftKeyDown = leftKeyDown;
	}

	public boolean isRightKeyDown() {
		return RightKeyDown;
	}

	public void setRightKeyDown(boolean rightKeyDown) {
		RightKeyDown = rightKeyDown;
	}

	public boolean isDownKeyDown() {
		return DownKeyDown;
	}

	public void setDownKeyDown(boolean downKeyDown) {
		DownKeyDown = downKeyDown;
	}

	public boolean isLAKeyDown() {
		return LAKeyDown;
	}

	public void setLAKeyDown(boolean lAKeyDown) {
		LAKeyDown = lAKeyDown;
	}

	public boolean isHAKeyDown() {
		return HAKeyDown;
	}

	public void setHAKeyDown(boolean hAKeyDown) {
		HAKeyDown = hAKeyDown;
	}

	public boolean isGrKeyDown() {
		return GrKeyDown;
	}

	public void setGrKeyDown(boolean grKeyDown) {
		GrKeyDown = grKeyDown;
	}
	
	
}
