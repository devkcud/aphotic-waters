# Ocean generation

In-game review is complete. Ocean monument and shipwreck placement are approved, along with the regional ocean terrain, shelves, trenches, outcrops, beaches, frozen cliffs, and cliff rock across Y 0.

The build passes with deprecation warnings treated as errors. Five standard shipwrecks and five oversized replacements matched their approved placement bounds and passed Forge save and reload checks.

## Code review

The frozen cliff floor scan now passes through above-water air in ice cover. An archived terrain sample contained 56 affected columns. The offline assembly passed. Inspect a submerged cliff below an iceberg with an above-water air pocket in a disposable world.

Structure reload now checks the saved elevation against the piece and enforces the generation size limit. All 18 marked starts in two archived saves matched the new checks. The four beached controls had no marker. Offline assembly and the four-target Forge mixin audit passed.

In a disposable world, reload a fitted monument and submerged wreck, then check fresh neighboring chunks after replacing a wreck template with one wider than 96 blocks. The wreck should keep its saved origin and the mod should skip seabed shaping for the oversized replacement.
