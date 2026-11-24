# Ocean generation

In-game review is complete. Ocean monument and shipwreck placement are approved, along with the regional ocean terrain, shelves, trenches, outcrops, beaches, frozen cliffs, and cliff rock across Y 0.

The build passes with deprecation warnings treated as errors. Five standard shipwrecks and five oversized replacements matched their approved placement bounds and passed Forge save and reload checks.

## Code review

The frozen cliff floor scan now passes through above-water air in ice cover. An archived terrain sample contained 56 affected columns. The offline assembly passed. Inspect a submerged cliff below an iceberg with an above-water air pocket in a disposable world.

Next, check saved structure bounds against the template used on reload and review the terrain generation lifecycle.
