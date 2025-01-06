package org.sandbytes.instaCraft.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CraftingUtils {

    public static boolean canBeCrafted(Material material) {
        List<Recipe> recipes = Bukkit.getRecipesFor(new ItemStack(material));
        return !recipes.isEmpty() && recipes.get(0) instanceof ShapedRecipe;
    }

    public static boolean hasRequiredMaterials(Player player, Material material) {
        List<Recipe> recipes = Bukkit.getRecipesFor(new ItemStack(material));

        Recipe recipe = recipes.get(0);
        if (recipe instanceof ShapedRecipe) {
            ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;
            Map<Material, Integer> requiredMaterials = getRecipeMaterials(shapedRecipe);
            return hasMaterialsInInventory(player.getInventory(), requiredMaterials);
        }

        return false;
    }

    private static Map<Material, Integer> getRecipeMaterials(ShapedRecipe recipe) {
        Map<Material, Integer> materials = new HashMap<>();
        recipe.getIngredientMap().forEach((key, itemStack) -> {
            if (itemStack != null) {
                Material material = itemStack.getType();
                materials.put(material, materials.getOrDefault(material, 0) + 1);
            }
        });
        return materials;
    }

    private static boolean hasMaterialsInInventory(Inventory inventory, Map<Material, Integer> requiredMaterials) {
        Map<Material, Integer> playerMaterials = new HashMap<>();

        for (ItemStack item : inventory.getContents()) {
            if (item != null) {
                Material material = item.getType();
                playerMaterials.put(material, playerMaterials.getOrDefault(material, 0) + item.getAmount());
            }
        }

        for (Map.Entry<Material, Integer> entry : requiredMaterials.entrySet()) {
            Material material = entry.getKey();
            int requiredAmount = entry.getValue();
            if (playerMaterials.getOrDefault(material, 0) < requiredAmount) {
                return false;
            }
        }

        return true;
    }

    public static void craftItem(Player player, Material result) {
        List<Recipe> recipes = Bukkit.getRecipesFor(new ItemStack(result));
        if (recipes.isEmpty()) return;

        Recipe recipe = recipes.get(0);
        if (recipe instanceof ShapedRecipe) {
            ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;
            Map<Material, Integer> requiredMaterials = getRecipeMaterials(shapedRecipe);

            requiredMaterials.forEach((material, amount) -> {
                for (int i = 0; i < amount; i++) {
                    player.getInventory().removeItem(new ItemStack(material, 1));
                }
            });

            player.getInventory().addItem(new ItemStack(result));
        }
    }

}
