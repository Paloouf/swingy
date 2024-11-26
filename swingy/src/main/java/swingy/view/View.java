package swingy.view;

import swingy.model.Map;
import swingy.view.console.MenuAction;
import swingy.model.Hero;

public abstract class View {
	private Map map;

	public void display(String message){
		System.out.println(message);
	}

	public abstract String getMoveInput();
	public abstract void MenuDisplay();
	public abstract MenuAction getMenuChoice();
	public abstract Hero createHero();
	public abstract Hero selectSave();
    public abstract void MapDisplay(Map map, Hero hero);
    public abstract void CombatDisplay();
}
