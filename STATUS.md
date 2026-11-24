# Ocean terrain

Continental shelves, steep drop-offs, abyssal plains and trenches in the default Overworld await human in-game review. The extra terrain shaping fades out between Y −48 and −56 to retain the underlying basin floor near lava level.

The build and headless Forge test passed. Across three seeds, all 487 sampled trench centerline columns had continuous water to the floor. Fresh Forge chunks also reproduced the previously validated seed-0 crater floor at Y −24 and its western rim at Y 31.

Use spectator mode in a fresh default world with seed `0` for the remaining visual checks.

| Feature | Teleport | What to inspect |
| --- | --- | --- |
| Continental shelf | `/tp @s -224 52 -312` | Head north. The floor drops from Y 46 to Y −4 over 32 blocks. |
| Abyssal plain | `/tp @s 1536 -15 -512` | Follow the Z axis. A 1,664-block section has a median floor of Y −26. |
| Trench | `/tp @s -1033 -30 -1311` | The floor lies at Y −48 along a winding trench traced for about 2,100 blocks. |
