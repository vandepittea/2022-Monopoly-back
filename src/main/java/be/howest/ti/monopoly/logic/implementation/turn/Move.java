package be.howest.ti.monopoly.logic.implementation.turn;

public class Move {
    private String title;
    private String description;

    public Move(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
