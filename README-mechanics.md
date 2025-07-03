# Skript-Nexo Custom Mechanics System

This document explains how to use the custom mechanics system in Skript-Nexo, which allows you to create and interact with custom Nexo mechanics using Skript.

## Overview

The custom mechanics system allows you to:

1. Create custom mechanics with unique IDs
2. Set properties on mechanics
3. Register handlers for mechanic events
4. Check if items have specific mechanics
5. Respond to interactions with mechanics

## Configuration

The `config.yml` file includes a section for mechanic events:

```yaml
events:
  mechanic:
    interact:
      enabled: true
      cooldown: 1
```

You can enable/disable mechanic events and set cooldowns for them.

## Creating Custom Mechanics

To create a custom mechanic, use the following Skript syntax:

```
create nexo mechanic with id "mechanic_id"
```

For example:

```
create nexo mechanic with id "toasty"
```

## Setting Mechanic Properties

You can set properties on mechanics using:

```
set property "property_name" of mechanic "mechanic_id" to value
```

or

```
set mechanic "mechanic_id"'s property "property_name" to value
```

For example:

```
set property "damage" of mechanic "toasty" to 5
set mechanic "toasty"'s property "enabled" to true
```

## Getting Mechanic Properties

You can get properties from mechanics using:

```
property "property_name" of mechanic "mechanic_id"
```

or

```
mechanic "mechanic_id"'s property "property_name"
```

For example:

```
set {_damage} to property "damage" of mechanic "toasty"
set {_enabled} to mechanic "toasty"'s property "enabled"
```

## Registering Mechanic Handlers

You can register handlers for mechanic events using:

```
register handler for mechanic "mechanic_id" event "event_type":
    # Handler code here
```

For example:

```
register handler for mechanic "toasty" event "interact":
    send "You feel a warm sensation!" to player
    ignite player for 3 seconds
```

## Checking for Mechanics

You can check if an item has a specific mechanic using:

```
item has mechanic "mechanic_id"
```

or

```
item doesn't have mechanic "mechanic_id"
```

For example:

```
if player's tool has mechanic "toasty":
    send "That item is toasty!" to player
```

## Responding to Mechanic Interactions

You can respond to interactions with mechanics using:

```
on interact with mechanic "mechanic_id":
    # Event code here
```

For example:

```
on interact with mechanic "toasty":
    if player's tool is a water bucket:
        send "The water cooled down the toasty mechanic!" to player
        cancel event
```

## Example Script

See the `example-mechanic.sk` file for a complete example of how to use the custom mechanics system.

## Programmatic Usage

You can also create and use custom mechanics programmatically. See the `ToastyItem.java` class in the `me.asleepp.skriptnexo.examples` package for an example.

## Using Custom Mechanics in Nexo Configs

When you create a custom mechanic using Skript, it is registered with Nexo's MechanicsManager, which means:

1. Your custom mechanics are available to Nexo and can be referenced in Nexo configurations by their ID.
2. Properties set on custom mechanics are stored in memory and can be accessed through Skript or Java code.
3. You can use your custom mechanics in Nexo's configuration files by referencing their ID.

For example, if you create a mechanic with ID "toasty", you can reference it in Nexo's configuration files like this:

```yaml
# Example of how to reference a custom mechanic in a Nexo config
items:
  toasty_sword:
    material: DIAMOND_SWORD
    name: "&6Toasty Sword"
    lore:
      - "&eThis sword is extremely hot!"
    mechanics:
      - toasty  # Reference to your custom mechanic
```

To apply a custom mechanic to an item or block programmatically, you need to use Nexo's API:

```java
// Example of applying a custom mechanic to an item programmatically
ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
MechanicsManager.INSTANCE.applyMechanic(item, "toasty");
```

## Implementation Details

The custom mechanics system is implemented using the following classes:

- `SkriptMechanicFactory`: Creates custom mechanics
- `SkriptMechanic`: Represents a custom mechanic
- `MechanicHandler`: Handles mechanic events
- `SkriptMechanicInteractEvent`: Event for mechanic interactions
- `EvtMechanicInteractEvent`: Skript event for mechanic interactions
- `CondHasMechanic`: Condition for checking if an item has a mechanic
- `EffCreateCustomMechanic`: Effect for creating custom mechanics
- `EffSetMechanicProperty`: Effect for setting mechanic properties
- `ExprMechanicProperty`: Expression for getting mechanic properties
- `EffRegisterMechanicHandler`: Effect for registering mechanic handlers