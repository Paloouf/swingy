package swingy.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class UserInputDTO<T> {
	@Min(value =1)
	@Max(value = 9)
	private Integer integerInput;

    @Size(min = 1, max = 50, message = "Input length must be between 1 and 50.")
    private String stringInput; // Only used for text validation

	public Integer getIntegerInput(){
		return integerInput;
	}

	public void setIntegerInput(Integer input){
		this.integerInput = input;
	}

    public String getStringInput() {
        return stringInput;
    }

    public void setStringInput(String stringInput) {
        this.stringInput = stringInput;
    }
}
