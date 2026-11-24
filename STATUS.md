# Ocean generation

In-game review is complete. Ocean monument and shipwreck placement are approved, along with the regional ocean terrain, shelves, trenches, outcrops, beaches, frozen cliffs, and cliff rock across Y 0.

The build passes with deprecation warnings treated as errors. Five standard shipwrecks and five oversized replacements matched their approved placement bounds and passed Forge save and reload checks.

## Review changes awaiting in-game validation

The frozen cliff scan now passes through above-water air in ice cover. An archived terrain sample contained 56 affected columns. Inspect the submerged cliff below a hollow iceberg in a disposable world.

Structure reload now checks the saved elevation against the piece and enforces the generation size limit. All 18 marked starts in two archived saves matched the new checks. The four beached controls had no marker.

In a disposable world, reload a fitted monument and submerged wreck, then check fresh neighboring chunks after replacing a wreck template with one wider than 96 blocks. The wreck should keep its saved origin and the mod should skip seabed shaping for the oversized replacement.

Placement also checks the configured sea level before subtracting structure height. With a sea level of `-2147483648`, a disposable custom Overworld should skip seafloor fitting.

Offline assembly passed with deprecations treated as errors. All four mixins applied in the Forge audit. Full javac lint reported unclaimed runtime annotations, and Mixin reported Java 25 bytecode above its declared Java 21 compatibility level.
