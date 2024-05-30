import java.util.List;

public class recipe {
    private String name;
    private List<String> ingredients;
    private String instructions;

    public recipe(String name, List<String> ingredients, String instructions) {
        this.name = name;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    public String getName() {
        return name;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public String toString() {
        return "Recipe: " + name + "\nIngredients: " + ingredients + "\nInstructions: " + instructions;
    }
}
