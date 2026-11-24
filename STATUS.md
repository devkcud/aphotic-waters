# Ocean terrain

Regional noise blends calm offshore shelves into rugged ridges and small islands. Deep regions keep the abyssal plains and trenches. The blend uses the existing continentalness mask and fades out from Y −48 to −56.

Across 10,979 offshore base columns from seeds `0`, `42`, and `2026`, the share at Y 0 or lower fell from 93% to 42%. Floors at Y 32–62 rose from 1% to 21%. The deepest sampled floor reached Y −58. All 11,651 columns outside the basin mask matched the previous build. In the deep-profile regions, 3,715 of 3,716 sampled columns matched.

The build and packaged Forge check passed. A fresh seed-0 world generated a calm shelf with 25 blocks of water, an island crest at Y 71, an abyssal plain with 87 blocks of water, and a trench with 110 blocks of water. The two beach controls and the crater floor and rim matched their previous heights.

Human visual review is pending for the new regional terrain, gentler beaches, frozen cliffs, and cliff rock across Y 0. Use spectator mode in a fresh default world with seed `0`.

| Feature | Teleport | What to inspect |
| --- | --- | --- |
| Calm shelf | `/tp @s 128 51 -512` | The floor is at Y 37. Head west toward the island ridges. |
| Rugged shelf and islands | `/tp @s -192 80 -448` | Follow the ridge and adjacent submerged valleys. The crest rises to Y 71. |
| Abyssal plain | `/tp @s -1024 -12 -256` | Head west. The floor stays near Y −25. |
| Trench and drop-off | `/tp @s -960 -30 -512` | The floor is at Y −48. Head west toward X −1040 to see the wall rise to the surface. |
| Sandy beach | `/tp @s -162 69 1521` | Follow the sand into the shallows. Check the nearby shore at X −97, Z 1723 and the river-mouth ledge at X −48, Z 1990. |
| Andesite wall | `/tp @s 384.048 -1.891 548.735 78.6 3.2` | Follow the andesite across Y 0 at X 376, Z 555. |
| Diorite wall | `/tp @s 374.729 -1.891 530.015 47.4 5.3` | Check the diorite band at X 365, Z 540. |
