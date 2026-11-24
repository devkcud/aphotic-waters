# Ocean terrain

The deep ocean floor passed in-game review. The wall treatment awaits visual review in fresh chunks. It extends the existing rock palette onto covered deepslate blocks exposed sideways to ocean water, including up to eight blocks beneath a ledge.

On seed `0`, deepslate on steep faces bordering open water fell from 46% to 9% at the west-facing site and from 60% to 15% at the south-facing site. All 6,276 sampled submerged floor columns below Y 0 retained their height and top block. Reversing chunk generation order reproduced all 3,041 wall replacements at these sites.

Continental shelves, steep drop-offs, abyssal plains and trenches in the default Overworld await human in-game review. The extra terrain shaping fades out between Y −48 and −56 to retain the underlying basin floor near lava level.

The build and headless Forge test passed. Across three seeds, all 487 sampled trench centerline columns had continuous water to the floor. Fresh Forge chunks also reproduced the previously validated seed-0 crater floor at Y −24 and its western rim at Y 31.

Use spectator mode in a fresh default world with seed `0` for the remaining visual checks.

| Feature | Teleport | What to inspect |
| --- | --- | --- |
| West-facing view | `/tp @s 374.859 -22.422 438.837 89.7 -4.5` | Inspect the tall wall behind the spire. |
| South-facing view | `/tp @s 488.878 -27.222 544.713 2.2 -0.8` | Follow the granite patches down the cliff face. |
| Continental shelf | `/tp @s -224 52 -312` | Head north. The floor drops from Y 46 to Y −4 over 32 blocks. |
| Abyssal plain | `/tp @s 1536 -15 -512` | Follow the Z axis. A 1,664-block section has a median floor of Y −26. |
| Trench | `/tp @s -1033 -30 -1311` | The floor lies at Y −48 along a winding trench traced for about 2,100 blocks. |
