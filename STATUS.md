# Ocean terrain

The cliff scan now follows connected water across biome boundaries and resolves frozen floors at any build height. All nine targeted cases and the strict Java build pass. Next, trim the floor scan and verify the packaged mod in a disposable Forge world.

The deep ocean floor passed in-game review. Submerged cliff walls now use the same rock palette on stone and deepslate up to the water surface. The pass requires sideways exposure to ocean water and a solid block above. The transition across Y 0 awaits in-game review in fresh chunks.

In the two seed-0 views below, generated andesite now spans Y −7 to 6 and diorite spans Y −2 to 5. All 15,136 sampled submerged seafloor heights around the views matched the previous build. Some overlapping vanilla deposits varied with chunk order in both builds.

Continental shelves, steep drop-offs, abyssal plains and trenches in the default Overworld await human in-game review. The extra terrain shaping fades out between Y −48 and −56 to retain the underlying basin floor near lava level.

The cliff pass compiles its material rule once per chunk. Both javac and Eclipse pass with deprecations treated as errors. A headless Forge test of the packaged mod reproduced the seed-0 cliff and floor samples.

Across three seeds, all 487 sampled trench centerline columns had continuous water to the floor. Fresh Forge chunks also reproduced the previously validated seed-0 crater floor at Y −24 and its western rim at Y 31.

Use spectator mode in a fresh default world with seed `0` for the remaining visual checks.

| Feature | Teleport | What to inspect |
| --- | --- | --- |
| Andesite wall | `/tp @s 384.048 -1.891 548.735 78.6 3.2` | Follow the andesite across Y 0 at X 376, Z 555. |
| Diorite wall | `/tp @s 374.729 -1.891 530.015 47.4 5.3` | Check the diorite band at X 365, Z 540. |
| Continental shelf | `/tp @s -224 52 -312` | Head north. The floor drops from Y 46 to Y −4 over 32 blocks. |
| Abyssal plain | `/tp @s 1536 -15 -512` | Follow the Z axis. A 1,664-block section has a median floor of Y −26. |
| Trench | `/tp @s -1033 -30 -1311` | The floor lies at Y −48 along a winding trench traced for about 2,100 blocks. |
