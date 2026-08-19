<div align="center">
<img src="images/icon.gif" alt="Icon">
<h1>Brilliant Items</h1>
</div>

A library for Minecraft 1.12.2 that allows you to give your items more flare. An expansion of the effects in
the [Brilliant Text](https://github.com/Karto1000/Brilliant-Text) library. Inspired by the 'loot beams' the [Loot Beams Retro](https://www.curseforge.com/minecraft/mc-mods/loot-beams-retro)
and [RGB Chat](https://www.curseforge.com/minecraft/mc-mods/rgb-chat) mods add.

## How to use

> [!important]
> There are two different "kinds" of effects. Firstly, there's the `Entity Effects` which apply to a dropped item
> entity. Then, there's also the `Inventory Effects` which apply to an item when it is in any inventory. These two
> effects can be used at the same time without issue.

### JSON configuration

> [!important]
> A list of effects can be found in the [Effects](EFFECTS.md) file.

The mod creates a `brilliant_items_bindings.json` file in the `config` folder of your minecraft instance. The file has
the following structure:

```json lines
{
  "mappings": {
    // The id of the item that should have an effect
    "minecraft:diamond_sword": {
      // A list of effects that are applied to the item when it is in an inventory
      "inventoryEffects": [
        // A glow outline effect
        {
          "identifier": "glow_outline",
          "arguments": {
            "color": "FF00F6FF"
          }
        },
        // An effect that occasionally spawns particles above the item
        {
          "identifier": "sparkle",
          "arguments": {
            "color": "6600F6FF"
          }
        }
      ],
      // A list of effects that are applied to the item when it is dropped in the world
      "entityEffects": [
        // A 'glow pinwheel' that is rendered behind the physical item
        {
          "identifier": "pinwheel",
          "arguments": {
            "color": "6600F6FF"
          }
        },
        // A 'glow pillar' that is rendered at the item position to make it stand out
        {
          "identifier": "glow_pillar",
          "arguments": {
            "color": "6600F6FF"
          }
        }
      ]
    },
  }
}
```

Each effect object must specify an `identifier` to indicate which effect should be used. Additionally, you can specify a
list of arguments to customize some behavior of each effect.

### Mod Integration

If you are developing a mod and want your custom item to have an effect, you can implement the `IHasEffects`
interface.

```java
/// An Interface that can be implemented for any class that implements the Item class
@SideOnly(Side.CLIENT)
public interface IHasEffects {
    /// Get a list of effects to be applied to the given stack when it is present in the world.
    /// This is run once when the ItemStack is constructed
    ///
    /// @param stack The ItemStack
    /// @return A list of effects
    @Nonnull
    default NonNullList<IEntityItemEffect> getEntityEffects(@Nonnull ItemStack stack) {
        return NonNullList.create();
    }

    /// Get a list of effects to be applied to the given stack when it is in an inventory
    /// This is run once when the ItemStack is constructed
    ///
    /// @param stack The ItemStack
    /// @return A list of effects
    @Nonnull
    default NonNullList<IInventoryItemEffect> getInventoryEffects(@Nonnull ItemStack stack) {
        return NonNullList.create();
    }
}
```

This interface has two methods which you can override. Each Method returns a `NonNullList` of effects.

> This is what an implementation would look like
> ```java
> public class CustomItem extends Item implements IHasEffects {
>     /// Get a list of effects to be applied to the given stack when it is in an inventory
>     /// This is run once when the ItemStack is constructed
>     ///
>     /// @param stack The ItemStack
>     /// @return A list of effects
>     @Nonnull
>     @Override
>     public NonNullList<IInventoryItemEffect> getInventoryEffects(@Nonnull ItemStack stack) {
>         NonNullList<IInventoryItemEffect> effects = NonNullList.create();
>         // We want to add a glow outline effect here
>         effects.add(new GlowOutlineEffect(new GlowOutlineEffect.Args()));
>         return effects;
>     }
> }
> ```

## Custom Effects

### Inventory Effects

To implement a custom inventory effect, you need to create a new class and implement the
`IInventoryItemEffect` or `IInventoryItemShaderEffect` interface.

From there you can define your custom rendering logic in the `renderPass` method. Examples can be found in
the [Builtin Package](src/main/java/brilliant_items/api/inventory_item_effects/builtin)

### Entity Effects

To implement a custom entity effect, you need to create a new class and implement the
`IEntityItemEffect` interface.

From there you can define your custom rendering logic in the `renderPass` method. Examples can be found in
the [Builtin Package](src/main/java/brilliant_items/api/entity_item_effects/builtin)

### Reference your Effect in the Config

If you want your effect to be referencable, you need to annotate your class with the `ReferencableEffect` annotation.

Example:

```java

@ReferencableEffect(identifier = "my_effect", argumentsClass = MyEffect.Args.class)
public class MyEffect implements IInventoryItemEffect {
    // ...

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Args {
        @Builder.Default
        @JsonAdapter(HexColorAdapter.class)
        public int color = 0xFFFFFFFF;
    }

    // ...
}
```

There you need to specify the identifier that will be used and the Arguments class. The `Args` class should be
deserializable from JSON.

You then need to register it from a `preInit` function in your `ClientProxy` with the
`BrilliantItemsAPI.registerForJson` function.

Example:

```java
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit() {
        // ...
        BrilliantItemsAPI.registerForJSON(MyEffect.class);
        // ...
    }
}
```

You can then use your effect in the JSON using the specified identifier