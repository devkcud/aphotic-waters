# This AGENTS.md is considered sacred. If you touch it unprompted, I will fucking hunt you. And I will find you.

## Feature planning and development

- When assigned a specific task or placed in a focused branch, prioritize that assignment over the general backlog. Do not resume unrelated work from `STATUS.md`.
- Treat `TODO.md` as the feature backlog. Resume unfinished work from STATUS.md before selecting another feature.
- Select the next feature based on dependencies and existing implementation. Plan only that feature, with concrete completion criteria and small, independently verifiable implementation slices.
- Record the active feature and its plan in `STATUS.md`. Do not create release plans or roadmaps unpromptedly.
- `TODO.md` checkboxes are managed exclusively by human developers. Every implemented feature requires manual in-game testing and human validation before it can be marked as complete. Agents must not mark TODO items as complete.
- Keep scope focused. Do not introduce speculative abstractions, unrelated refactors, dependency upgrades or CI/CD unless explicitly asked for.
- Be extremely conservative with agents. Each spawned agent must have an exclusive task and defined goal.
- Every commit will be reviewed by a human developer. Expect eyes on your work.
- Features awaiting human validation must be recorded in STATUS.md. Do not reimplement them solely because their TODO checkboxes remain unchecked.

## Code Style

- Use short, clear names and explicit visibility wherever the language permits it.
- Prefer `final`, immutable binding and immutable fields where appropriate. Use `static` for class-level members.
- Keep code spacious. Separate declarations, guards, side effects and returns with blank lines. Run formatter whenever appropriate.
- Keep functions and modules cohesive. Extract distinct responsibilities without arbitrary file-size rules.
- Do not add comments unless the logic is genuinely complex or non-obvious. Preserve required directives and licenses.
- Preserve strict typing, existing architecture and formatting conventions. Fix the responsible layer rather than duplicating workarounds.
- Inspect existing implementations and dependencies before adding new ones. Edit source files, not generated artifacts.

## Simulation and compatibility

- Respect Minecraft's client/server boundaries, chunk lifecycle, threading model and persistence conventions.
- Keep simulation bounded. Avoid unbounded scans, per-block global ticking, unnecessary allocations and forced chunk loading.
- Use deterministic generation and authoritative server state where appropriate. Consider multiplayer, save compatibility and unloaded chunks.
- Make destructive geological changes configurable and bounded. Validate them in disposable worlds, never existing user worlds without permission.
- Reuse existing systems and verify APIs against the repository's actual Minecraft, loader and mapping versions.

## Commits

- Always split-commit by coherent, reviewable changes.
- Use short imperative subjects with a capitalized verb and no trailing period: `Add depth-dependent light attenuation`.
- Preserve unrelated staged and unstaged work. If unrelated staged changes prevent a safe commit, leave them intact and report the blocker.
- Never merge other branches, cherry-pick commits or revert changes (local or otherwise) together without explicit user consent.
- Obviously, do not commit secrets, temporary files, local worlds, installed dependencies or untracked build output.
- Do not amend, rewrite history, push, tag, publish or deploy unless explicitly authorized. Do not bump versions unless explicitly authorized.

## Status and handoff

Maintain `STATUS.md` after every implementation slice and before ending a session. Keep it concise and current:

- Active feature, implementation plan and progress.
- Actual validation results and remaining checks.
- Known issues, blockers and important technical decisions.
- Next concrete implementation steps.

Completed work and commit references should not clutter `STATUS.md`. The file should NEVER contain a full tracking history of what was implemented. We are using `git` for a reason.

Expect the repository to be public. Preserve user data and unrelated changes. Keep secrets, names, paths, locations, decisions, conversations or any personal data out of tracked files and logs.

User-generated and existing documentation is holy. Do not modify the texts inside `TODO.md` or `AGENTS.md`.
