![Not Just Spoiled title in minecraft logo style](https://cdn.modrinth.com/data/cached_images/afae8860c3fa49bae32ead1f3897ee1330167662.png)
# Description

Not Just Spoiled is a modern mod that adds a simple but immersive and realistic food spoilage system. It makes survival more dynamic by encouraging players to carefully manage their food supplies without becoming overly difficult. With extensive configuration options, JSON-based customization and performance, the mod is designed to fit both personal worlds and large modpacks.

---

# Features
### Food Status
Food status system is the core of Not Just Spoiled. While other mods simply convert food into rotten flesh or another item, this mod adds 4 different spoilage statuses instead: fresh, stale, half-spoiled and spoiled. Food does not instantly become unusable — it gradually loses its quality over time, with each status having its own hunger and saturation values. This allows food to become less effective before becoming completely spoiled, adding more depth to survival without making food management overly punishing. Spoiled food may also apply negative effects.

### Food Category
Every food item belongs to one of **34** logically divided food categories, each with its own spoilage time. This allows different types of food to spoil at different rates, making food management a more important part of gameplay. For now, categories affect only spoilage duration, but additional mechanics based on food categories are planned for future updates.

### Food Environment
Food in this mod does not spoil after a fixed amount of time. Instead, spoilage speed depends on the environment the food is currently in. There are 4 environments: inventory, storage, open air and while cooking. The worse the environment, the faster the food spoils!

### Food Spoilage in Loot Tables
Food in loot tables (like chests, archaeology, etc.) can have different spoilage statuses depending on where it is found. Instead of always generating fresh food, loot tables can define the expected condition of the food they generate using built-in loot modifiers. For example, food in buried treasures, shipwrecks, mineshafts or other abandoned structures generates already spoiled, while food in villages, mansions, bastions, etc. will be fresh or stale. This mechanic makes loot feel more dynamic and logical, as the spoilage status of found food depends on the place where it was discovered.

Currently, there are 4 loot modifiers:
- **Fresh**. Food always generates with fresh status.
- **Fresh or stale**. Food generates with fresh or stale status.
- **Spoiled**. Food always generates with spoiled status.
- **Random**. Food generates with a random spoilage status.

### Food Crafting
The way food spoilage affects crafting is fully configurable. There are five food crafting modes, each with its own rules and logic, letting you decide how much food spoilage affects your survival experience.
- **Average**. Food can always be crafted. The spoilage time of the result is calculated as the weighted average of all ingredients.
- **Worst status**. Food can always be crafted. The result inherits the spoilage level of the worst ingredient. For example, if all ingredients are fresh but one is half-spoiled, the crafted food will also be half-spoiled.
- **Same status**. You can craft food only if all ingredients have the same food status.
- **Fresh only**. Food can be crafted only when using fresh ingredients.
- **Fresh or stale**. Food can be crafted only when using fresh or stale ingredients.

### Food Stacking
Unlike many similar mods, Not Just Spoiled keeps vanilla item stacking behavior without any workarounds or artificial limitations. Food can be stacked normally **as long as all items have the same spoilage status**. When food stacks are merged, the final spoilage time is calculated correctly using a weighted arithmetic mean, ensuring that no spoilage data is lost.

---

# Configuration
Almost every aspect of the food spoilage system can be customized through config or JSON files:
- hunger and saturation values of each food status (JSON);
- effects, their duration, amplifier and applying chance for each status (JSON);
- spoilage time of each food category (JSON);
- food crafting mode (config);
- spoilage speed in different environments (config);
- various tooltip information (config);
- food slot overlay (config);
- initial food spoilage status in different loot tables (JSON);
- spoilage status chances for loot generation (config);

---

# Performance
The mod is built around a smart lazy-update system instead of redundant ticking of players, item and block entities, meaning that food is updated in only0 3 scenarios:
- when its environment changes;
- when a player eats it;
- when it merges with other food stacks.

This approach avoids unnecessary calculations and keeps the performance impact minimal, even with large amounts of food items in the world.

---

# Compatibility
Not Just Spoiled is not automatically compatible with other mods. However, compatibility can be easily added via JSON files, while some mods may require additional in-code support.

Additional compatibility is required for mods that add:
- food items or food ingredients (JSON);
- loot tables (e.g. chests, archaeology, etc.) (JSON);
- advanced blocks with custom inventories, such as backpacks from Sophisticated Backpacks or cooking pots from Farmer's Delight (requires in-code support).

### Compatible with:
- [Alex's Caves](https://modrinth.com/mod/alexs-caves)
- [Alex's Delight](https://www.curseforge.com/minecraft/mc-mods/alexs-delight)
- [Alex's Mobs](https://modrinth.com/mod/alexs-mobs)
- [Deeper and Darker](https://modrinth.com/mod/deeperdarker)
- [Dungeons and Taverns](https://modrinth.com/mod/dungeons-and-taverns)
- [Dungeons Plus](https://modrinth.com/mod/dungeons-plus)
- [End's Delight](https://modrinth.com/mod/ends-delight)
- [Farmer's Delight](https://modrinth.com/mod/farmers-delight)
- [Genetic Animals](https://modrinth.com/mod/genetic-animals)
- [Ocean's Delight](https://modrinth.com/mod/oceans-delight)
- [Ribbits](https://modrinth.com/mod/ribbits)
- [Simple Delights Recooked](https://www.curseforge.com/minecraft/mc-mods/simple-delights-recooked)
- [Simple Farming](https://www.curseforge.com/minecraft/mc-mods/simple-farming)
- [Terralith](https://modrinth.com/mod/terralith)
- [Tough As Nails](https://modrinth.com/mod/tough-as-nails)
- [YUNG's Better Desert Temples](https://modrinth.com/mod/yungs-better-desert-temples)
- [YUNG's Better Dungeons](https://modrinth.com/mod/yungs-better-dungeons)
- [YUNG's Better Jungle Temples](https://modrinth.com/mod/yungs-better-jungle-temples)
- [YUNG's Better Nether Fortresses](https://modrinth.com/mod/yungs-better-nether-fortresses)
- [YUNG's Better Ocean Monuments](https://modrinth.com/mod/yungs-better-ocean-monuments)
- [YUNG's Better Strongholds](https://modrinth.com/mod/yungs-better-strongholds)
- [YUNG's Better Witch Huts](https://modrinth.com/mod/yungs-better-witch-huts)
- [YUNG's Cave Biomes](https://modrinth.com/mod/yungs-cave-biomes)
- [YUNG's Extras](https://modrinth.com/mod/yungs-extras)

### Special integration with:
- [AppleSkin](https://modrinth.com/mod/appleskin) (dynamically updates hunger and saturation values in the tooltip depending on the food's current status)
- [Tough As Nails](https://modrinth.com/mod/tough-as-nails) (food spoilage also works with TAN's thirst system)

You can submit a compatibility request on the [mod's issue tracker](https://github.com/TheRealVoin/NotJustSpoiled/issues).