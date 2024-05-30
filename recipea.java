import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class recipea {
    private List<recipe> recipes;
    public recipea() {
        recipes = new ArrayList<>();
        initializeRecipes();
    }
    public void initializeRecipes() {
        recipe a = new recipe("竹筍炒肉",
                Arrays.asList("竹筍","肉","油","鹽"),
                "做法:熱鍋燒油，下肉，下竹筍，炒，加鹽，適合家庭的一小菜，小孩應該挺愛吃");
        recipes.add(a);

        a = new recipe("雞湯",
                Arrays.asList("雞肉","水","鹽"),
                "做法:鍋裡加水，加雞肉，加鹽，蓋蓋子煮一段時間，雞湯來嘍");
        recipes.add(a);

        a = new recipe("麵包",
                Arrays.asList("麵粉", "水", "雞蛋"),
                "做法:麵粉+水+雞蛋攪一攪，麵團拿去烤");
        recipes.add(a);
    }
    public recipe RandomRecipe() {
        if (recipes.isEmpty()) {
            return null; // 或者返回适当的默认值
        }
        Random random = new Random();
        int index = random.nextInt(recipes.size());
        return recipes.get(index);
    }
}
