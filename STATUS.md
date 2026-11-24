# Ocean generation

In-game review is complete. Ocean monument and shipwreck placement are approved, along with the regional ocean terrain, shelves, trenches, outcrops, beaches, frozen cliffs, and cliff rock across Y 0.

The build passes with deprecation warnings treated as errors. Five standard shipwrecks and five oversized replacements matched their approved placement bounds and passed Forge save and reload checks.

## Review validation

Manual checks passed for submerged cliffs beneath hollow icebergs, structure reload and oversized wreck replacement, and seafloor placement at the minimum configurable sea level.

Offline assembly passed with deprecations treated as errors. All four mixins applied in the Forge audit. Full javac lint reported unclaimed runtime annotations, and Mixin reported Java 25 bytecode above its declared Java 21 compatibility level.
