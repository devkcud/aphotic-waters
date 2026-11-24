# Ocean structure placement

New monuments and submerged wrecks use the median seafloor height across their generated footprints. The seabed blends back into the surrounding slope over 32 blocks. Data packs select eligible structures through `#aphoticwaters:seafloor_structures`.

The build and headless Forge checks passed. In fresh seed-0 worlds, the longest support across four monuments fell from 84 blocks to 4. All 58 tested keel blocks touched solid ground across five wrecks, including with wider replacement templates padded underneath. The two beached controls and 46,798 sampled columns outside the blending areas matched the baseline.

Save checks covered regenerated monument rooms and existing vanilla starts. An empty replacement tag reproduced all 130,474 sampled terrain heights. A test mod retained its chosen monument and wreck elevations, including for oversized templates. Replacement structure types and explicit terrain adaptation also kept their own placement.

Human visual review is pending. These sites are in a fresh default world with seed `0` and can be inspected in spectator mode.

| Site | Teleport | What to inspect |
| --- | --- | --- |
| Screenshot monument | `/tp @s 1404.520 79.233 -219.890 53.7 48.2` | The shelf reached Y 61 under the roof. The fitted seafloor spans Y 37–39. |
| Deep monument | `/tp @s -656 13 -252 0 25` | The foundation is at Y −15. Circle the perimeter and look underneath. |
| Rotated wreck | `/tp @s 1455 14 -270 0 45` | The hull rests at Y −2 across the slope. |

## Ocean terrain awaiting visual review

The offshore shelf ceiling keeps broad ridges underwater. Rare, fine-scale peaks let low outcrops emerge with coarse dirt and stone surfaces. Calm shelves, abyssal plains, and trenches still vary the ocean depth.

Across 10,979 offshore base columns from seeds `0`, `42`, and `2026`, columns above the waterline fell from 463 to 1. The counts at Y 0 or below and Y −40 or below matched, as did all 11,651 columns outside the basin mask. Block-resolution checks around detected outcrops found a largest exposed footprint of 17 × 14 blocks and a highest crest at Y 65.

The build and packaged Forge check passed. Fresh seed-0 chunks submerged the hooked ridge from the screenshots and generated a 15 × 14 block mound at the waterline with coarse dirt and stone. The calm shelf, abyssal plain, trench, and two beach controls matched their prior heights.

Human visual review is pending for the new regional terrain, gentler beaches, frozen cliffs, and cliff rock across Y 0. Use spectator mode in a fresh default world with seed `0`.

| Feature | Teleport | What to inspect |
| --- | --- | --- |
| Calm shelf | `/tp @s 128 51 -512` | The floor is at Y 37. Head west toward the submerged ridges. |
| Hooked ridge | `/tp @s 1740 116 -36` | Check the former screenshot view. The previous crest at Y 71 is now at Y 60. |
| Low outcrop | `/tp @s -124 76 -1047` | Look for the small stone crest and coarse dirt at the waterline. |
| Abyssal plain | `/tp @s -1024 -12 -256` | Head west. The floor stays near Y −25. |
| Trench and drop-off | `/tp @s -960 -30 -512` | The floor is at Y −48. Head west toward X −1040 to see the wall rise to the surface. |
| Sandy beach | `/tp @s -162 69 1521` | Follow the sand into the shallows. Check the nearby shore at X −97, Z 1723 and the river-mouth ledge at X −48, Z 1990. |
| Andesite wall | `/tp @s 384.048 -1.891 548.735 78.6 3.2` | Follow the andesite across Y 0 at X 376, Z 555. |
| Diorite wall | `/tp @s 374.729 -1.891 530.015 47.4 5.3` | Check the diorite band at X 365, Z 540. |
