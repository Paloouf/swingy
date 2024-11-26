package swingy;

import swingy.view.View;
import swingy.controller.GameController;
import swingy.view.CLIView;
import swingy.view.GUIView;

public class SwingyGame{
	private String saveFile;
	private String GUIOption;
	public	View displayMode;
	public GUIView guiview;
	public CLIView cliview;
	public GameController game;

	public SwingyGame(String saveFile, String GUIOption){
		this.saveFile = saveFile;
		this.GUIOption = GUIOption;
	}

	public void start(){
		this.setDisplayMode();
		//while (game.isRunning){
			game.gameloop();
		//}
	}


	public void setDisplayMode(){
		guiview = new GUIView();
		cliview = new CLIView();
		if (this.GUIOption.toLowerCase().equals("console")){
			displayMode = cliview;
			this.game = new GameController(displayMode, guiview);
			//displayMode.display("CLIView");
		} else if (this.GUIOption.toLowerCase().equals("gui")){
			displayMode = guiview;
			this.game = new GameController(displayMode, cliview);
			//displayMode.display("GUIView");
		}
	}
}