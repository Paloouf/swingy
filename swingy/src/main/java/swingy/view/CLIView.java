package swingy.view;

import java.util.Scanner;

import swingy.controller.InputManager;
import swingy.view.console.ConsoleMap;
import swingy.view.console.ConsoleMenu;
import swingy.view.console.HeroMenu;
import swingy.view.console.MenuAction;
import swingy.model.Hero;
import swingy.model.HeroClass;
import swingy.model.Map;

public class CLIView extends View{
	private ConsoleMenu console;
	private ConsoleMap consoleMap;
	private HeroMenu heroMenu;

	public CLIView(){
		this.console = new ConsoleMenu();
		this.consoleMap = new ConsoleMap();
		this.heroMenu = new HeroMenu();
	}

	@Override
	public void MenuDisplay(){
		InputManager.setContext(console);
		console.menu();
		InputManager.startInputLoop();
	}

	@Override
	public MenuAction getMenuChoice(){
		return console.getLastAction();
	}

	@Override
	public void MapDisplay(Map map, Hero hero){
		consoleMap.displayMap(map, hero);
	}

	public String getMoveInput() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Move (w/a/s/d): ");
		return scanner.nextLine().toLowerCase(); // THIS SHIT NEEDS FIXING
	}

	@Override
	public Hero createHero(){
		InputManager.setContext(heroMenu);
		InputManager.startInputLoop();
		return heroMenu.createHero();
	}
	
	@Override
	public Hero selectSave(){
		return heroMenu.selectSave();
	}

	@Override
	public void CombatDisplay(){}

}
