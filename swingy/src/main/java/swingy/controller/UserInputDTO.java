package swingy.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class UserInputDTO<T> {
    //@NotNull(message = "Input cannot be null.")
    private T input;

	@Min(value = 1, message = "Choice must be at least 1.")
    @Max(value = 2, message = "Choice must be at most 2.")
    private Integer menuChoice;

	@Min(value =1)
	@Max(value = 9)
	private Integer integerInput;

    @Size(min = 1, max = 50, message = "Input length must be between 1 and 50.")
    private String stringInput; // Only used for text validation

	public Integer getMenuChoice() {
        return menuChoice;
    }

    public void setMenuChoice(Integer menuChoice) {
        this.menuChoice = menuChoice;
    }
	public Integer getIntegerInput(){
		return integerInput;
	}

	public void setIntegerInput(Integer input){
		this.integerInput = input;
	}

    public T getInput() {
        return input;
    }

    public void setInput(T input) {
        this.input = input;
    }

    public String getStringInput() {
        return stringInput;
    }

    public void setStringInput(String stringInput) {
        this.stringInput = stringInput;
    }
}
