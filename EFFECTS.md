# Inventory Effects

## [Glow Outline Effect](src/main/java/brilliant_items/api/inventory_item_effects/builtin/GlowOutlineEffect.java)

> ![Glow Outline](images/glow_outline.png)
>
> Adds a glow outline by blurring the texture and placing it in the background

Structure:

- `identifier`: glow_outline
- Arguments:
    - `color`: `ARGB`, Hex color
    - `blurRadius`: `int`, The size of the pixels sampled for producing the blur
    - `sigma`: `float`, The size of the blur, higher value means bigger blur

Example:

```json lines
{
  "identifier": "glow_outline",
  "arguments": {
    "color": "FF00F6FF",
    "blurRadius": 2,
    "sigma": 1.2
  }
}
```

## [Radial Glow Effect](src/main/java/brilliant_items/api/inventory_item_effects/builtin/RadialGlowEffect.java)

> ![Radial Glow](images/radial_glow.png)
>
> Adds a radial glow behind the item

Structure:

- `identifier`: radial_glow
- Arguments:
    - `colors`: `List<ARGB>`, A list of colors which will be cycled through
    - `duration`: `ISO-8601 String`, The amount of time it takes to switch from one color to the next

Example:

```json lines
{
  "identifier": "radial_glow",
  "arguments": {
    "colors": [
      "FF00F6FF",
      "FFFFFFFF"
    ],
    "duration": "PT1S"
  }
}
```

## [Sparkle Effect](src/main/java/brilliant_items/api/inventory_item_effects/builtin/SparkleEffect.java)

> ![Sparkle](images/sparkles.png)
>
> Occasionally spawns particles resembling sparkles

Structure:

- `identifier`: sparkles
- Arguments:
    - `color`: `ARGB`, The color of the sparkle
    - `minLifetime`: `int`, The minimum lifetime of the particle in frames
    - `maxLifetime`: `int`, The maximum lifetime of the particle in frames
    - `velocity`: `{x: float, y: float}`, The velocity of the particle
    - `amountOfSparkles`: `int`, The number of particles which should be visible at once
    - `size`: `float`, The size of the particles
    - `texture`: `Resource Location`, The particle texture as a minecraft resource location

Example:

```json lines
{
  "identifier": "sparkle",
  "arguments": {
    "color": "FF00F6FF",
    "minLifetime": 800,
    "maxLifetime": 2000,
    "amountOfSparkles": 5,
    "size": 2.5,
    "texture": "brilliant_items:textures/particles/glow.png",
    "velocity": {
      "x": 5,
      "y": 5
    }
  }
}
```

# Entity Effects

## [Glow Pillar Effect](src/main/java/brilliant_items/api/entity_item_effects/builtin/GlowPillarEffect.java)

> ![Glow Pillar](images/glow_pillar.png)
>
> Adds a glowing `pillar` that marks the location of an item

Structure:

- `identifier`: glow_pillar
- Arguments:
    - `color`: `ARGB`, The color of the beam
    - `width`: `float`, The width of the beam in blocks
    - `height`: `float`, The height of the beam in blocks

Example:

```json lines
{
  "identifier": "glow_pillar",
  "arguments": {
    "color": "6600F6FF",
    "width": 0.3,
    "height": 1.5
  }
}
```

## [Pinwheel Effect](src/main/java/brilliant_items/api/entity_item_effects/builtin/PinwheelEffect.java)

> ![Pinwheel](images/pinwheel.png)
>
> Adds a rotating glowing `pinwheel` behind the item

Structure:

- `identifier`: pinwheel
- Arguments:
    - `color`: `ARGB`, The color of the pinwheel
    - `width`: `float`, The width of the pinwheel in blocks
    - `height`: `float`, The height of the pinwheel in blocks

Example:

```json lines
{
  "identifier": "pinwheel",
  "arguments": {
    "color": "6600F6FF",
    "width": 0.75,
    "height": 0.75
  }
}
```